package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
@WebServlet(urlPatterns = "/courses")
public class CoursesServlet extends HttpServlet {

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
                "<a class='knapp' href=/attendance> KURSPLAN </a>" +
                "<a class='knapp' href=/addstudent> REGISTRERA STUDENT </a>" +
                "<a class='knapp' href=/association> REGISTRERA TILL KURS </a>" +
                "</nav>" +
                "</header>" +
                "<div style='text-align: center;'>" +
                "<main><div>" +
                "<h2>Kurser:</h2>" +
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
                "<h2>Lägg till kurs:</h2><br><br>" +
                "<form action='/courses' method='POST'>" +
                "<label for='name'>Namn:</label><br>" +
                "<input type='text' id='name' name='name' required><br><br>" +
                "<label for='YHP'>YHP:</label><br>" +
                "<input type='text' id='YHP' name='YHP' required><br><br>" +
                "<label for='description'>Beskrivning:</label><br>" +
                "<input type='text' id='description' name='description' required><br><br>" +
                "<input type='submit' value='Lägg till'></div></main>" +
                "</body></html>");
        System.out.println("GET Request");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        resp.setContentType("text/html");

        try{

            PreparedStatement statement = connect().prepareStatement("SELECT COUNT(*) FROM courses WHERE name = ?");
            statement.setString(1, req.getParameter("name"));
            ResultSet rs = statement.executeQuery();
            if(rs.next() && rs.getInt(1) > 0) { // Icke fungerande alert, men stoppar tillägg av kurs med samma namn
                out.println("<script type='text/javascript'>" +
                        "alert('Denna kurs finns redan, testa en annan.');" +
                        "location='/courses';" +
                        "</script>");
            } else {
                PreparedStatement ps = connect().prepareStatement("INSERT INTO courses (name, YHP, description) VALUES (?, ?, ?)");
                ps.setString(1, req.getParameter("name"));
                ps.setString(2, req.getParameter("YHP"));
                ps.setString(3, req.getParameter("description"));
                int addCourse = ps.executeUpdate();
                if (addCourse>0) {
                    System.out.println("Kurs tillagd");
                } else {
                    System.out.println("Kurs INTE tillagd");
                }
            }
        } catch (SQLException e) {
            System.out.println("FEL: " + e);
        }
        resp.sendRedirect("/courses");
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