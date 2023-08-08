import java.util.ArrayList;
import java.util.List;

public class school {
    private final String school_id;
    private final String school_name;
    private final float rating;
    private final int numVotes;
    private final int net_cost;
    private final String description;
    private final int upper_SAT;
    private final int lower_SAT;
    private final String link_to_website;
    private final String telephone;
    private final String address;
    private final String link_to_image;
    private final String type;
    private  List<celebrity> celebrities = new ArrayList<>();
    private final String genre;

    public school(String school_id, String school_name ,float rating, int numVotes,
                  int net_cost, String description, int upper_SAT, int lower_SAT,
                  String link_to_website, String telephone,  String address,
                  String link_to_image, String type, List<celebrity> celebrities, String genre) {
        this.school_id = school_id;
        this.school_name = school_name;
        this.rating = rating;
        this.numVotes = numVotes;
        this.net_cost = net_cost;
        this.description = description;
        this.upper_SAT = upper_SAT;
        this.lower_SAT = lower_SAT;
        this.link_to_website = link_to_website;
        this.telephone = telephone;
        this.address = address;
        this.link_to_image = link_to_image;
        this.type = type;
        this.celebrities = celebrities;
        this.genre = genre;
    }

    public String get_school_id() {
        return school_id;
    }
    public String get_school_name() {
        return school_name;
    }
    public float get_rating() {
        return rating;
    }
    public int get_net_cost() {return net_cost;}
    public int get_numVotes() {return numVotes;}

    public String get_description() {
        return description;
    }
    public int get_upper_SAT() {return upper_SAT;}
    public int get_lower_SAT() {return lower_SAT;}
    public String get_link_to_website() {
        return link_to_website;
    }
    public String get_telephone() {
        return telephone;
    }
    public String get_address() {
        return address;
    }
    public String get_link_to_image() {
        return link_to_image;
    }
    public String get_type() {
        return type;
    }
    public List<celebrity> get_celebrities() {
        return celebrities;
    }
    public String get_genre() {
        return genre;
    }
    public String toString() {

        return "school_id:" + get_school_id() + ", " +
                "school_name:" + get_school_name() + ", " +
                "rating:" + get_rating() + ", " +
                "numVotes:" + get_numVotes() + ","+
                "net_cost:" + get_net_cost() + ","+
                "description:" + get_description() + ", " +
                "upper_SAT:" + get_upper_SAT() + ", " +
                "lower_SAT:" + get_lower_SAT() + ", " +
                "link_to_website:" + get_link_to_website() + ","+
                "telephone:" + get_telephone() + ","+
                "address:" + get_address() + ", " +
                "link_to_image:" + get_link_to_image() + ","+
                "celebrities:" + get_celebrities() + ","+
                "gerne: "+get_genre()+","+
                "type:" + get_type() + ".";
    }
}
