package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet (name="/association", urlPatterns = "/association")
public class AssociationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("<html><head>" +
                "<title>GRIT</title>" +
                "<link rel='stylesheet' href='style.css'>" +
                "</head>" +
                "<body>" +
                "<header>" +
                "<h1 style='color: ae8e42;'>GRIT ACADEMY</h1>" +
                "<nav>" +
                "<a class='knapp' href=/students> STUDENTER </a>" +
                "<a class='knapp' href=/courses> KURSER </a>" +
                "<a class='knapp' href=/attendance> KURSPLAN </a>" +
                "<a class='knapp' href=/addstudent> REGISTRERA STUDENT </a>" +
                "</nav>" +
                "</header>" +
                "<div style='text-align: center;'>" +
                "<main><div>" +
                "<h2>Studenter:</h2>" +
                "<table style='margin-left: auto; margin-right: auto;'>" +
                "<tr bgcolor=ae8e42>" +
                "<th style='color: white;'>FÖRNAMN</th><th style='color: white;'>EFTERNAMN</th><th style='color: white;'>HEMSTAD</th><th style='color: white;'>HOBBY</th>" +
                "</tr>");
        try {
            Statement stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students");
            while (rs.next()) {
                out.println("<tr bgcolor= d4d4d2>" +
                        "<td>" + rs.getString(2) + "</td>" +
                        "<td>" + rs.getString(3) + "</td>" +
                        "<td>" + rs.getString(4) + "</td>" +
                        "<td>" + rs.getString(5) + "</td>" +
                        "</tr>");
            }
        } catch (SQLException e){
            System.out.println("FEL: " + e);
        }
        out.println("</table></div>" +
                "<div style='text-align: center;'>" +
                "<h2>Kurser:</h2>" +
                "<main><div>" +
                "<table style='margin-left: auto; margin-right: auto;'>" +
                "<tr bgcolor=ae8e42>" +
                "<th style='color: white;'>KURS</th><th style='color: white;'>YHP</th><th style='color: white;'>BESKRIVNING</th>" +
                "</tr>");
        try {
            Statement stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Courses");
            while (rs.next()) {
                out.println("<tr bgcolor= d4d4d2>" +
                        "<td>" + rs.getString(2) + "</td>" +
                        "<td>" + rs.getString(3) + "</td>" +
                        "<td>" + rs.getString(4) + "</td>" +
                        "</tr>");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        out.println("</table></div>" +
                "<div>" +
                "<h3>Registrera student till kurs:</h3><br><br>" +
                "<form action='/association' method='POST'>" +
                "<label for='fname'>Förnamn:</label><br>" +
                "<input type='text' id='fname' name='fname' required><br>" +
                "<label for='lname'>Efternamn:</label><br>" +
                "<input type='text' id='lname' name='lname' required><br><br>" +
                "<label for='name'>Kurs:</label><br>" +
                "<input type='text' id='name' name='name' required><br><br>" +
                "<input type='submit' value='Registrera'>" +
                "</main></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int studentId = 0;
        int courseId = 0;

        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");

        String courseName = req.getParameter("name");

        // Hämtar student_ID
        try {
            PreparedStatement ps = connect().prepareStatement("SELECT student_id FROM students WHERE fname = ? AND lname = ?");
            ps.setString(1, fname);
            ps.setString(2, lname);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                studentId = rs.getInt(1);
            }
            } catch (SQLException e) {
                System.out.println(e);
            }
        // Hämtar course_ID
        try {
            PreparedStatement ps = connect().prepareStatement("SELECT course_id FROM courses WHERE name = ?");
            ps.setString(1, courseName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                courseId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("student_id: " + studentId + " course_id: " + courseId);

        try {
            PreparedStatement ps = connect().prepareStatement("INSERT INTO attendance (students_id, subject_id) VALUES ( ?, ?)");
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            int regStudent = ps.executeUpdate();
            if (regStudent > 0) {
                System.out.println("Student registrerad till kurs");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        resp.sendRedirect("/attendance");
    }

    public Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:4306/gritacademy", "Oliver", "quicksea988");
        } catch (Exception e) {
            System.out.println("FEL: " + e);
            return null;
        }
    }
}
