import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

// --------------------------
// Easy Level: User Login Servlet
// --------------------------

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Simple validation (In real life, this would be database-driven)
        if ("admin".equals(username) && "password123".equals(password)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h2>Welcome, " + username + "!</h2>");
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h2>Invalid username or password!</h2>");
        }
    }
}

// --------------------------
// Medium Level: Employee Servlet with JDBC
// --------------------------

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    
    // Database utility to get connection (adjust according to your DB credentials)
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/your_database_name";
        String user = "your_username";
        String password = "your_password";
        return DriverManager.getConnection(url, user, password);
    }

    // Get Employee data and Search by ID
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchId = request.getParameter("searchId");

        if (searchId != null && !searchId.isEmpty()) {
            // Search employee by ID
            try (Connection conn = getConnection()) {
                String query = "SELECT * FROM employees WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(searchId));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<h2>Employee Details</h2>");
                    out.println("<p>ID: " + rs.getInt("id") + "</p>");
                    out.println("<p>Name: " + rs.getString("name") + "</p>");
                    out.println("<p>Position: " + rs.getString("position") + "</p>");
                    out.println("<p>Salary: " + rs.getDouble("salary") + "</p>");
                } else {
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<h2>No employee found with ID " + searchId + "</h2>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Display all employees
            try (Connection conn = getConnection()) {
                String query = "SELECT * FROM employees";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<h2>Employee List</h2>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Position</th><th>Salary</th></tr>");

                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("position") + "</td><td>" + rs.getDouble("salary") + "</td></tr>");
                }

                out.println("</table>");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

// --------------------------
// Hard Level: Student Attendance Portal using JSP
// --------------------------

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    
    // Database utility to get connection (adjust according to your DB credentials)
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/your_database_name";
        String user = "your_username";
        String password = "your_password";
        return DriverManager.getConnection(url, user, password);
    }

    // Save attendance details
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setDate(2, Date.valueOf(date));
            stmt.setString(3, status);

            int result = stmt.executeUpdate();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            if (result > 0) {
                out.println("<h2>Attendance saved successfully!</h2>");
            } else {
                out.println("<h2>Failed to save attendance.</h2>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
