package entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDetail {
    private int detailId;
    private String movieName;
    private String movieInfo;
    private String schedule;
    private Integer runningTime;
    private int[][] seatArray;

    public MovieDetail(int detailId, String movieName, String movieInfo, String schedule, Integer runningTime, int[][] seatArray) {
        this.detailId = detailId;
        this.movieName = movieName;
        this.movieInfo = movieInfo;
        this.schedule = schedule;
        this.runningTime = runningTime;
        this.seatArray = seatArray;
    }

    public String getTheaterNumber() {
        // schedule의 앞 2글자를 반환
        if (schedule != null && schedule.length() >= 2) {
            return schedule.substring(0, 2);
        } else {
            return ""; // schedule이 null이거나 길이가 2보다 작을 경우 빈 문자열 반환
        }
    }

    public String getStartTime() {
        if (schedule != null && schedule.length() >= 6) {
            return schedule.substring(2, 6);
        }
        return ""; // 시작시간이 제대로 형성되지 않은 경우 빈 문자열 반환
    }

    public String getEndTime() {
        if (schedule != null && schedule.length() >= 10) {
            return schedule.substring(6, 10);
        }
        return ""; // 종료시간이 제대로 형성되지 않은 경우 빈 문자열 반환
    }

    public String getFormattedTime(String time) {
        if (time.length() == 4) {
            return time.substring(0, 2) + "시 " + time.substring(2) + "분";
        }
        return ""; // 시간 문자열이 제대로 형성되지 않은 경우 빈 문자열 반환
    }

    public static void printMovieDetail(List<MovieDetail> movieDetailList) {
        System.out.println("영화 번호\t\t영화 제목\t\t상영관\t러닝타임\t시작시간\t\t종료시간");
        for (int i = 0; i < movieDetailList.size(); i++) {
            MovieDetail detail = movieDetailList.get(i);
            String theaterNumber = detail.getTheaterNumber();
            if (theaterNumber.startsWith("0")) {
                theaterNumber = theaterNumber.substring(1);
            }
            theaterNumber += "관";

            System.out.printf("%d\t\t\t%s\t\t%s\t\t%s\t\t%s\t%s\n",
                    i + 1,
                    detail.getMovieName(),
                    theaterNumber,
                    detail.getRunningTime(),
                    detail.getFormattedTime(detail.getStartTime()),
                    detail.getFormattedTime(detail.getEndTime()));
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
