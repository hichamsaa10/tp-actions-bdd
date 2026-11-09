import java.sql.*;
import java.util.Optional;

public class Students {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/tp", "postgres", "postgres");
    }

    public static int addStudent(Connection conn, String name, double grade) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id")) {
            ps.setString(1, name);
            ps.setDouble(2, grade);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public static Optional<String[]> getStudent(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name, grade FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new String[] {rs.getString("name"), rs.getString("grade")});
            }
        }
    }

    public static double averageGrade(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT AVG(grade) FROM students")) {
            rs.next();
            return rs.getDouble(1);
        }
    }
}
