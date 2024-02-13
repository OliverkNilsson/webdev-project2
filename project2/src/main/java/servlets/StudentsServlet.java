package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet (name="/students", urlPatterns = "/students")
public class StudentsServlet extends HttpServlet {
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
                "<a class='knapp' href=/courses> KURSER </a>" +
                "<a class='knapp' href=/attendance> KURSPLAN </a>" +
                "<a class='knapp' href=/addstudent> REGISTRERA STUDENT </a>" +
                "<a class='knapp' href=/association> REGISTRERA TILL KURS </a>" +
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
                "<div>" +
                "<h2>Se students kursplan:</h2><br><br>" +
                "<form action='/students' method='POST'>" +
                "<label for='fname'>Förnamn:</label><br>" +
                "<input type='text' id='fname' name='fname' required><br>" +
                "<label for='lname'>Efternamn:</label><br>" +
                "<input type='text' id='lname' name='lname' required><br><br>" +
                "<input type='submit' value='Sök'></div></main>" +
                "</body></html>");
        System.out.println("GET Request");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out =resp.getWriter();

        out.println("<html><head>" +
                "<title>GRIT</title>" +
                "<link rel='stylesheet' href='style.css'>" +
                "</head>" +
                "<body>" +
                "<header>" +
                "<h1 style='color: ae8e42;'>GRIT ACADEMY</h1>" +
                "<nav>" +
                "<a class='knapp' href=/courses> KURSER </a>" +
                "<a class='knapp' href=/attendance> KURSPLAN </a>" +
                "<a class='knapp' href=/addstudent> REGISTRERA STUDENT </a>" +
                "<a class='knapp' href=/association> REGISTRERA TILL KURS </a>" +
                "</nav>" +
                "</header>" +
                "<div style='text-align: center;'>" +
                "<h2>Kursplan:</h2>" +
                "<table style='margin-left: auto; margin-right: auto;'>" +
                "<tr bgcolor=ae8e42>" +
                "<th style='color: white;'>FÖRNAMN</th><th style='color: white;'>EFTERNAMN</th><th style='color: white;'>HEMSTAD</th><th style='color: white;'>HOBBY</th>" +
                "</tr>");

        try {
            PreparedStatement ps = connect().prepareStatement("SELECT s.fname, c.name, c.YHP, c.description FROM attendance a " +
                                                                   "JOIN students s ON a.students_id = s.student_id JOIN courses c ON a.subject_id = c.course_id " +
                                                                    "WHERE s.fname = ? AND s.lname = ?");
            ps.setString(1, req.getParameter("fname"));
            ps.setString(2, req.getParameter("lname"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.println("<tr bgcolor= d4d4d2>" +
                        "<td>" + rs.getString("fname") + "</td>" +
                        "<td>" + rs.getString("name") + "</td>" +
                        "<td>" + rs.getInt("YHP") + "</td>" +
                        "<td>" + rs.getString("description") + "</td>" +
                        "</tr>");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        out.println("</table><br><br>" +
                "<button type=button id=reset onClick=location.href='/students'>ÅTERSTÄLL</button>" +
                "</div>");
        System.out.println("GET Request");
    }

    public Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:4306/gritacademy", "user", "user");
        } catch (Exception e) {
            System.out.println("FEL: " + e);
            return null;
        }
    }
}


