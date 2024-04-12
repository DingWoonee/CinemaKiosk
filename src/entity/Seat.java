package entity;

import java.util.List;

public class Seat {
    private String theaterNum;
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
