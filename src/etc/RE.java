package etc;

public enum RE {
    MOVIE_NAME("^[A-Za-z0-9가-힣!@#$%^&*()\\-_=+[]{};:'\",.<>/?]{1,15}$"),
    MOVIE_INFO("^[A-Za-z0-9가-힣!@#%^&*()\\-=+[]{};:'\",.<>/?]{10,}$"),
    MOVIE_TIME("^(조조|미들|심야)$"),
    ROOM_NUMBER("^(0[0-9]|1[0-9]|2[0-3])$"),
    SEAT_NUMBER("^[A-Z][0-9]{2}$"),
    SEAT_CHART("^(([A-Z]:(-1|0|1))+|)([A-Z]:(-1|0|1))+$"),
    MOVIE_ORDER("^[0-9]+$"),
    TICKET_NUMBER("^[A-Z][0-9]{6}$"),
    TICKET_PASSWORD("^[0-9]{4}$"),
    ADMIN_PASSWORD("^(?:[A-Za-z][0-9]+[A-Za-z]|[0-9][A-Za-z]+[0-9]*|[A-Za-z0-9]{6,15})$");

    private String value;
    RE(String value) {this.value = value;}
}
