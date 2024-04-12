package entity;

import file.FileManager;

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

    public static void movieListPrint() {
        List<Movie> movieList =  FileManager.movieList;
        System.out.println("[영화 목록 출력]");
        System.out.println("영화 제목\t\t상영관\t\t상영시간");
        for (Movie movie : movieList) {
            StringBuilder sumNum = new StringBuilder();
            for (String num : movie.getTheaterNumList()) {
                sumNum.append(num);
                sumNum.append(",");
            }
            // 마지막 쉼표 제거
            if (!sumNum.isEmpty()) {
                sumNum.deleteCharAt(sumNum.length() - 1);
            }

            System.out.printf("%s\t\t%s\t\t%s", movie.getName(), sumNum, movie.getTime());
        }
    }
}
