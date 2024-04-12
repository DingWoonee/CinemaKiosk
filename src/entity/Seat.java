package entity;

import java.util.List;

public class Seat {
    private String theaterNum;

    public Seat(String theaterNum, List<List<Integer>> seatArray) {
        this.theaterNum = theaterNum;
        this.seatArray = seatArray;
    }

    private List<List<Integer>> seatArray;

    public String getTheaterNum() {
        return theaterNum;
    }

    public void setTheaterNum(String theaterNum) {
        this.theaterNum = theaterNum;
    }

    public List<List<Integer>> getSeatArray() {
        return seatArray;
    }

    public void setSeatArray(List<List<Integer>> seatArray) {
        this.seatArray = seatArray;
    }
}
