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
import java.util.Random;

@WebServlet(name = "AddLocation", urlPatterns = "/api/add-location")
public class AddLocation extends HttpServlet {
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
//    getSaltString function Citation: https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
    protected String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 22) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type
        System.out.println("0");

        PrintWriter out = response.getWriter();

        String city = request.getParameter("city");
        String state_init = request.getParameter("state_init");
        String state_full = request.getParameter("state_full");
        String zipcode = request.getParameter("zipcode");
        String LivingCostIndex = request.getParameter("LivingCostIndex");
        String safety = request.getParameter("safety");

        System.out.println("1");

        String error_message = "";
        float LCIValue = 0;
        if(LivingCostIndex.length()>0){
            try {
                LCIValue = Float.parseFloat(LivingCostIndex);
            } catch (NumberFormatException e) {
                error_message += "Invalid LivingCostIndex value.\n";
                System.err.println("Error: Invalid LivingCostIndex value. " + e.getMessage());
            }
        }
        else
            LCIValue = -1;

        int safetyValue = 0;
        if(safety.length()>0){
            try {
                safetyValue = Integer.parseInt(safety);
            } catch (NumberFormatException e) {
                error_message += "Invalid safety value.\n";
                System.err.println("Error: Invalid safety value. " + e.getMessage());
            }
        }
        else
            safetyValue = -1;

        System.out.println("2");
        if(city.length()<1 || state_init.length()<1 || state_full.length()<1 || zipcode.length()<1){
            error_message += "Please complete all required part.\n";
            System.out.println(city);
            System.out.println(city.length());
            System.out.println(state_init);
            System.out.println(state_init.length());
            System.out.println(state_full);
            System.out.println(state_full.length());
            System.out.println(zipcode);
            System.out.println(zipcode.length());
        }
        if(state_init.length()>2){
            error_message += "State initial should be less then 2 chars.\n";
        }
        if(zipcode.length()>5){
            error_message += "Zipcode should be less then 5 chars.\n";
        }

        JsonObject responseJsonObject = new JsonObject();
        if (error_message.length() > 0){
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", error_message);
            response.getWriter().write(responseJsonObject.toString());
            System.out.println("2.3");
        } else {
            boolean exist = false;
            try (out; Connection conn = dataSource.getConnection()) {
                System.out.println("3");
                String check_query = String.format("SELECT COUNT(1) FROM location WHERE state_init= '%s' and zipcode= '%s'",
                        state_init, zipcode);
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
                    error_message += "This location already existed";
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", error_message);
                    System.out.println(error_message);
                } else {
                    String query = "CALL add_location(?,?,?,?,?,?)";
                    String _query = String.format("CALL add_location('%s','%s','%s','%s',%s,%s)",city,state_init,state_full,zipcode,LivingCostIndex,safety);

                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, city);
                    statement.setString(2, state_init);
                    statement.setString(3, state_full);
                    statement.setString(4, zipcode);
                    statement.setFloat(5, LCIValue);
                    statement.setInt(6, safetyValue);

                    System.out.println(_query);
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