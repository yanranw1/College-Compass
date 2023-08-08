import java.util.ArrayList;
import java.util.List;

public class location {
    private final String location_id;
    private final String state_init;
    private final String zipcode;
    private final String state_full;
    private final String city;
    private final String type;

    private final float lci;
    private final int safety;
//    private  List<school> schools = new ArrayList<>();

    public location(String location_id,String state_init,String zipcode,String state_full,String city, float lci, int safety, String type) {
        this.location_id = location_id;
        this.state_init = state_init;
        this.zipcode = zipcode;
        this.state_full = state_full;
        this.city = city;
        this.lci = lci;
        this.safety = safety;
        this.type = type;
//        this.schools = schools;
    }

    public String getLocation_id() {
        return location_id;
    }
    public String getState_init() {
        return state_init;
    }
    public String getZipcode() {
        return zipcode;
    }
    public String getState_full() {return state_full;}
    public String getCity() {return city;}
    public float getLci() {return lci;}
    public int getSafety() {return safety;}

    public String getType() {
        return type;
    }

//    public List<school> getSchools() {
//        return schools;
//    }

    public String toString() {

        return "location_id:" + getLocation_id() + ", " +
                "state_init:" + getState_init() + ", " +
                "zipcode:" + getZipcode() + ", " +
                "state_full:" + getState_full() + ", " +
                "city:" + getCity() + ", " +
                "lci:" + getLci() + ", " +
                "safety:" + getSafety() + ", " +
                "type:" + getType() + ". ";
//                "schools:" + getSchools() + ".";
    }
}
