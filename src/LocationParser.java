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

public class LocationParser {

    List<location> locations = new ArrayList<>();
    Document dom;
    Integer fileParsed = 0;
    String query_string = "INSERT IGNORE location(location_id, city, state_init, state_full, zipcode, LivingCostIndex, safety) VALUES ";

    public void runExample() {

        // parse the xml file and get the dom object
        parseXmlFile();

        // get each employee element and create a Employee object
        parseDocument();

        // iterate through the list and print the data
//        printData();

        System.out.println("files parsed: " + fileParsed);
        query_string = query_string.substring(0, query_string.length()-1);
        query_string += ";";
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
            dom = documentBuilder.parse("locations.xml");

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
            fileParsed++;
            // get the Employee object
            location location = parseLocation(element);
            updateQuery(location);
            // add it to list
            locations.add(location);
        }
    }

    private void updateQuery(location location) {
        query_string += "('" + location.getLocation_id() + "', '" + location.getCity() +"', '" + location.getState_init() + "', '"  + location.getState_full() + "', '"+ location.getZipcode() +"', "+
                location.getLci() + ", " + location.getSafety() + "),";
    }

    private void insertLocationIntoDatabase(String query_string) {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/collegedb";

        try {
            // load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // create a connection to the database
            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                System.out.println(query_string);
                PreparedStatement statement = connection.prepareStatement(query_string);
                // execute the SQL statement
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
    private location parseLocation(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name

        String location_id = getTextValue(element, "location_id");
        String state_init = getTextValue(element, "state_init");
        String zipcode = getTextValue(element, "zipcode");
        String state_full = getTextValue(element, "state_full");
        String city = getTextValue(element, "city");
        int lci = getIntValue(element, "lci");
        int safety = getIntValue(element, "safety");
        String type = element.getAttribute("type");
//        NodeList schoolNodes = element.getElementsByTagName("school");

//        List<school> schools = new ArrayList<>();

        // parse each Hobby element and add it to the list
//        for (int i = 0; i < schoolNodes.getLength(); i++) {
//            Element schoolElement = (Element) schoolNodes.item(i);
//            school new_school = parseSchool(schoolElement);
//            schools.add(new_school);
//        }
//        // create a new Employee with the value read from the xml nodes
        return new location(location_id, state_init, zipcode, state_full, city,
                lci, safety, type);
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
//        System.out.println(getTextValue(ele, tagName));
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {

        System.out.println("Total parsed " + locations.size() + " locations");
        for (location loc : locations) {
            System.out.println("\t" + loc.toString());
        }
    }

    public static void main(String[] args) {
        // create an instance
        LocationParser domParserExample = new LocationParser();
        // call run example
        domParserExample.runExample();
    }

}
