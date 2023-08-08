import java.util.ArrayList;
import java.util.List;

public class temp_location {
    private final String location_id;
    private  List<temp_school> schools = new ArrayList<>();

    public temp_location(String location_id, List<temp_school> schools ) {
        this.location_id = location_id;
        this.schools = schools;
    }

    public String getLocation_id() {
        return location_id;
    }
    public List<temp_school> getSchools() {
        return schools;
    }

    public String toString() {

        return "location_id:" + getLocation_id() + ", "+
                "schools:" + getSchools() + "." ;
    }
}
