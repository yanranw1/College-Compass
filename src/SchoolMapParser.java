import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolMapParser {

    List<temp_location> locations = new ArrayList<>();
    Document dom;
    String query_string = "INSERT IGNORE schools_in_locations(id, location_id) VALUES ";
    Integer fileParsed = 0;

    public void runExample() {
        // parse the xml file and get the dom object
        parseXmlFile();

        // get each employee element and create a Employee object
        parseDocument();

        System.out.println("files parsed: " + fileParsed);
        query_string = query_string.substring(0, query_string.length() - 1);
        query_string += ";";
        System.out.println(query_string);
        insertLocationIntoDatabase(query_string);
    }

    private void parseXmlFile() {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false); // Disable validation
        documentBuilderFactory.setExpandEntityReferences(false); // Disable entity expansion
        try {
            // using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom = documentBuilder.parse("location_school.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseDocument() {
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        // get a nodelist of employee Elements, parse each into Employee object
        NodeList nodeList = documentElement.getElementsByTagName("location");
        for (int i = 0; i < nodeList.getLength(); i++) {

            // get the employee element
            Element element = (Element) nodeList.item(i);

            // get the Employee object
            temp_location location = parseLocation(element);
            fileParsed++;
            // add it to list
            locations.add(location);
            updateQuery(location);
        }
    }

    private void updateQuery(temp_location location) {
        List<temp_school> temp_schools = location.getSchools();
        for (temp_school school : temp_schools) {
            String sql = String.format("('%s', '%s'),", school.get_school_id(), location.getLocation_id());
            query_string += sql;
        }
    }

    private void insertLocationIntoDatabase(String query) {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/collegedb";

        try {
            // load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                PreparedStatement statement = connection.prepareStatement(query_string);

                int rowsAffected = statement.executeUpdate();
                System.out.println("Duplicate rows: " + (fileParsed - rowsAffected));

                // close the statement and connection
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * It takes an employee Element, reads the values in, creates
     * an Employee object for return
     */
    private temp_location parseLocation(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name

        String location_id = getTextValue(element, "location_id");
        NodeList schoolNodes = element.getElementsByTagName("school");

        List<temp_school> schools = new ArrayList<>();

        // parse each Hobby element and add it to the list
        for (int i = 0; i < schoolNodes.getLength(); i++) {
            Element schoolElement = (Element) schoolNodes.item(i);
            temp_school new_school = parseSchool(schoolElement);
            schools.add(new_school);
        }


        // create a new Employee with the value read from the xml nodes
        return new temp_location(location_id,schools);
    }

    private temp_school parseSchool(Element element) {

        String school_id = getTextValue(element, "school_id");
        String school_name = getTextValue(element, "school_name");

        // create a new Employee with the value read from the xml nodes
        return new temp_school(school_id,  school_name );
    }

    /**
     * It takes an XML element and the tag name, look for the tag and get
     * the text content
     * i.e for <Employee><Name>John</Name></Employee> xml snippet if
     * the Element points to employee node and tagName is name it will return John
     */
    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            // here we expect only one <Name> would present in the <Employee>
            textVal = nodeList.item(0).getFirstChild().getNodeValue();
        }
        return textVal;
    }

    private float getFloatValue(Element ele, String tagName) {
        // in production application you would catch the exception
        return Float.parseFloat(getTextValue(ele, tagName));
    }

    /**
     * Calls getTextValue and returns a int value
     */
    private int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    public static void main(String[] args) {
        // create an instance
        SchoolMapParser domParserExample = new SchoolMapParser();
        // call run example
        domParserExample.runExample();
    }

}
