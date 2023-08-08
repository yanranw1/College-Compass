import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/api/employee-login")
public class EmployeeLoginServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user_email = request.getParameter("username");
        String user_password = request.getParameter("password");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        response.setContentType("application/json"); // Response mime type
        // Verify reCAPTCHA
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", e.getMessage());

            response.getWriter().write(responseJsonObject.toString());
            return;
        }
        PrintWriter out = response.getWriter();
        int pass = 0;
        int user_exist = 0;

        try (out; Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            // Construct a query with parameter represented by "?"

            String query = String.format("SELECT COUNT(1) FROM employees WHERE email = '%s' and password = '%s'",user_email, user_password);
            String query_1 = String.format("SELECT COUNT(1) FROM employees WHERE email = '%s'", user_email);
            PreparedStatement statement = conn.prepareStatement(query);
            PreparedStatement statement1 = conn.prepareStatement(query_1);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                pass = rs.getInt(1);
            }
            rs.close();
            ResultSet rs_1 = statement1.executeQuery(query_1);
            while (rs_1.next()) {
                user_exist = rs_1.getInt(1);
            }
            rs_1.close();
            statement.close();
            statement1.close();

            JsonObject responseJsonObject = new JsonObject();
            if (pass==1) {
                request.getSession().setAttribute("user", new User(user_email));
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            }
            else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (user_exist==0) {
                    responseJsonObject.addProperty("message", "user " + user_email + " doesn't exist");
//                    System.out.println("%%0.1");
                } else {
                    responseJsonObject.addProperty("message", "incorrect password");
//                    System.out.println("%%0.2");
                }
            }
            response.getWriter().write(responseJsonObject.toString());
//            System.out.println("%%5");
            // Declare our statement
        }
//      ENCODE EVERY POSSIBLE VARIABLE IN FRONT END LINK
        catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        finally {
            out.close();
        }
    }
}