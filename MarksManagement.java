import java.awt.*;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class MarksManagement extends JFrame
{
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField[] markFields = new JTextField[5];
    private final JTextField totalField = new JTextField();
    private final JTextField percentageField = new JTextField();
    private final JTextField gradeField = new JTextField();
    private final JTextField searchField = new JTextField(12);
    private final DefaultTableModel tableModel;
    private final JTable table;
    private Database database;

    public MarksManagement()
{
        setTitle("Student Marks Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        totalField.setEditable(false);
        percentageField.setEditable(false);
        gradeField.setEditable(false);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Input", createInputPanel());

        String[] columns =
        {"Student ID", "Student Name", "Subject 1", "Subject 2",
                "Subject 3", "Subject 4", "Subject 5", "Total", "Percentage", "Grade"};
        tableModel = new DefaultTableModel(columns, 0)
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        setColumnWidths();
        tabs.addTab("Display", createDisplayPanel());
        add(tabs);
        try
        {
            database = new Database();
            loadStudents("");
        } catch (SQLException exception)
        {
            showError("Could not initialize the database: " + exception.getMessage());
        }
    }

    private JPanel createInputPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JPanel form = new JPanel(new GridLayout(1, 3, 14, 0));
        form.add(createStudentInfoPanel());
        form.add(createSubjectPanel());
        form.add(createResultPanel());

        JLabel title = new JLabel("Enter Student Marks");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        JButton calculateButton = createButton("Calculate");
        JButton saveButton = createButton("Save");
        JButton updateButton = createButton("Update");
        JButton deleteButton = createButton("Delete");
        JButton clearButton = createButton("Clear");
        calculateButton.addActionListener(event -> calculateResult());
        saveButton.addActionListener(event -> saveStudent());
        updateButton.addActionListener(event -> updateStudent());
        deleteButton.addActionListener(event -> deleteStudent());
        clearButton.addActionListener(event -> clearFields());
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStudentInfoPanel()
{
        JPanel panel = createSectionPanel("Student Information");
        JPanel fields = createFormFields();
        addFieldRow(fields, "Student ID:", idField, 0);
        addFieldRow(fields, "Student Name:", nameField, 1);
        panel.add(fields, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createSubjectPanel()
{
        JPanel panel = createSectionPanel("Subject Marks");
        JPanel fields = createFormFields();
        for (int i = 0; i < markFields.length; i++)
{
            markFields[i] = new JTextField();
            addFieldRow(fields, "Subject " + (i + 1) + ":", markFields[i], i);
        }
        panel.add(fields, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createResultPanel()
    {
        JPanel panel = createSectionPanel("Result");
        JPanel fields = createFormFields();
        totalField.setEditable(false);
        percentageField.setEditable(false);
        gradeField.setEditable(false);
        styleReadOnlyField(totalField);
        styleReadOnlyField(percentageField);
        styleReadOnlyField(gradeField);
        addFieldRow(fields, "Total:", totalField, 0);
        addFieldRow(fields, "Percentage:", percentageField, 1);
        addFieldRow(fields, "Grade:", gradeField, 2);
        panel.add(fields, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createSectionPanel(String title)
    {
        JPanel panel = new JPanel(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 190, 200)),
                title);
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD, 13f));
        panel.setBorder(BorderFactory.createCompoundBorder(
                border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return panel;
    }

    private JPanel createFormFields()
    {
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        return fields;
    }

    private void addFieldRow(JPanel panel, String labelText, JTextField field, int row)
    {
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(105, 25));
        styleField(field);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.insets = new Insets(4, 0, 4, 8);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(4, 0, 4, 0);
        panel.add(field, fieldConstraints);
    }

    private void styleField(JTextField field)
    {
        field.setPreferredSize(new Dimension(135, 28));
        field.setMinimumSize(new Dimension(80, 28));
    }

    private void styleReadOnlyField(JTextField field)
    {
        field.setBackground(new Color(238, 242, 246));
    }

    private JButton createButton(String text)
    {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 32));
        return button;
    }

    private JPanel createDisplayPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        JLabel searchLabel = new JLabel("Search Student ID:");
        searchLabel.setFont(searchLabel.getFont().deriveFont(Font.BOLD));
        styleField(searchField);
        JButton searchButton = createButton("Search");
        JButton refreshButton = createButton("Refresh");
        searchButton.addActionListener(event -> loadStudents(searchField.getText()));
        refreshButton.addActionListener(event ->
        {
            searchField.setText("");
            loadStudents("");
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        panel.add(searchPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 190, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setColumnWidths()
    {
        int[] widths =
        {90, 150, 80, 80, 80, 80, 80, 75, 95, 65};
        for (int i = 0; i < widths.length; i++)
        {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private int[] readMarks()
    {
        int[] marks = new int[5];
        for (int i = 0; i < markFields.length; i++)
        {
            try
            {
                marks[i] = Integer.parseInt(markFields[i].getText().trim());
            } catch (NumberFormatException exception)
            {
                throw new IllegalArgumentException("Enter a whole number for Subject "
                        + (i + 1) + ".");
            }
            if (marks[i] < 0 || marks[i] > 100)
            {
                throw new IllegalArgumentException("Each mark must be between 0 and 100.");
            }
        }
        return marks;
    }

    private boolean hasRequiredDetails()
    {
        if (idField.getText().trim().isEmpty())
        {
            showError("Student ID is required.");
            return false;
        }
        if (nameField.getText().trim().isEmpty())
        {
            showError("Student name is required.");
            return false;
        }
        return true;
    }

    private int[] calculateResult()
    {
        if (!hasRequiredDetails())
        {
            return null;
        }
        try
        {
            int[] marks = readMarks();
            int total = 0;
            for (int mark : marks)
            {
                total += mark;
            }
            double percentage = total / 5.0;
            totalField.setText(String.valueOf(total));
            percentageField.setText(String.format("%.2f", percentage));
            gradeField.setText(getGrade(percentage));
            return marks;
        } catch (IllegalArgumentException exception)
        {
            showError(exception.getMessage());
            return null;
        }
    }

    private String getGrade(double percentage)
    {
        if (percentage >= 90)
        {
            return "A+";
        } else if (percentage >= 80)
        {
            return "A";
        } else if (percentage >= 70)
        {
            return "B";
        } else if (percentage >= 60)
        {
            return "C";
        } else if (percentage >= 50)
        {
            return "D";
        }
        return "F";
    }

    private void saveStudent()
    {
        if (database == null)
        {
            return;
        }
        int[] marks = calculateResult();
        if (marks == null)
        {
            return;
        }
        try
        {
            database.saveStudent(idField.getText().trim(), nameField.getText().trim(),
                    marks, Integer.parseInt(totalField.getText()),
                    Double.parseDouble(percentageField.getText()), gradeField.getText());
            JOptionPane.showMessageDialog(this, "Student saved successfully.");
            loadStudents("");
            clearFields();
        } catch (SQLException exception)
        {
            showError("Could not save student: " + exception.getMessage());
        }
    }

    private void updateStudent()
    {
        if (database == null)
        {
            return;
        }
        int[] marks = calculateResult();
        if (marks == null)
        {
            return;
        }
        try
        {
            int updated = database.updateStudent(idField.getText().trim(),
                    nameField.getText().trim(),
                    marks, Integer.parseInt(totalField.getText()),
                    Double.parseDouble(percentageField.getText()), gradeField.getText());
            if (updated == 0)
            {
                showError("No student found with that ID.");
            } else
            {
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                loadStudents("");
            }
        } catch (SQLException exception)
        {
            showError("Could not update student: " + exception.getMessage());
        }
    }

    private void deleteStudent()
    {
        if (database == null)
        {
            return;
        }
        String id = idField.getText().trim();
        if (id.isEmpty())
        {
            showError("Enter a Student ID to delete.");
            return;
        }
        try
        {
            int deleted = database.deleteStudent(id);
            if (deleted == 0)
            {
                showError("No student found with that ID.");
            } else
            {
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                loadStudents("");
                clearFields();
            }
        } catch (SQLException exception)
        {
            showError("Could not delete student: " + exception.getMessage());
        }
    }

    private void loadStudents(String searchId)
    {
        if (database == null)
        {
            return;
        }
        try
        {
            ArrayList<Object[]> students = database.getStudents(searchId);
            tableModel.setRowCount(0);
            for (Object[] student : students)
            {
                tableModel.addRow(student);
            }
        } catch (SQLException exception)
        {
            showError("Could not load students: " + exception.getMessage());
        }
    }

    private void clearFields()
    {
        idField.setText("");
        nameField.setText("");
        for (JTextField field : markFields)
        {
            field.setText("");
        }
        totalField.setText("");
        percentageField.setText("");
        gradeField.setText("");
    }

    private void showError(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new MarksManagement().setVisible(true));
    }
}
