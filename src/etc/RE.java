package etc;

public enum RE {

    MOVIE_NAME("^[A-Za-z0-9가-힣ㅏ-ㅣㄱ-ㅎ\\s!@#%^&*()\\-_=+\\[\\]{};:'\",.<>/?]{1,15}$"),
    MOVIE_INFO("^[A-Za-z0-9가-힣ㅏ-ㅣㄱ-ㅎ\\s!@#%^&*()\\-_=+\\[\\]{};:'\",.<>/?]{10,}$"),
    MOVIE_TIME("^(조조|미들|심야)$"),
    MOVIE_CHOICE_MENU_NUMBER("^[1-2]$"),
    ROOM_NUMBER("^(0[1-9]|[1-9][0-9])$"),
    ROOM_NUMBERS("^(0[1-9]|[1-9][0-9])(\\|0[1-9]|[1-9][0-9])*$"),
    SEAT_NUMBER("^[A-Z](0[1-9]|[1-9][0-9])$"),
    SEAT_CHART("^([A-Z](:(-1|0|1))+)(\\|[A-Z](:(-1|0|1))+)*$"),
    MOVIE_ORDER("^[0-9]+$"),
    PEOPLE_COUNT("^[1-9][0-9]*$"),
    TICKET_NUMBER("^[A-Z][0-9]{6}$"),
    TICKET_PASSWORD("^[0-9]{4}$"),
    ADMIN_PASSWORD("^[A-Za-z0-9]{6,15}$"),
    DATE_EIGHT("^([0-9]{4})((01|03|05|07|08|10|12)(0[1-9]|[12][0-9]|3[01])|(02)(0[1-9]|1[0-9]|2[0-8])|(04|06|09|11)(0[1-9]|[12][0-9]|30))$");


    private String value;
    RE(String value) {this.value = value;}

    public String getValue() {
        return value;
    }
}
