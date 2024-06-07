package entity;

import etc.Prompt;

public class Ticket {
    private String reservationId;
    private String reservationPw;
    private String movieName;
    private String seatCode;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationPw() {
        return reservationPw;
    }

    public void setReservationPw(String reservationPw) {
        this.reservationPw = reservationPw;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }
}
