import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchoolParser {

    List<school> schools = new ArrayList<>();
    Document dom;
    Integer fileParsed = 0;

    HashMap<String, Integer> map = check_map();
    String query_school = "INSERT IGNORE school(id,name,rating,numVotes,net_cost,description,upper_SAT,lower_SAT,link_to_website,telephone,address,link_to_image) VALUES ";
    String query_genre = "INSERT IGNORE genre(fullname) values ";
    String query_genres_in_schools = "INSERT IGNORE genres_in_schools (genre_id, school_id) values ";
    String query_celebrity = "INSERT IGNORE celebrity(name,net_worth,industry) values ";
    String query_celebrities_in_schools = "INSERT IGNORE celebrities_in_schools(celebrity_id,school_id) VALUES ";
    int celebrity_counter = 1;
    int genre_id_counter = check_id();

    public void runExample() {

        parseXmlFile();

        parseDocument();

        query_genre = query_genre.substring(0, query_genre.length() - 1);
        query_school = query_school.substring(0, query_school.length() - 1);
        query_genres_in_schools = query_genres_in_schools.substring(0, query_genres_in_schools.length() - 1);
        query_celebrity = query_celebrity.substring(0, query_celebrity.length() - 1);
        query_celebrities_in_schools = query_celebrities_in_schools.substring(0, query_celebrities_in_schools.length() - 1);

        query_school += ";";
        query_genre += ";";
        query_genres_in_schools += ";";
        query_celebrity += ";";
        query_celebrities_in_schools += ";";

        System.out.println("files parsed: " + fileParsed);
        insertSchoolIntoDatabase(query_school);
        if(!query_genre.equals("INSERT IGNORE genre(fullname) values;"))
            insertSchoolIntoDatabase(query_genre);
        insertSchoolIntoDatabase(query_genres_in_schools);
        insertSchoolIntoDatabase(query_celebrity);
        insertSchoolIntoDatabase(query_celebrities_in_schools);
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
            dom = documentBuilder.parse("schools.xml");
        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }


    private void parseDocument() {
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        NodeList nodeList = documentElement.getElementsByTagName("school");
        for (int i = 0; i < nodeList.getLength(); i++) {

            // get the employee element
            Element element = (Element) nodeList.item(i);

            // get the Employee object
            school school = parseSchool(element);
            fileParsed++;
            updateQuery(school);
            // add it to list
            schools.add(school);
        }
    }

    private void updateQuery(school school) {
        query_school += " ('" + school.get_school_id() + "', '" + school.get_school_name() +"', " + school.get_rating() + ", "  + school.get_numVotes() + ", "+ school.get_net_cost() +", '"+
                school.get_description() + "', " + school.get_upper_SAT() +", " + school.get_lower_SAT() +", '" +school.get_link_to_website() + "', '" + school.get_telephone() + "', '" +
                school.get_address()+ "', '" + school.get_link_to_image()+"'),";
        if (map.containsKey(school.get_genre())) {
            query_genres_in_schools += String.format(" (%s, '%s'),",map.get(school.get_genre()),school.get_school_id() );
        } else {
            map.put(school.get_genre(), genre_id_counter);
            query_genre += String.format("('%s'),", school.get_genre());
            query_genres_in_schools += String.format(" (%s, '%s'),",Integer.toString(genre_id_counter),school.get_school_id() );
            genre_id_counter++;
        }

        List<celebrity> celebrities = school.get_celebrities();
        for(celebrity celebrity:celebrities){
            query_celebrity += String.format(" ('%s',%s,'%s'),",celebrity.get_name(), celebrity.get_net_worth(),celebrity.get_industry() );
            query_celebrities_in_schools += String.format(" (%s, '%s'),", Integer.toString(celebrity_counter), school.get_school_id() );;
            celebrity_counter ++;
        };
    }

    private int check_id() {
        int res = 0;
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/collegedb";

        try {
            // load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                connection.setAutoCommit(false);
                String query = "Select max(id) from genre;";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()){
                    res = rs.getInt(1);
                }
                res+=1;

                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
    private HashMap<String, Integer> check_map() {
        HashMap<String, Integer> res = new HashMap<String, Integer>();
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/collegedb";

        try {
            // load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                connection.setAutoCommit(false);
                String query = "Select * from genre;";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()){
                    res.put(rs.getString("fullname"),rs.getInt("id")) ;
                }

                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void insertSchoolIntoDatabase(String query_string) {
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

    private school parseSchool(Element element) {
        String school_id = getTextValue(element, "school_id");
        String school_name = getTextValue(element, "school_name");
        float rating = getFloatValue(element, "rating");
        int numVotes = getIntValue(element, "numVotes");
        int net_cost = getIntValue(element, "net_cost");

        String description = getTextValue(element, "description");
        int upper_SAT = getIntValue(element, "upper_SAT");
        int lower_SAT = getIntValue(element, "lower_SAT");
        String link_to_website = getTextValue(element, "link_to_website");

        String telephone = getTextValue(element, "telephone");
        String address = getTextValue(element, "address");
        String link_to_image = getTextValue(element, "link_to_image");
        String type = getTextValue(element, "type");
        String genre = getTextValue(element, "genre");


        NodeList celebritiesNodes = element.getElementsByTagName("celebrity");

        List<celebrity> celebrities = new ArrayList<>();

        // parse each Hobby element and add it to the list
        for (int i = 0; i < celebritiesNodes.getLength(); i++) {
            Element schoolElement = (Element) celebritiesNodes.item(i);
            celebrity new_celebrity = parseCelebrity(schoolElement);
            celebrities.add(new_celebrity);
        }

        // create a new Employee with the value read from the xml nodes
        return new school(school_id,  school_name , rating,  numVotes,
                net_cost,  description,  upper_SAT,  lower_SAT,
                link_to_website,  telephone,  address,  link_to_image,  type, celebrities,genre);
    }


    private celebrity parseCelebrity(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name

        int id = getIntValue(element, "id");

        String name = getTextValue(element, "name");
        int net_worth = getIntValue(element, "net_worth");
        String industry = getTextValue(element, "industry");
        String type = element.getAttribute("type");

        return new celebrity(id, name,industry, net_worth,type);
    }


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


    private int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele, tagName));
    }


    public static void main(String[] args) {
        // create an instance
        SchoolParser domParserExample = new SchoolParser();
        // call run example
        domParserExample.runExample();
    }

}
