import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
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
import java.sql.ResultSet;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet("/school-suggestion")
public class SchoolSuggestion extends HttpServlet {

	private DataSource dataSource;

	public void init(ServletConfig config) {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("##suggestion");
		PrintWriter out = response.getWriter();

		try(out; Connection conn = dataSource.getConnection()){
			System.out.print("##inside");

			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();

			// get the query string from parameter
			String school = request.getParameter("query");

			// return the empty json array if query is null or empty
			if (school == null ||school.length() < 3|| school.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}

			String query = "SELECT id,name \n" +
					"FROM school AS s\n" ;
			String[] keywords = school.split(" ");
			query += "WHERE MATCH (name) AGAINST ('";
			int counter = 0;
			for (String s : keywords){
				if(!s.equals("-")) {
					System.out.println("**" + s + "**");
					query += "+" + s + "*";
					counter++;
					if (counter != keywords.length) {
						query += " ";
					}
				}

			}
			query +="' IN BOOLEAN MODE)";
			query += "LIMIT 10;\n";

			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			JsonObject responseJsonObject = new JsonObject();

			System.out.println("%%2");
			int id_counter = 0;
			while (rs.next()) {
				int id = id_counter;
				id_counter ++;
				String name = rs.getString("name");

				jsonArray.add(generateJsonObject(id, name));
				System.out.println("page exist");
			}
			System.out.println("%%3");

			rs.close();
			statement.close();

			response.getWriter().write(jsonArray.toString());
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}




}
