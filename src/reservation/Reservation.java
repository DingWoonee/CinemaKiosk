package reservation;

import etc.Prompt;
import file.FileManager;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reservation {
    FileManager fileManager;
    int movieNumber;

    public Reservation(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    public void run() {
        movieChoice();
        movieInfo();
        peopleCount();
        seatChoice();
        password();
        reservationInfo();
    }
    public void movieChoice() {
        System.out.println(FileManager.movieList);
        System.out.println("[영화선택]");
        System.out.print("번호입력(숫자만입력):");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        try {
            movieNumber = Integer.parseInt(input);
            if (movieNumber < 1 || movieNumber > FileManager.movieList.size()) {
                System.out.println(Prompt.BAD_INPUT);
            } else {
                movieInfo();
            }
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT);
        }
    }

    public void movieInfo() {
        System.out.println(FileManager.movieList.get(movieNumber - 1).getInfo());
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
                    peopleCount();
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
    public void peopleCount(){

    }
    public void seatChoice(){

    }
    public void password(){

    }
    public void reservationInfo(){

    }
}
