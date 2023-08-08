import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This CartServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/cart.
 */
@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/cart")
public class ShoppingCartServlet extends HttpServlet {

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Got a request in shopping cart servlet");
        HttpSession session = request.getSession();

        JsonObject responseJsonObject = new JsonObject();

        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<String>();
        }
        // Log to localhost log
        request.getServletContext().log("getting " + previousItems.size() + " items");
        JsonArray previousItemsJsonArray = new JsonArray();
        previousItems.forEach(previousItemsJsonArray::add);
        responseJsonObject.add("previousItems", previousItemsJsonArray);

        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // receives a http POST request: get parameter "item" from shopping-cart.html "input" field
        String remove = request.getParameter("remove");
//        String school_name = request.getParameter("name");
//        String genre = request.getParameter("genre");
//        String state = request.getParameter("state");
//        String school_id = request.getParameter("id");
//        String location_id = request.getParameter("location_id");
//        String SAT = request.getParameter("SAT");
//        String cost = request.getParameter("cost");


        String school_id= request.getParameter("school_id");
        String school_name = request.getParameter("school_name");
        String school_dis = request.getParameter("description");
        String link_to_website= request.getParameter("link_to_website");
        String link_to_image= request.getParameter("link_to_image");
        String school_rating= request.getParameter("rating");


        String address = request.getParameter("address");
        String net_cost = request.getParameter("net_cost");
        String upper_SAT= request.getParameter("upper_SAT");
        String lower_SAT = request.getParameter("lower_SAT");

        String location_id = request.getParameter("location_id");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String safety_level = request.getParameter("safety_level");
        String genre = request.getParameter("genre");
        String preference = request.getParameter("preference");


        System.out.println(school_name);
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        //preference: stores college name and student's preference scores


        JsonObject schoolObject = new JsonObject();

        schoolObject.addProperty("school_id", school_id);
        schoolObject.addProperty("school_name", school_name);
        schoolObject.addProperty("school_dis", school_dis);
        schoolObject.addProperty("location_id", location_id);
        schoolObject.addProperty("link_to_website", link_to_website);
        schoolObject.addProperty("link_to_image", link_to_image);
        schoolObject.addProperty("genre", genre);

        schoolObject.addProperty("address", address);
        schoolObject.addProperty("net_cost", net_cost);
        schoolObject.addProperty("upper_SAT", upper_SAT);
        schoolObject.addProperty("lower_SAT", lower_SAT);
        schoolObject.addProperty("rating", school_rating);

        schoolObject.addProperty("city", city);
        schoolObject.addProperty("state", state);
        schoolObject.addProperty("safety_level", safety_level);
        schoolObject.addProperty("preference", preference);


        if (previousItems == null) {
            previousItems = new ArrayList<String>();

            previousItems.add(schoolObject.toString());
            // storing the item for this session using a key-pair value
            session.setAttribute("previousItems", previousItems);
        }
        else if (remove != null) {
            for (String item: previousItems) {
                System.out.println(item);
                System.out.println(school_name);
                if (item.contains(school_name)) {
                    System.out.println("removing: " + item);
                    previousItems.remove(item);
                }
            }
        }
        else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {
                previousItems.add(schoolObject.toString());
            }
        }

        // Create a new JSON object and sends back response to browser
        JsonObject responseJsonObject = new JsonObject();

        JsonArray previousItemsJsonArray = new JsonArray();
        previousItems.forEach(previousItemsJsonArray::add);
        // a previousItems of array
        responseJsonObject.add("previousItems", previousItemsJsonArray);

        response.getWriter().write(responseJsonObject.toString());
    }
}
