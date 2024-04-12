package reservation;

import file.FileManager;

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
    public int movieChoice(){
        System.out.println(FileManager.movieList);
        System.out.println("[영화선택]");
        System.out.println("번호입력(숫자만입력):");
        Scanner scanner = new Scanner(System.in);
        int inputNumber = scanner.nextInt();
        return inputNumber;

    }
    public void movieInfo(int number){

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
