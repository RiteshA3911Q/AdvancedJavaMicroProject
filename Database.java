import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
    private static final String URL =
            "jdbc:mysql://localhost:3306/marks_management?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveStudent(String id, String name, int[] marks, int total,
                            double percentage, String grade) throws SQLException {
        String sql = "INSERT INTO students "
                + "(student_id, student_name, subject1, subject2, subject3, "
                + "subject4, subject5, total, percentage, grade) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setStudentValues(statement, id, name, marks, total, percentage, grade);
            statement.executeUpdate();
        }
    }

    public int updateStudent(String id, String name, int[] marks, int total,
                             double percentage, String grade) throws SQLException {
        String sql = "UPDATE students SET student_name = ?, subject1 = ?, "
                + "subject2 = ?, subject3 = ?, subject4 = ?, subject5 = ?, "
                + "total = ?, percentage = ?, grade = ? WHERE student_id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, marks[0]);
            statement.setInt(3, marks[1]);
            statement.setInt(4, marks[2]);
            statement.setInt(5, marks[3]);
            statement.setInt(6, marks[4]);
            statement.setInt(7, total);
            statement.setDouble(8, percentage);
            statement.setString(9, grade);
            statement.setString(10, id);
            return statement.executeUpdate();
        }
    }

    public int deleteStudent(String id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.executeUpdate();
        }
    }

    public ArrayList<Object[]> getStudents(String searchId) throws SQLException {
        ArrayList<Object[]> students = new ArrayList<Object[]>();
        String sql = "SELECT student_id, student_name, subject1, subject2, "
                + "subject3, subject4, subject5, total, percentage, grade "
                + "FROM students";

        if (searchId != null && !searchId.trim().isEmpty()) {
            sql += " WHERE student_id = ?";
        }
        sql += " ORDER BY student_id";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (searchId != null && !searchId.trim().isEmpty()) {
                statement.setString(1, searchId.trim());
            }

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    students.add(new Object[] {
                            result.getString("student_id"),
                            result.getString("student_name"),
                            result.getInt("subject1"),
                            result.getInt("subject2"),
                            result.getInt("subject3"),
                            result.getInt("subject4"),
                            result.getInt("subject5"),
                            result.getInt("total"),
                            result.getDouble("percentage"),
                            result.getString("grade")
                    });
                }
            }
        }
        return students;
    }

    private void setStudentValues(PreparedStatement statement, String id,
                                  String name, int[] marks, int total,
                                  double percentage, String grade)
            throws SQLException {
        statement.setString(1, id);
        statement.setString(2, name);
        statement.setInt(3, marks[0]);
        statement.setInt(4, marks[1]);
        statement.setInt(5, marks[2]);
        statement.setInt(6, marks[3]);
        statement.setInt(7, marks[4]);
        statement.setInt(8, total);
        statement.setDouble(9, percentage);
        statement.setString(10, grade);
    }
}
