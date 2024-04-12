package etc;

public enum RE {

    MOVIE_NAME("^[A-Za-z0-9가-힣!@#$%^&*()\\-_=+[]{};:\'\",.<>/?]{1,15}$"),
    MOVIE_INFO("^[A-Za-z0-9가-힣!@#%^&*()\\-=+[]{};:\'\",.<>/?]{10,}$"),
    MOVIE_TIME("^(조조|미들|심야)$"),
    ROOM_NUMBER("^(0[1-9]|[1-9][0-9])$"),
    ROOM_NUMBERS("^(0[1-9]|[1-9][0-9])(\\|0[1-9]|[1-9][0-9])*$"),
    SEAT_NUMBER("^[A-Z](0[1-9]|[1-9][0-9])$"),
    SEAT_CHART("^([A-Z](:(-1|0|1))+)(\\|[A-Z](:(-1|0|1))+)*$"),
    MOVIE_ORDER("^[0-9]+$"),
    TICKET_NUMBER("^[A-Z][0-9]{6}$"),
    TICKET_PASSWORD("^[0-9]{4}$"),
    ADMIN_PASSWORD("^[A-Za-z0-9]{6,15}$"),
    DATE_EIGHT("^(202[4-9]|20[3-9][0-9])(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$");


    private String value;
    RE(String value) {this.value = value;}

    public String getValue() {
        return value;
    }
}
