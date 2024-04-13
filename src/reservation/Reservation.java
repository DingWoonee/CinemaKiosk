package reservation;

import entity.*;
import etc.Prompt;
import etc.RE;
import file.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reservation {
    FileManager fileManager;
    int movieNumber;
    int peopleCount;
    String reservationPassword;


    public Reservation(FileManager fileManager) {
        this.fileManager = fileManager;
    }


    public void run() throws GoHomePromptException {
        tempDataCreate(); // 0. 임시데이터 생성
        movieChoice(); // 1.영화선택
        List<String> selectedSeats = seatChoice(); // 2.좌석선택
        password(); // 3.패스워드 입력
        List<Ticket> tickets = createAndAddTickets(selectedSeats); // 4.티켓 생성, 추가
        FileManager.saveTicketInfo(); // 4. 티켓 저장
        reservationInfo(tickets); // 5. 티켓 정보 출력
    }
    // 0. 임시 데이터 생성
    public void tempDataCreate(){
        FileManager.movieDetailList.add(new MovieDetail(1,"겨울연가","송혜교 주연의 멜로 영화","01", MovieTime.Time1, new int[10][10]));
        FileManager.movieDetailList.add(new MovieDetail(2,"기생충","송강호 주연의 드라마","02", MovieTime.Time2, new int[10][10]));
        // 좌석배열 임시데이터 생성
        int rows = 12; // A부터 L까지 총 12줄
        int cols = 8;  // 1부터 8까지 총 8좌석
        int[][] tempSeats = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                tempSeats[i][j] = 0;
            }
        }
        // 임시데이터 공석과 예매좌석 추가
        tempSeats[3][3] = -1;
        tempSeats[4][5] = 1;
        // FileManager의 seatList에 새로운 Seat 객체를 추가합니다.
        FileManager.seatList.add(new Seat("01", tempSeats));
        FileManager.seatList.add(new Seat("02",tempSeats));
    }
    // 1. 영화 선택
    public void movieChoice() throws GoHomePromptException {

        // 이 부분 movieDetail의 메소드로 바꿔야함.
        MovieDetail.printMovieDetail(FileManager.movieDetailList);

        System.out.println("[영화선택]");
        System.out.print("번호입력(숫자만입력):");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        // 영화번호 잘못 입력시 예외처리
        if (!input.matches(RE.MOVIE_ORDER.getValue())) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            throw new GoHomePromptException("잘못된 입력입니다. 숫자만 입력하세요.");
        }
        try {
            movieNumber = Integer.parseInt(input);
            // 영화번호에 대한 예외처리
            if (movieNumber < 1 || movieNumber > FileManager.movieDetailList.size()) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
            } else {
                movieInfo();
            }
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());

        }
    }
    // 1. 영화 선택  - 예매하기, 홈으로 메뉴
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
                    throw new GoHomePromptException("홈으로");
                default:
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                    movieInfo();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            // 예외 입력시 다시 사용자의 키 입력을 받음
            movieInfo();
        }
    }
    // 1. 영화선택 - 인원 수 입력
    public void countingPeople(){
        System.out.print("인원 수 입력(숫자만 입력):");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        try {
            peopleCount = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            countingPeople();
        }

    }
    // 2. 좌석번호 입력 
    public List<String> seatChoice() {
        List<String> selectedSeats = new ArrayList<>();
        String theaterNum = FileManager.movieDetailList.get(movieNumber - 1).getTheaterNum();
        int theaterIndex = Integer.parseInt(theaterNum) - 1;
        int[][] seats = FileManager.seatList.get(theaterIndex).getSeatArray();

        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        System.out.println("■ : 선택 불가");
        System.out.println("좌석 선택 (입력 예시: A02 A03)");
        FileManager.seatList.get(theaterIndex).printSeatArray(); // 좌석을 출력하는 메소드 호출

        while (!validInput) {
            System.out.print("입력:");
            String input = scanner.nextLine().trim().toUpperCase();
            String[] seatCodes = input.split(" ");

            if (seatCodes.length != peopleCount) {
                System.out.println(Prompt.BAD_INPUT.getPrompt() + " - 인원 수에 맞는 좌석 수를 입력해주세요.");
                continue;
            }

            boolean allSeatsValid = true; // 모든 좌석이 유효한지 검사하는 flag
            for (String seatCode : seatCodes) {
                if (!seatCode.matches(RE.SEAT_NUMBER.getValue())) {
                    System.out.println(Prompt.BAD_INPUT.getPrompt() + " - 잘못된 좌석 번호 형식입니다: " + seatCode);
                    allSeatsValid = false;
                    break;
                }
            }

            if (!allSeatsValid) {
                continue; // 좌석이 하나라도 유효하지 않다면 다시 입력 받습니다.
            }

            selectedSeats.clear(); // 이전에 선택된 좌석 초기화
            // 좌석 예매
            for (String seatCode : seatCodes) {
                int row = seatCode.charAt(0) - 'A';
                int col = Integer.parseInt(seatCode.substring(1)) - 1;

                if (row >= 0 && row < seats.length && col >= 0 && col < seats[row].length && seats[row][col] == 0) {
                    seats[row][col] = 1; // 좌석 예약 표시
                    selectedSeats.add(seatCode);
                } else {
                    System.out.println(Prompt.BAD_INPUT.getPrompt() + " - 이미 예약되었거나 존재하지 않는 좌석입니다: " + seatCode);
                    selectedSeats.clear(); // 에러가 발생하면 이미 추가된 좌석을 클리어
                    break;
                }
            }

            if (!selectedSeats.isEmpty()) {
                validInput = true; // 모든 좌석이 유효하면 루프 종료
            } else {
                System.out.println(Prompt.BAD_INPUT.getPrompt() + " - 선택한 유효한 좌석이 없습니다. 다시 시도하세요.");
            }
        }

        return selectedSeats; // 선택된 좌석 코드 리스트 반환
    }

    // 3. 패스워드 입력
    public void password() {
        System.out.print("예매를 위한 비밀번호를 입력하세요:");
        Scanner scanner = new Scanner(System.in);
        reservationPassword = scanner.nextLine().trim();
        if (!reservationPassword.matches(RE.TICKET_PASSWORD.getValue())) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            password();
        }

    }
    // 4. 티켓 생성
    public List<Ticket> createAndAddTickets(List<String> seatCodes) {
        List<Ticket> tickets = new ArrayList<>();
        MovieDetail movieDetail = FileManager.movieDetailList.get(movieNumber - 1);
        String theaterNum = movieDetail.getTheaterNum();
        MovieTime movieTime = movieDetail.getTime();

        for (String seatCode : seatCodes) {
            String reservationId = generateReservationId(seatCode, theaterNum, movieTime);
            Ticket ticket = new Ticket(reservationId, this.reservationPassword, this.movieNumber, seatCode);
            FileManager.ticketInfoList.add(ticket);
            tickets.add(ticket);
        }
        return tickets;
    }

    // 4. 티켓생성 - 예매 번호 생성
    private String generateReservationId(String seatCode, String theaterNum, MovieTime movieTime) {
        // 상영 시간에 따른 번호 할당
        String timeCode = switch (movieTime) {
            case Time1 -> // "조조" 상영 시간
                    "01";
            case Time2 -> // "미들" 상영 시간
                    "02";
            case Time3 -> // "심야" 상영 시간
                    "03";
            default -> "00"; // 기본값 혹은 잘못된 입력에 대한 처리
        };

        // 예매 번호 생성
        return seatCode.charAt(0) +
                seatCode.substring(1) +
                theaterNum +
                timeCode;
    }
    // 5. 예매 티켓 정보 출력
    public void reservationInfo(List<Ticket> tickets) {
        System.out.println("예매가 완료되었습니다!!");
        System.out.println("[예매정보]");

        for (Ticket ticket : tickets) {
            MovieDetail movieDetail = FileManager.movieDetailList.get(movieNumber - 1);
            System.out.println("예매번호: " + ticket.getReservationId());
            System.out.println("영화 제목: " + movieDetail.getName());
            System.out.println("상영관: " + movieDetail.getTheaterNum());
            System.out.println("상영 시간: " + movieDetail.getTime().getTime());
            System.out.println();
        }
    }

}
