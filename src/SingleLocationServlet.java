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

// Declaring a WebServlet called SingleSchoolServlet, which maps to url "/api/single-school"
@WebServlet(name = "SingleLocationServlet", urlPatterns = "/api/single-location")
public class SingleLocationServlet extends HttpServlet {
    private static final long serialVersionUID = 3L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");
        System.out.println("id2"+id);
        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (out; Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            // Construct a query with parameter represented by "?"
            String query = "SELECT * from school as s, schools_in_locations as sil, location as l where l.location_id = sil.location_id and sil.id = s.id and l.location_id = ?";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);
            query += "where ORDER BY s.rating DESC,s.name ASC";
            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String school_id= rs.getString("id");
                String school_name = rs.getString("name");
                String school_dis = rs.getString("description");
                String school_rating = rs.getString("rating");

                String location_id = rs.getString("location_id");
                String city = rs.getString("city");
                String state = rs.getString("state_full");
                String safety_level = rs.getString("safety");
                String LivingCostIndex = rs.getString("LivingCostIndex");
                String zipcode = rs.getString("location_id");

                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("school_id", school_id);
                jsonObject.addProperty("school_name", school_name);
                jsonObject.addProperty("school_dis", school_dis);
                jsonObject.addProperty("school_rating", school_rating);
                jsonObject.addProperty("LivingCostIndex", LivingCostIndex);

                jsonObject.addProperty("zipcode", zipcode);
                jsonObject.addProperty("location_id", location_id);
                jsonObject.addProperty("city", city);
                jsonObject.addProperty("state", state);
                jsonObject.addProperty("safety_level", safety_level);

                jsonArray.add(jsonObject);
            }
            rs.close();
            statement.close();

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}
