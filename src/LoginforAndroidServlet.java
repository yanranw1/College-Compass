import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginforAndroidServlet", urlPatterns = "/api/android-login")
public class LoginforAndroidServlet extends HttpServlet {
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
        System.out.println("android-login starts");

        response.setContentType("application/json"); // Response mime type


        PrintWriter out = response.getWriter();

        String user_email = request.getParameter("username");
        System.out.println(user_email);

        String user_password = request.getParameter("password");
        System.out.println(user_password);

        int pass = 0;
        int user_exist = 0;

        try (out; Connection conn = dataSource.getConnection()) {
            String query = String.format("SELECT * from user where email='%s'", user_email);
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery(query);
            boolean success = false;
            String encryptedPassword = "";
            if (rs.next()) {
                encryptedPassword = rs.getString("password");
            }
            System.out.println(user_password);
            System.out.println(encryptedPassword);
            if(!encryptedPassword.equals("")){
            success = new StrongPasswordEncryptor().checkPassword(user_password, encryptedPassword);}

            System.out.println("yes");
            if(success)
                pass = 1;
            rs.close();

            String query_1 = String.format("SELECT COUNT(1) FROM user WHERE email = '%s'", user_email);
            PreparedStatement statement1 = conn.prepareStatement(query_1);
            ResultSet rs_1 = statement1.executeQuery(query_1);
            while (rs_1.next()) {
                user_exist = rs_1.getInt(1);
            }
            rs_1.close();

            JsonObject responseJsonObject = new JsonObject();
            if (pass==1) {

                String query_2 = String.format("SELECT * FROM user WHERE email = '%s' and password = '%s'",user_email, user_password);
                PreparedStatement statement2 = conn.prepareStatement(query_2);
                ResultSet rs_2 = statement2.executeQuery(query_2);
                String user_id = "";
                System.out.println("here");
                while (rs_2.next()) {
                    user_id = rs_2.getString("id");
                }
                System.out.println(user_id);

                rs_2.close();
                responseJsonObject.addProperty("user_id", user_id);

                request.getSession().setAttribute("user", new User(user_email));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

                statement2.close();

            }
            else {
                responseJsonObject.addProperty("status", "fail");
                request.getServletContext().log("Login failed");
                if (user_exist==0) {
                    responseJsonObject.addProperty("message", "user " + user_email + " doesn't exist");
                } else {
                    responseJsonObject.addProperty("message", "incorrect password");
                }
            }
            statement.close();
            statement1.close();
            response.getWriter().write(responseJsonObject.toString());
        }

        catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            request.getServletContext().log("Error:", e);
            response.setStatus(500);
        }
        finally {
            out.close();
        }
    }
}