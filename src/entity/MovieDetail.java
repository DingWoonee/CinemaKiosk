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
            System.out.printf("%d\t\t\t%s\t\t%s\t\t    %s\n", i+1,movieDetailList.get(i).name, movieDetailList.get(i).theaterNum, movieDetailList.get(i).time);
        }
    }
}
