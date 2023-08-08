import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "TomcatPoolingServlet", urlPatterns = "/")
public class TomcatPoolingServlet extends HttpServlet {
    // Create a dataSource which registered in web.xml
    private DataSource dataSource;
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use HTTP GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // the following line is to get a connection from a data source configured as a connection pool
        try (out; Connection conn = dataSource.getConnection()) {


            // the following commented lines are direct connections without pooling, which is the old way
            // Class.forName("org.gjt.mm.mysql.Driver");
            // Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {


            out.println("<HTML><HEAD><TITLE>MovieDBExample</TITLE></HEAD>");
            out.println("<BODY><H1>MovieDBExample (with some changes)</H1>");


            if (conn == null) {
                out.println("conn is null.");
            } else {
                // Declare our statement
                Statement statement = conn.createStatement();
                String query = "SELECT * from school limit 10";

                // Perform the query
                ResultSet rs = statement.executeQuery(query);

                out.println("<TABLE border>");

                // Iterate through each row of rs
                while (rs.next()) {
                    String m_id = rs.getString("id");
                    String m_LN = rs.getString("name");
                    String m_dob = rs.getString("description");
                    out.println("<tr>" + "<td>" + m_id + "</td>" + "<td>" + m_LN + "</td>" + "<td>" + m_dob + "</td>"
                            + "</tr>");
                }

                out.println("</TABLE>");

                rs.close();
                statement.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }

}
