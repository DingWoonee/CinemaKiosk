package reservation;

import entity.Movie;
import entity.MovieDetail;
import entity.MovieTime;
import entity.Seat;
import etc.Prompt;
import file.FileManager;

import java.io.File;
import java.util.InputMismatchException;
import java.util.List;
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
        seatChoice();
        password();
        reservationInfo();
    }
    public void movieChoice() {
        // 임시 데이터 추가
        FileManager.movieDetailList.add(new MovieDetail(1,"겨울연가","송혜교 주연의 멜로 영화","01", MovieTime.Time1, new int[10][10]));
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
        System.out.print("인원 수 입력(숫자만 입력):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        try {
            peopleCount = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT);
            countingPeople();
        }

    }
    public void seatChoice() {
        // 좌석배열 데이터 임시로 생성
        int rows = 12; // A부터 L까지 총 12줄
        int cols = 8;  // 1부터 8까지 총 8좌석
        int[][] tempSeats = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                tempSeats[i][j] = 0;
            }
        }
        tempSeats[3][3] = -1;
        tempSeats[4][5] = 1;
        // FileManager의 seatList에 새로운 Seat 객체를 추가합니다.
        // 여기서 'A09'는 상영관 번호를 의미하며, 실제 시스템에서는 영화 상영관 번호에 맞게 조정될 것입니다.
        FileManager.seatList.add(new Seat("01", tempSeats));


        // 파일 매니저로부터 영화의 상영관 번호를 가져옴
        String rawTheaterNum = FileManager.movieDetailList.get(movieNumber - 1).getTheaterNum();
        // 상영관 번호가 한 자리 숫자일 경우, 예를 들어 '1'이면 '01'로 표기하기 위한 조건문
        int theaterIndex = Integer.parseInt(rawTheaterNum) - 1;
        // 상영관 번호에 해당하는 좌석 배열을 가져옴
        int[][] seats = FileManager.seatList.get(theaterIndex).getSeatArray();
        System.out.println("■ : 선택 불가");
        System.out.println("좌석 선택 (입력 예시: A02 A03)");
        FileManager.seatList.get(theaterIndex).printSeatArray(); // 좌석을 출력하는 메소드 호출 (아래에 정의 필요)

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.length() < 2) {
                System.out.println(Prompt.BAD_INPUT);
                continue;
            }
            int row = input.charAt(0) - '1';
            int col = input.charAt(1) - 'A';

            // 배열의 index bounds 체크
            if (row >= 0 && row < seats.length && col >= 0 && col < seats[row].length) {
                if (seats[row][col] == 0) {
                    seats[row][col] = 1; // 좌석 예약 표시
                    break;
                } else {
                    System.out.println("이미 예약되었거나 존재하지 않는 좌석입니다. 다시 선택해주세요.");
                }
            } else {
                System.out.println("존재하지 않는 좌석입니다. 다시 선택해주세요.");
            }
        }
    }


    public void password() {
        System.out.println("예매를 위한 비밀번호를 입력하세요:");
        Scanner scanner = new Scanner(System.in);
        String reservationPassword = scanner.nextLine().trim();
        // 비밀번호를 저장하거나 처리하는 로직이 필요합니다.
        // 현재 예제에서는 출력만 합니다.
        System.out.println("입력된 비밀번호: " + reservationPassword);
    }

    public void reservationInfo() {
        MovieDetail selectedMovie = FileManager.movieDetailList.get(movieNumber - 1);
        System.out.println("예매 정보를 확인하세요:");
        System.out.println("영화: " + selectedMovie.getName());
        System.out.println("시간: " + selectedMovie.getTime());
        System.out.println("인원 수: " + peopleCount);
        // 선택한 좌석 정보도 출력해야 합니다.
        // 좌석 선택 로직이 완성되면 해당 정보를 여기에 추가합니다.

        // 이 정보를 예약 정보와 함께 파일에 저장하거나 데이터베이스에 저장하는 로직을 구현할 수 있습니다.
        // 현재 예제에서는 출력만 합니다.
    }

}
