public class celebrity {
    private final String name;
    private final String industry;
    private final String type;

    private final int net_worth;
    private final int id;

//    private  List<school> schools = new ArrayList<>();

    public celebrity(int id,String name, String industry , int net_worth, String type) {
        this.name = name;
        this.industry = industry;
        this.net_worth = net_worth;
        this.type = type;
        this.id = id;
//        this.schools = schools;
    }

    public String get_name() {
        return name;
    }
    public String get_industry() {
        return industry;
    }
    public int get_net_worth() {
        return net_worth;
    }
    public int get_id() {
        return id;
    }
    public String get_type() {
        return type;
    }



    public String toString() {

        return
                "id:" + get_id() + ", " +
                        "name:" + get_name() + ", " +
                        "industry:" + get_industry() + ", " +
                        "net_worth:" + get_net_worth() + ", " +

                        "type:" + get_type() + "." ;
    }
}
