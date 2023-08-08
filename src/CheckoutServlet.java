

import com.google.gson.JsonArray;
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
import java.sql.Statement;

// Declaring a WebServlet called SchoolsServlet, which maps to url "/api/school"
@WebServlet(name = "CheckoutServlet", urlPatterns = "/api/check")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(0);

        response.setContentType("application/json"); // Response mime type
        String user_id = request.getParameter("user_id");
        System.out.println(1);

        String school_array = request.getParameter("school_array");
        System.out.println(2);

        String matching_rate = request.getParameter("matching_rate");
        System.out.println(3);

        System.out.println("user_id" + user_id);
        System.out.println("school_array" + school_array);
        System.out.println("matching_rate" + matching_rate);
        System.out.println("matching_rate len" + matching_rate.length());


        String arrayContent = school_array.substring(1, school_array.length()-1);
        System.out.println("arrayContent" + arrayContent);

        String arrayContent2 = matching_rate.substring(1, matching_rate.length()-1);

        String[] schools = arrayContent.split(",");
        String[] rate = arrayContent2.split(",");

        System.out.println(schools[0]);
        System.out.println(rate[0]);
        System.out.println(schools.length);


        String query = "Insert into recommendation(user_id, school_id, matching_rate) values ";
        // The log message can be found in localhost log
        for(int i = 0 ; i<schools.length; i++ ){
            query += "('"+user_id+"',"+schools[i]+","+rate[i]+"),";
        }
        query = query.substring(0, query.length()-1);
        query += ";";

        System.out.println("query");
        System.out.println(query);

        PrintWriter out = response.getWriter();

        try (out; Connection conn = dataSource.getConnection()) {
            System.out.println("entered");
            PreparedStatement statement = conn.prepareStatement(query);
            System.out.println("statement");
//            ResultSet rs = statement.executeQuery(query);
            statement.executeUpdate();
            System.out.println("execute");
//            rs.close();
            statement.close();
            response.setStatus(200);
        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
