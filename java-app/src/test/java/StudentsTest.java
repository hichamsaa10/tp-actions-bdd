import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.Optional;
import org.junit.jupiter.api.*;

class StudentsTest {

    static Connection conn;

    @BeforeAll
    static void connect() throws SQLException {
        conn = Students.connect();
    }

    @AfterAll
    static void close() throws SQLException {
        conn.close();
    }

    @BeforeEach
    void clean() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM students");
        }
    }

    @Test
    void ajoutEtLecture() throws SQLException {
        int id = Students.addStudent(conn, "Sara", 17);
        Optional<String[]> s = Students.getStudent(conn, id);
        assertTrue(s.isPresent());
        assertEquals("Sara", s.get()[0]);
        assertEquals(17.0, Double.parseDouble(s.get()[1]));
    }

    @Test
    void etudiantInexistant() throws SQLException {
        assertEquals(Optional.empty().isPresent(),
                     Students.getStudent(conn, 99999).isPresent());
    }

    @Test
    void moyenne() throws SQLException {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);
        assertEquals(12.0, Students.averageGrade(conn));
    }
}
