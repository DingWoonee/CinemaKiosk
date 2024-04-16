package entity;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail {
    private int detailId;
    private String name;
    private String info;
    private String theaterNum;
    private MovieTime time;


    public MovieDetail(int detailId, String name, String info, String theaterNum, MovieTime time, int[][] seatArray) {
        this.detailId = detailId;
        this.name = name;
        this.info = info;
        this.theaterNum = theaterNum;
        this.time = time;
        this.seatArray = seatArray;
    }

    private int[][] seatArray;

    public int getDetailId() {
        return detailId;

    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTheaterNum() {
        return theaterNum;
    }

    public void setTheaterNum(String theaterNum) {
        this.theaterNum = theaterNum;
    }

    public MovieTime getTime() {
        return time;
    }

    public void setTime(MovieTime time) {
        this.time = time;
    }

    public int[][] getSeatArray() {
        return seatArray;
    }

    public void setSeatArray(int[][] seatArray) {
        this.seatArray = seatArray;
    }
    public static void printMovieDetail(List<MovieDetail> movieDetailList){
        System.out.println("영화 번호\t\t영화 제목\t\t상영관\t\t상영시간");
        for (int i=0;i<movieDetailList.size(); i++) {
            System.out.printf("%d\t\t\t%s\t\t%s\t\t    %s\n", i+1,movieDetailList.get(i).name, movieDetailList.get(i).theaterNum, movieDetailList.get(i).time.getTime());
        }
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
