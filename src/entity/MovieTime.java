package entity;

public enum MovieTime {
    Time1("조조"), Time2("미들"), Time3("심야");

    private String time;
    MovieTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public static MovieTime getMovieTime(String name) {
        if (name.equals("조조")) {
            return MovieTime.Time1;
        } else if (name.equals("미들")) {
            return MovieTime.Time2;
        } else if (name.equals("심야")) {
            return MovieTime.Time3;
        } else {
            return null;
        }
    }
}
