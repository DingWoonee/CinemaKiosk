package reservation;

import entity.Movie;
import entity.MovieDetail;
import entity.MovieTime;
import etc.Prompt;
import file.FileManager;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reservation {
    FileManager fileManager;
    int movieNumber;

    int peopleCount;

    public Reservation(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    public void run() {
        movieChoice();
        movieInfo();
        countingPeople();
        seatChoice();
        password();
        reservationInfo();
    }
    public void movieChoice() {
        FileManager.movieDetailList.add(new MovieDetail(1,"겨울연가","송혜교 주연의 멜로 영화","A09", MovieTime.Time1, new int[10][10]));
        // 이 부분 movieDetail의 메소드로 바꿔야함.
        for (MovieDetail movieDetail:FileManager.movieDetailList){
            System.out.println(movieDetail.getDetailId() +" "+ movieDetail.getName() + " " + movieDetail.getTheaterNum());

        }

        System.out.println("[영화선택]");
        System.out.print("번호입력(숫자만입력):");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        try {
            movieNumber = Integer.parseInt(input);
            if (movieNumber < 1 || movieNumber > FileManager.movieDetailList.size()) {
                System.out.println(Prompt.BAD_INPUT);
            } else {
                movieInfo();
            }
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT);
        }
    }

    public void movieInfo() {
        System.out.println(FileManager.movieDetailList.get(movieNumber - 1).getInfo());
        System.out.println("1. 예매하기");
        System.out.println("2. 홈으로");
        System.out.print("번호입력(숫자만 입력): ");

        Scanner scanner = new Scanner(System.in);
        int menuNumber;
        String input = scanner.nextLine().trim();
        try {
            menuNumber = Integer.parseInt(input);
            switch (menuNumber) {
                case 1:
                    countingPeople();
                    break;
                case 2:
                    break;
                default:
                    System.out.println(Prompt.BAD_INPUT);
                    movieInfo();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT);
            movieInfo();
        }
    }
    public void countingPeople(){
        System.out.println("인원 수 입력(숫자만 입력):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        try {
            peopleCount = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT);
            countingPeople();
        }
        

    }
    public void seatChoice(){

    }
    public void password(){

    }
    public void reservationInfo(){

    }
}
