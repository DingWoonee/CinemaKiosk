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
            result += (startCh + i) + ":" + joiner;
            if (i != seatArray.length - 1) {
                result += "|";
            }
        }
        return result;
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

    // 좌석 구조 출력
    public void printSeatArray() {
        for (int row = 0; row < seatArray.length; row++) {
            for (int col = 0; col < seatArray[row].length; col++) {
                char rowLabel = (char) ('A' + row);
                if (seatArray[row][col] == -1) {
                    // 빈 좌석(-1)에 대한 처리: 공간 유지
                    System.out.print("    "); // 빈 좌석은 4개의 공백으로 표시
                } else if (seatArray[row][col] == 0) {
                    // 예약 가능한 좌석(0): 좌석 번호와 행 레이블 함께 출력
                    System.out.printf("%s%02d ", rowLabel, col + 1);
                } else if (seatArray[row][col] == 1) {
                    // 예약된 좌석(1): '■' 출력
                    System.out.print(" ■  ");
                }
            }
            System.out.println(); // 행이 끝날 때마다 줄바꿈
        }
    }
}
