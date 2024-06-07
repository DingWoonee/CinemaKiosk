package entity;

import etc.Prompt;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
    private String reservationId;
    private String reservationPw;
    private String movieName;
    private String seatCode;

    public Ticket(String reservationId, String reservationPw, String movieName, String seatCode) {
        this.reservationId = reservationId;
        this.reservationPw = reservationPw;
        this.movieName = movieName;
        this.seatCode = seatCode;
    }

}
