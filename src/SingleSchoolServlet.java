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
@WebServlet(name = "SingleSchoolServlet", urlPatterns = "/api/single-school")
public class SingleSchoolServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

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

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (out; Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            // Construct a query with parameter represented by "?"
            String query = "SELECT * from school as s, schools_in_locations as sil, genre as g, genres_in_schools as gis, location as l where g.id = gis.genre_id and gis.school_id = s.id and l.location_id = sil.location_id and sil.id = s.id and s.id = ?";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                String school_id= rs.getString("id");
                String school_name = rs.getString("name");
                String school_dis = rs.getString("description");
                String link_to_website= rs.getString("link_to_website");
                String link_to_image= rs.getString("link_to_image");
                String school_rating= rs.getString("rating");


                String address = rs.getString("address");
                String net_cost = rs.getString("net_cost");
                String upper_SAT= rs.getString("upper_SAT");
                String lower_SAT = rs.getString("lower_SAT");

                String location_id = rs.getString("location_id");
                String city = rs.getString("city");
                String state = rs.getString("state_full");
                String safety_level = rs.getString("safety");
                String genre = rs.getString("fullname");


                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("school_id", school_id);
                jsonObject.addProperty("school_name", school_name);
                jsonObject.addProperty("school_dis", school_dis);
                jsonObject.addProperty("location_id", location_id);
                jsonObject.addProperty("link_to_website", link_to_website);
                jsonObject.addProperty("link_to_image", link_to_image);
                jsonObject.addProperty("genre", genre);

                jsonObject.addProperty("address", address);
                jsonObject.addProperty("net_cost", net_cost);
                jsonObject.addProperty("upper_SAT", upper_SAT);
                jsonObject.addProperty("lower_SAT", lower_SAT);
                jsonObject.addProperty("rating", school_rating);


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
