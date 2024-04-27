package reservation;

import entity.*;
import etc.Prompt;
import etc.RE;
import file.FileManager;

import java.util.*;

public class Reservation {
    FileManager fileManager;
    int selectedMovieNumber;
    int peopleCount;
    String reservationPassword;


    public Reservation(FileManager fileManager) {
        this.fileManager = fileManager;
    }


    public void run() throws GoHomePromptException {
        System.out.println(Prompt.NEW_MENU_START.getPrompt());
        movieChoice(); // 1.영화선택
        List<String> selectedSeats = seatChoice(); // 2.좌석선택
        password(); // 3.패스워드 입력
        List<Ticket> tickets = createAndAddTickets(selectedSeats); // 4.티켓 생성, 추가
        FileManager.saveTicketInfo(); // 4. 티켓 저장
        reservationInfo(tickets); // 5. 티켓 정보 출력
    }


    // 1. 영화 선택
    public void movieChoice() throws GoHomePromptException {
        MovieDetail.printMovieDetail(FileManager.movieDetailList); // 영화 목록 출력

        System.out.println("[영화선택]");
        System.out.print("번호입력(숫자만입력):");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        // 영화번호 잘못 입력시 예외처리
        if (!input.matches(RE.MOVIE_ORDER.getValue())) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            throw new GoHomePromptException("\n잘못된 입력입니다. 숫자만 입력하세요.");
        }
        try {
            selectedMovieNumber = Integer.parseInt(input);
            // 영화번호에 대한 예외처리
            if (selectedMovieNumber < 1 || selectedMovieNumber > FileManager.movieDetailList.size()) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                throw new GoHomePromptException();

            } else {
                movieInfo();
            }
        } catch (NumberFormatException e) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            throw new GoHomePromptException();

        }
    }

    // 1. 영화 선택  - 예매하기, 홈으로 메뉴
    public void movieInfo() {
        System.out.println();
        System.out.println("[영화정보]");
        System.out.println("-제목-");
        System.out.println(FileManager.movieDetailList.get(selectedMovieNumber - 1).getName());
        System.out.println("-정보-");
        System.out.println(FileManager.movieDetailList.get(selectedMovieNumber - 1).getInfo());
        System.out.println();
        System.out.println("[예매선택]");
        System.out.println("1. 예매하기");
        System.out.println("2. 홈으로");
        System.out.print("번호입력(숫자만 입력): ");

        Scanner scanner = new Scanner(System.in);
        String menuNumber = scanner.nextLine().trim();
        try {
            FileManager.validateInputWithRE(menuNumber, RE.MOVIE_CHOICE_MENU_NUMBER.getValue());
            switch (menuNumber) {
                case "1":
                    countingPeople();
                    break;
                case "2":
                    throw new GoHomePromptException("\n홈으로");
            }
        } catch (InputRetryException e) {
            // 예외 입력시 다시 사용자의 키 입력을 받음
            movieInfo();
        }
    }

    // 1. 영화선택 - 인원 수 입력
    public void countingPeople() {
        System.out.print("\n인원 수 입력(숫자만 입력):");
        Scanner scanner = new Scanner(System.in);
        String tempPeopleCount = scanner.nextLine().trim();
        try {
            FileManager.validateInputWithRE(tempPeopleCount, RE.PEOPLE_COUNT.getValue());
            peopleCount = Integer.parseInt(tempPeopleCount);


        } catch (InputRetryException e) {
            countingPeople();
        }
    }

    // 2. 좌석 선택
    public List<String> seatChoice() throws GoHomePromptException {
        List<String> selectedSeats = new ArrayList<>();
        MovieDetail movieDetail = FileManager.movieDetailList.get(selectedMovieNumber - 1);
        int[][] seats = movieDetail.getSeatArray();
        int[][] originalSeats = new int[seats.length][]; // 좌석 예약 과정 중 오류가 났을 때 복구하기 위한 배열

        // 원래 좌석 상태 복사
        for (int i = 0; i < seats.length; i++) {
            originalSeats[i] = new int[seats[i].length];
            for (int j = 0; j < seats[i].length; j++) {
                originalSeats[i][j] = seats[i][j]; // 좌석 상태 직접 복사
            }
        }

        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;



        while (!isValidInput) {
            System.out.println("■ : 선택 불가");
            System.out.println("좌석 선택 (입력 예시: A02 A03)");
            movieDetail.printSeatArray(); // 좌석 구조 출력
            System.out.print("입력:");
            String input = scanner.nextLine().trim().toUpperCase();
            String[] seatCodes = input.split(" ");
            // 중복 체크
            Set<String> seatSet = new HashSet<>(Arrays.asList(seatCodes));
            if (seatSet.size() != seatCodes.length) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                continue; // 중복이 있으면 입력을 다시 받습니다.
            }
            // 인원수 검사
            if (seatCodes.length != peopleCount) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                continue;
            }

            try {
                // 좌석마다 문법/예외 규칙 확인, 예약 표시
                for (String seatCode : seatSet) {
                    FileManager.validateInputWithRE(seatCode, RE.SEAT_NUMBER.getValue());
                    int row = seatCode.charAt(0) - 'A';
                    int col = Integer.parseInt(seatCode.substring(1)) - 1;

                    if (row >= 0 && row < seats.length && col >= 0 && col < seats[row].length && seats[row][col] == 0) {
                        seats[row][col] = 1; // 좌석 예약 표시
                        selectedSeats.add(seatCode);
                    } else {
                        System.out.println(Prompt.BAD_INPUT.getPrompt());
                        throw new InputRetryException("\n선택한 좌석이 유효하지 않습니다. 다시 시도하세요.");
                    }
                }
                isValidInput = true; // 모든 좌석이 유효하면 루프 종료
            } catch (InputRetryException e) {
                // 오류 발생 시 좌석 복원 로직
                for (int i = 0; i < seats.length; i++) {
                    for (int j = 0; j < seats[i].length; j++) {
                        seats[i][j] = originalSeats[i][j]; // 원래 상태로 복원
                    }
                }
                selectedSeats.clear();
            }
        }
        return selectedSeats;
    }


    // 3. 패스워드 입력
    public void password() {
        System.out.print("\n예매를 위한 비밀번호를 입력하세요:");
        Scanner scanner = new Scanner(System.in);
        reservationPassword = scanner.nextLine().trim();
        try {
            FileManager.validateInputWithRE(reservationPassword, RE.TICKET_PASSWORD.getValue());
        } catch (InputRetryException e) {
            password();
        }

    }

    // 4. 티켓 생성
    public List<Ticket> createAndAddTickets(List<String> seatCodes) {
        List<Ticket> tickets = new ArrayList<>();
        MovieDetail movieDetail = FileManager.movieDetailList.get(selectedMovieNumber - 1);
        String theaterNum = movieDetail.getTheaterNum();
        MovieTime movieTime = movieDetail.getTime();

        for (String seatCode : seatCodes) {
            String reservationId = generateReservationId(seatCode, theaterNum, movieTime);
            Ticket ticket = new Ticket(reservationId, this.reservationPassword,movieDetail.getDetailId(), seatCode);
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
        System.out.println("\n예매가 완료되었습니다!!");
        System.out.println("[예매정보]");

        for (Ticket ticket : tickets) {
            MovieDetail movieDetail = FileManager.movieDetailList.get(selectedMovieNumber - 1);
            System.out.println("예매번호: " + ticket.getReservationId());
            System.out.println("영화 제목: " + movieDetail.getName());
            System.out.println("상영관: " + movieDetail.getTheaterNum());
            System.out.println("상영 시간: " + movieDetail.getTime().getTime());
            System.out.println();
        }
    }

}
