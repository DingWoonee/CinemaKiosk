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
}
