package reservation;

import etc.Prompt;
import file.FileManager;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reservation {
    private FileManager fileManager;

    public Reservation(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    public void run() {
        int inputNumber = movieChoice();
        movieInfo(inputNumber);
        peopleCount();
        seatChoice();
        password();
        reservationInfo();
    }
    public int movieChoice() {
        System.out.println(FileManager.movieList);
        System.out.println("[영화선택]");
        System.out.println("번호입력(숫자만입력):");
        Scanner scanner = new Scanner(System.in);
        int movieNumber = 0;
        try {
            movieNumber = scanner.nextInt();

        } catch (InputMismatchException e) {
            System.out.println(Prompt.BAD_INPUT);
        }
        return movieNumber;

    }
    public void movieInfo(int movieNumber){
        System.out.println(FileManager.movieList.get(movieNumber).getInfo());

        System.out.println("1. 예매하기");
        System.out.println("2. 홈으로");
        System.out.println("번호입력(숫자만 입력):");
        Scanner scanner = new Scanner(System.in);
        try{
            int menuInputNumber = scanner.nextInt();
        }catch(InputMismatchException e){

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
