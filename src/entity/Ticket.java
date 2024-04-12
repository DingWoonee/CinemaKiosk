package entity;

public class Ticket {
    private String reservationId;
    private String reservationPw;
    private int detailId;
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

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }
}
