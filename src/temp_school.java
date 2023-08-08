import java.util.ArrayList;
import java.util.List;

public class temp_school {
    private final String school_id;
    private final String school_name;
//    private final float rating;

    public temp_school(String school_id, String school_name ) {
        this.school_id = school_id;
        this.school_name = school_name;
//        this.rating = rating;
    }

    public String get_school_id() {
        return school_id;
    }
    public String get_school_name() {
        return school_name;
    }
    //    public float get_rating() {
//        return rating;
//    }
    public String toString() {

        return "school_id:" + get_school_id() + ", " +
                "school_name:" + get_school_name() + ".";
    }
}
