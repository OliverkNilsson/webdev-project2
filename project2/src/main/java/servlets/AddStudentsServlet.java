package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "/addstudent", urlPatterns = "/addstudent")
public class AddStudentsServlet extends HttpServlet {
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
                "<a class='knapp' href=/students> STUDENTER </a>" +
                "<a class='knapp' href=/attendance> KURSPLAN </a>" +
                "<a class='knapp' href=/association> REGISTRERA TILL KURS </a>" +
                "</nav>" +
                "</header>" +
                "<main><div>" +
                    "<h2>Anmäl dig till skolan:</h2>" +

                "<form action='/addstudent' method='POST'>" +
                    "<label for='fname'>Förnamn:</label><br>" +
                    "<input type='text' id='fname' name='fname' required><br>" +
                    "<label for='lname'>Efternamn:</label><br>" +
                    "<input type='text' id='lname' name='lname' required><br>" +
                    "<label for='town'>Hemstad:</label><br>" +
                    "<input type='text' id='town' name='town' required><br>" +
                    "<label for='hobby'>Hobby:</label><br>" +
                    "<input type='text' id='hobby' name='hobby'><br><br>" +
                    "<input type='submit' value='Anmäl'>" +
                "</form></div>" +
                "</main>" +
                "</body>" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            PreparedStatement ps = connect().prepareStatement("INSERT INTO students (fname, lname, town, hobby) VALUES (?,?,?,?)");
            ps.setString(1, req.getParameter("fname"));
            ps.setString(2, req.getParameter("lname"));
            ps.setString(3, req.getParameter("town"));
            ps.setString(4, req.getParameter("hobby"));
            int addStudent = ps.executeUpdate();
            if (addStudent > 0) {
                System.out.println("Student tillagd");
            } else {
                System.out.println("Student INTE tillagd");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.sendRedirect("/students");

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
