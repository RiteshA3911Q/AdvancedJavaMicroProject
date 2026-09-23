import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MarksManagement extends JFrame {
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField[] markFields = new JTextField[5];
    private final JTextField totalField = new JTextField();
    private final JTextField percentageField = new JTextField();
    private final JTextField gradeField = new JTextField();
    private final JTextField searchField = new JTextField(12);
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final Database database = new Database();

    public MarksManagement() {
        setTitle("Student Marks Management System");
        setSize(950, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        totalField.setEditable(false);
        percentageField.setEditable(false);
        gradeField.setEditable(false);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Input", createInputPanel());

        String[] columns = {"Student ID", "Student Name", "Subject 1", "Subject 2",
                "Subject 3", "Subject 4", "Subject 5", "Total", "Percentage", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        tabs.addTab("Display", createDisplayPanel());
        add(tabs);
        loadStudents("");
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel fields = new JPanel(new GridLayout(9, 2, 5, 5));

        fields.add(new JLabel("Student ID:"));
        fields.add(idField);
        fields.add(new JLabel("Student Name:"));
        fields.add(nameField);
        for (int i = 0; i < markFields.length; i++) {
            markFields[i] = new JTextField();
            fields.add(new JLabel("Subject " + (i + 1) + " Marks:"));
            fields.add(markFields[i]);
        }
        fields.add(new JLabel("Total:"));
        fields.add(totalField);
        fields.add(new JLabel("Percentage:"));
        fields.add(percentageField);
        fields.add(new JLabel("Grade:"));
        fields.add(gradeField);

        JPanel buttons = new JPanel(new FlowLayout());
        JButton calculateButton = new JButton("Calculate");
        JButton saveButton = new JButton("Save");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        calculateButton.addActionListener(event -> calculateResult());
        saveButton.addActionListener(event -> saveStudent());
        updateButton.addActionListener(event -> updateStudent());
        deleteButton.addActionListener(event -> deleteStudent());
        clearButton.addActionListener(event -> clearFields());
        buttons.add(calculateButton);
        buttons.add(saveButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        panel.add(fields, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");
        searchButton.addActionListener(event -> loadStudents(searchField.getText()));
        refreshButton.addActionListener(event -> {
            searchField.setText("");
            loadStudents("");
        });
        searchPanel.add(new JLabel("Search Student ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private int[] readMarks() {
        int[] marks = new int[5];
        for (int i = 0; i < markFields.length; i++) {
            try {
                marks[i] = Integer.parseInt(markFields[i].getText().trim());
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Enter a whole number for Subject "
                        + (i + 1) + ".");
            }
            if (marks[i] < 0 || marks[i] > 100) {
                throw new IllegalArgumentException("Each mark must be between 0 and 100.");
            }
        }
        return marks;
    }

    private boolean hasRequiredDetails() {
        if (idField.getText().trim().isEmpty()) {
            showError("Student ID is required.");
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            showError("Student name is required.");
            return false;
        }
        return true;
    }

    private int[] calculateResult() {
        if (!hasRequiredDetails()) {
            return null;
        }
        try {
            int[] marks = readMarks();
            int total = 0;
            for (int mark : marks) {
                total += mark;
            }
            double percentage = total / 5.0;
            totalField.setText(String.valueOf(total));
            percentageField.setText(String.format("%.2f", percentage));
            gradeField.setText(getGrade(percentage));
            return marks;
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
            return null;
        }
    }

    private String getGrade(double percentage) {
        if (percentage >= 90) {
            return "A+";
        } else if (percentage >= 80) {
            return "A";
        } else if (percentage >= 70) {
            return "B";
        } else if (percentage >= 60) {
            return "C";
        } else if (percentage >= 50) {
            return "D";
        }
        return "F";
    }

    private void saveStudent() {
        int[] marks = calculateResult();
        if (marks == null) {
            return;
        }
        try {
            database.saveStudent(idField.getText().trim(), nameField.getText().trim(),
                    marks, Integer.parseInt(totalField.getText()),
                    Double.parseDouble(percentageField.getText()), gradeField.getText());
            JOptionPane.showMessageDialog(this, "Student saved successfully.");
            loadStudents("");
            clearFields();
        } catch (SQLException exception) {
            showError("Could not save student: " + exception.getMessage());
        }
    }

    private void updateStudent() {
        int[] marks = calculateResult();
        if (marks == null) {
            return;
        }
        try {
            int updated = database.updateStudent(idField.getText().trim(),
                    nameField.getText().trim(),
                    marks, Integer.parseInt(totalField.getText()),
                    Double.parseDouble(percentageField.getText()), gradeField.getText());
            if (updated == 0) {
                showError("No student found with that ID.");
            } else {
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                loadStudents("");
            }
        } catch (SQLException exception) {
            showError("Could not update student: " + exception.getMessage());
        }
    }

    private void deleteStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showError("Enter a Student ID to delete.");
            return;
        }
        try {
            int deleted = database.deleteStudent(id);
            if (deleted == 0) {
                showError("No student found with that ID.");
            } else {
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                loadStudents("");
                clearFields();
            }
        } catch (SQLException exception) {
            showError("Could not delete student: " + exception.getMessage());
        }
    }

    private void loadStudents(String searchId) {
        try {
            ArrayList<Object[]> students = database.getStudents(searchId);
            tableModel.setRowCount(0);
            for (Object[] student : students) {
                tableModel.addRow(student);
            }
        } catch (SQLException exception) {
            showError("Could not load students: " + exception.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        for (JTextField field : markFields) {
            field.setText("");
        }
        totalField.setText("");
        percentageField.setText("");
        gradeField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MarksManagement().setVisible(true));
    }
}
