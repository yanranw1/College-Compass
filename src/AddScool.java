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
import java.util.Random;

@WebServlet(name = "AddScool", urlPatterns = "/api/add-school")
public class AddScool extends HttpServlet {
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

        String name = request.getParameter("name");
        String rating = request.getParameter("rating");
        String numVotes = request.getParameter("numVotes");
        String net_cost = request.getParameter("net_cost");
        String description = request.getParameter("description");
        String upper_SAT = request.getParameter("upper_SAT");
        String lower_SAT = request.getParameter("lower_SAT");
        String link_to_website = request.getParameter("link_to_website");
        String telephone = request.getParameter("telephone");
        String address = request.getParameter("address");
        String genre_name = request.getParameter("genre_name");
        String zipcode = request.getParameter("zipcode");
        String state_init = request.getParameter("state_init");
        String city = request.getParameter("city");
        String link_to_image = "https://d13b2ieg84qqce.cloudfront.net/15ca370adc4d5c4141f7930d58b222cec42bd554";

        System.out.println("1");

        String error_message = "";
        float ratingValue = -1;
        try {
            ratingValue = Float.parseFloat(rating);
        } catch (NumberFormatException e) {
            error_message += "Invalid rating value.\n";
            System.err.println("Error: Invalid rating value.\n" + e.getMessage());
        }
        if(ratingValue != -1){
            if(ratingValue <0){
                error_message += "Rating must be greater than 0.\n";
            }
        }

        int numVotesValue = -1;
        try {
            numVotesValue = Integer.parseInt(numVotes);
        } catch (NumberFormatException e) {
            error_message += "Invalid numVotes value.\n";
            System.err.println("Error: Invalid numVotes value. " + e.getMessage());
        }
        if(numVotesValue != -1){
            if(numVotesValue <0){
                error_message += "numVotes value must be greater than 0.\n";
            }
        }

        int net_costValue = -1;
        try {
            net_costValue = Integer.parseInt(net_cost);
        } catch (NumberFormatException e) {
            error_message += net_cost + "Invalid net_cost value.\n";
            System.err.println("Error: Invalid net_cost value. " + e.getMessage());
        }
        if(net_costValue != -1){
            if(net_costValue <0){
                error_message += "net_cost value must be greater than 0.\n";
            }
        }

        int upper_SATValue = -1;
        try {
            upper_SATValue = Integer.parseInt(upper_SAT);
        } catch (NumberFormatException e) {
            error_message += "Invalid upper_SAT value.\n";
            System.err.println("Error: Invalid upper_SAT value. " + e.getMessage());
        }
        if(upper_SATValue != -1){
            if(upper_SATValue <400){
                error_message += "Upper_SAT value must be greater than 400.\n";
            }
            if(upper_SATValue >1600){
                error_message += "Upper_SAT value must be less than 1600.\n";
            }
        }

        int lower_SATValue = -1;
        try {
            lower_SATValue = Integer.parseInt(lower_SAT);
        } catch (NumberFormatException e) {
            error_message += "Invalid lower_SATValue value.\n";
            System.err.println("Error: Invalid lower_SATValue value. " + e.getMessage());
        }
        if(lower_SATValue != -1){
            if(lower_SATValue <400){
                error_message += "Lower_SAT value must be greater than 400.\n";
            }
            if(lower_SATValue >1600){
                error_message += "Lower_SAT value must be less than 1600.\n";
            }
            if(lower_SATValue >upper_SATValue){
                error_message += "Upper_SAT value must be greater than lower_SAT value.\n";
            }
        }
        System.out.println("2");
        if(name.length()<1 || description.length()<1 || link_to_website.length()<1 || telephone.length()<1 || address.length()<1||genre_name.length()<1 || zipcode.length()<1 || state_init.length()<1 || city.length()<1 )
        {
            error_message = "Please complete all required part.\n"+error_message;
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

                String check_query = String.format("SELECT COUNT(1) FROM school WHERE name = '%s' and rating= %s and numVotes= %s and net_cost= %s and description= '%s' and upper_SAT= %s and lower_SAT= %s and link_to_website= '%s' and telephone= '%s' and address= '%s'", name, rating, numVotes, net_cost, description, upper_SAT, lower_SAT, link_to_website, telephone, address);
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
                    error_message += "This school already existed";
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", error_message);
                    System.out.println(error_message);
                } else {
                    String query = "CALL add_school(?,?,?, ?,?,?, ?,?,? ,?,?,? ,?,?,?)";
//                    String _query = String.format("CALL add_location('%s','%s','%s','%s',%s,%s)",city,state_init,state_full,zipcode,LivingCostIndex,safety);

//                    String query = "INSERT INTO school(id,name,rating,numVotes,net_cost,description,upper_SAT,lower_SAT,link_to_website,telephone,address,link_to_image) VALUES " +
//                            "(?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, name);
                    statement.setFloat(2, ratingValue);
                    statement.setInt(3, numVotesValue);
                    statement.setInt(4, net_costValue);
                    statement.setString(5, description);
                    statement.setInt(6, upper_SATValue);
                    statement.setInt(7, lower_SATValue);
                    statement.setString(8, link_to_website);
                    statement.setString(9, telephone);
                    statement.setString(10, address);
                    statement.setString(11, link_to_image);
                    statement.setString(12, genre_name);
                    statement.setString(13, zipcode);
                    statement.setString(14, state_init);
                    statement.setString(15, city);

                    System.out.println("4");

                    int rowsAffected = statement.executeUpdate();
                    System.out.println("5");

                    if (rowsAffected == 1) {
                        System.out.println("rowsAffected");
                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success");
                    } else {
                        System.out.println("error happened");
                        error_message += "unable to add this school to database";
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