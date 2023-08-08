import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AddGenre", urlPatterns = "/api/add-genre")
public class AddGenre extends HttpServlet {
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
        response.setContentType("application/json"); // Response mime type
        System.out.println("0");

        PrintWriter out = response.getWriter();

        String genre_name = request.getParameter("fullname");

        System.out.println("1");

        String error_message = "";

        System.out.println("2");
        if(genre_name.length()<1 )
        {
            error_message = "Please complete all required part.\n";
        }

        JsonObject responseJsonObject = new JsonObject();
        if (error_message.length() > 0) {
            System.out.println("2.1");
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", error_message);
            System.out.println("2.2");
            System.out.println(error_message);
            response.getWriter().write(responseJsonObject.toString());
            System.out.println("2.3");
        } else {
            boolean exist = false;
            try (out; Connection conn = dataSource.getConnection()) {
                System.out.println("3");


                String check_query = String.format("SELECT COUNT(1) FROM genre WHERE fullname = '%s'",genre_name);
                System.out.println("30");

                PreparedStatement statement_check = conn.prepareStatement(check_query);
                System.out.println("31");

                ResultSet rs = statement_check.executeQuery(check_query);
                System.out.println("32");

                while (rs.next()) {
                    if (0 < rs.getInt(1)) {
                        exist = true;
                    }
                }
                System.out.println("33");
                System.out.println(exist);
                statement_check.close();

                if (exist) {
                    System.out.println("error happened");
                    error_message += "This genre already existed";
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", error_message);
                    System.out.println(error_message);
                } else {
                    String query = "CALL add_genre(?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, genre_name);

                    System.out.println("4");

                    int rowsAffected = statement.executeUpdate();
                    System.out.println("5");

                    if (rowsAffected == 1) {
                        System.out.println("rowsAffected");
                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success");
                    } else {
                        System.out.println("error happened");
                        error_message += "unable to add this genre to database";
                        responseJsonObject.addProperty("status", "fail");
                        responseJsonObject.addProperty("message", error_message);
                        System.out.println(error_message);
                    }
                    System.out.println("6");
                    statement.close();
                }
                response.getWriter().write(responseJsonObject.toString());
            } catch (Exception e) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", e.getMessage());
                out.write(jsonObject.toString());
                request.getServletContext().log("Error:", e);
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
    }
}