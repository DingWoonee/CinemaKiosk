package entity;

import java.util.List;
import java.util.StringJoiner;

public class Seat {
    private String theaterNum;

    public Seat(String theaterNum, int[][] seatArray) {
        this.theaterNum = theaterNum;
        this.seatArray = seatArray;
    }

    private int[][] seatArray;

    public static String seatToString(int[][] seatArray) {
        char startCh = 65;
        String result = "";
        for (int i = 0; i < seatArray.length; i++) {
            StringJoiner joiner = new StringJoiner(":");
            for (int num : seatArray[i]) {
                joiner.add(String.valueOf(num));
            }
            result += Character.toString(startCh + i) + ":" + joiner;
            if (i != seatArray.length - 1) {
                result += "|";
            }
        }
        return result;
    }
    public static int[][] stringToArray(String str) {
        String[] rows = str.split("\\|");
        String[] colForCount = rows[0].split(":");
        int[][] seatArray = new int[rows.length][colForCount.length - 1];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(":");
            for (int j = 1; j < cols.length; j++) {
                seatArray[i][j - 1] = Integer.parseInt(cols[j]);
            }
        }
        return seatArray;
    }

    public String getTheaterNum() {
        return theaterNum;
    }

    public void setTheaterNum(String theaterNum) {
        this.theaterNum = theaterNum;
    }

    public int[][] getSeatArray() {
        return seatArray;
    }

    public void setSeatArray(int[][] seatArray) {
        this.seatArray = seatArray;
    }


}
