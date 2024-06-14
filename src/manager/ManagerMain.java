package manager;

import entity.*;
import etc.Prompt;
import etc.RE;

import file.FileCheck;
import file.FileManager;
import reservation.GoHomePromptException;

import java.sql.SQLOutput;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ManagerMain {
    private FileManager fileManager;
    static List<Movie> movieList = new ArrayList<>();
    static List<MovieDetail> movieDetailList = new ArrayList<>();
    private boolean isDuplicateTitle = false;

    private int movieDetailId = 0;


    public ManagerMain(FileManager fileManager) {
        this.fileManager = fileManager;
        this.movieList = FileManager.movieList;
        this.movieDetailList = FileManager.movieDetailList;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean isGoHome = false;

        System.out.print("\n관리자 비밀번호 입력: ");
        String inputPw = sc.nextLine();
        if (!inputPw.equals(FileManager.manager.getManagerPw())) {
            System.out.println("\n관리자 비밀번호가 틀렸습니다.");
            return;
        }

        while (true) {
            try {
                System.out.println(Prompt.NEW_MENU_START.getPrompt());
                System.out.println("[관리자 메뉴]");
                System.out.println("1. 영화 목록 출력");
                System.out.println("2. 영화 추가");
                System.out.println("3. 영화 삭제");
                System.out.println("4. 영화 스케줄 추가");
                System.out.println("5. 영화 스케줄 삭제");
                System.out.println("6. 홈");
                System.out.print("번호 입력(숫자만 입력): ");
                String choiceString = sc.nextLine().trim();
                if (!choiceString.matches(RE.MANAGER_MODE_INPUT.getValue())) {
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                    continue;
                }

                int choice = Integer.parseInt(choiceString);

                switch (choice) {
                    case 1:
                        movieListPrint();
                        break;
                    case 2:
                        addMovie();
                        break;
                    case 3:
                        deleteMovie();
                        break;
                    case 4:
                        execAddMovieSchedule();
                        break;
                    case 5:
                        execDeleteMovieSchedule();
                        break;

                    case 6:
                        // 홈 프롬프트 돌아갑니다..
                        isGoHome = true;
                        break;

                    default:
                        System.out.println(Prompt.BAD_INPUT.getPrompt());
                }
                if (isGoHome) {
                    isGoHome = false;
                    break;
                }
            } catch(InvalidInputException e){
                System.out.println(e.getMessage());
            }
        }

    }
    public static String inputMovieDate() {
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 날짜 입력(8자리 숫자로 입력): ");
        String movieDeleteTime = sc.nextLine().trim();

        //날짜 형식 검사
        if (!movieDeleteTime.matches("\\d{8}")) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
        }

        return movieDeleteTime;
    }
    public static void execDeleteMovieSchedule() {
        Scanner sc = new Scanner(System.in);
        try {
            String inputDate = inputMovieDate();

            List<MovieDetail> scheduleList = FileCheck.getMovieDetail(inputDate);
            if (scheduleList == null) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                return;
            }

            System.out.println("[영화 스케줄 삭제]");
            System.out.println("- " + inputDate + "일 상영 스케줄 정보");
            System.out.println("번호 영화 이름\t상영관\t러닝 타임\t상영 시작 시간\t상영 종료 시간");
            for (int i = 0; i < scheduleList.size(); i++) {
                MovieDetail detail = scheduleList.get(i);
                System.out.printf("%d\t%s\t%s\t%s\t%s\t%s\n", i + 1, detail.getMovieName(), detail.getTheaterNumber(), detail.getRunningTime(), detail.getStartTime(), detail.getEndTime());
            }

            System.out.print("삭제할 스케줄 입력(번호만 입력): ");
            int scheduleNumber = Integer.parseInt(sc.nextLine().trim());
            if (scheduleNumber < 1 || scheduleNumber > scheduleList.size()) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                return;
            }
            scheduleList.remove(scheduleNumber - 1);
            if(inputDate.equals(FileManager.todayDate)){
                FileManager.movieDetailList = scheduleList;
            }
            FileManager.saveMovieDetail2(inputDate, scheduleList);

            System.out.println("[영화 스케줄 삭제 완료]");
        } catch (Exception e) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
        }
    }


    private void execAddMovieSchedule() {
        List<Movie> movieLists = movieList;
        //List<MovieDetail> movieDetailLists = movieDetailList;


        // 상영 날짜 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.print("상영 날짜 입력(8자리 숫자로 입력): ");
        String runningDate = inputRunningDate();

        List<MovieDetail> movieDetailLists = FileCheck.getMovieDetail(runningDate);

        // 상영관 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println();
        System.out.print("상영관 입력(상영관 번호 두 자리 숫자만 입력): ");
        String screenHall = inputScreenHall();

        // 영화 번호 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println("- 상영관: " + screenHall + "관");
        System.out.println("- 해당 날짜 " + screenHall + "관 기존 상영 정보");
        printRegisteredSchedules(movieDetailLists);

        int number = 1;
        System.out.println("영화번호\t영화 이름\t러닝 타임");
        for (Movie movieList : movieLists) {
            System.out.print(number++);
            System.out.print("\t\t");
            System.out.print(movieList.getName());
            System.out.print("\t\t");
            System.out.println(movieList.getRunningTime());
        }

        System.out.print("추가할 영화 번호 입력(숫자만 입력): ");
        String movieNumber = inputMovieNumber();
        System.out.println("추가 영화 번호 출력 완료");
        System.out.println();


        // 상영 시작 시간 입력
        Movie selectedMovie = movieLists.get(Integer.parseInt(movieNumber)-1);
        String addMovieName = selectedMovie.getName();

        System.out.println("[영화 스케줄 추가]");
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println("- 상영관: " + screenHall + "관");
        System.out.println("- 해당 날짜 " + screenHall + "관 기존 상영 정보");
        System.out.println();
        System.out.println("추가할 영화 이름: " + addMovieName);
        System.out.println();
        System.out.print("상영 시작 시간 입력(4자리 숫자로 입력): ");

        String start;
        start = inputMovieStartTime();

        // 시작 시간으로부터 추가된 시간을 계산하여 종료 시간 설정
        LocalTime inputStart = LocalTime.parse(start, DateTimeFormatter.ofPattern("HHmm"));
        LocalTime inputEnd = inputStart.plusMinutes(selectedMovie.getRunningTime());
        String end;
        end = inputEnd.format(DateTimeFormatter.ofPattern("HHmm"));

        checkOverlayedSchedule(movieDetailLists, start, end);

        System.out.println("[영화 스케줄 추가 완료]");
        System.out.println("- 영화 이름: " + addMovieName);
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println("- 상영관: " + screenHall + "관");
        System.out.println("- 상영 시작 시간: " + formatTime(inputStart));
        System.out.println("- 상영 종료 시간: " + formatTime(inputEnd));

        
        // 추가될 영화 리스트 번호 만들기
        int movieListSize = makeMovieListNumber(movieDetailLists);
        String schedule = makeSchedule(screenHall, start, end);

        // 상영관에 해당하는 좌석정보 가져오기
        List<Seat> seatList = FileManager.seatList;
        Seat seat = seatList.get(Integer.parseInt(screenHall) - 1);
        MovieDetail newMovieDetail = new MovieDetail(movieListSize, addMovieName, selectedMovie.getInfo(), schedule, selectedMovie.getRunningTime(), seat.getSeatArray());
        if(movieDetailLists == null) {
            movieDetailLists = new ArrayList<>();
        }
        movieDetailLists.add(newMovieDetail);

        if(runningDate.equals(FileManager.todayDate)) {
            FileManager.movieDetailList = movieDetailLists;
        }

        FileManager.saveMovieDetail2(runningDate, movieDetailLists);
    }

    private void checkOverlayedSchedule(List<MovieDetail> movieDetailLists, String start, String end) {
        if(movieDetailLists != null) {
            for (MovieDetail detailList : movieDetailLists) {
                String schedule = detailList.getSchedule();
                if (isWithinSchedule(schedule, start, end)) {
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                    throw new GoHomePromptException(Prompt.BAD_INPUT.getPrompt());
                }
            }
        }
    }

    private static String makeSchedule(String screenHall, String start, String end) {
        // 스케줄 String 만들기
        StringBuilder scheduleBuild = new StringBuilder();
        scheduleBuild.append(screenHall).append(start).append(end);
        String schedule = new String(scheduleBuild);
        return schedule;
    }

    private static int makeMovieListNumber(List<MovieDetail> movieDetailLists) {
        int movieListSize;
        if(movieDetailLists == null) {
            movieListSize = 0;
        }else {
            movieListSize = movieDetailLists.size();
        }
        return movieListSize;
    }

    private static void printRegisteredSchedules(List<MovieDetail> movieDetailLists) {
        // 리스트 크기 확인
        if(movieDetailLists != null) {
            int size = movieDetailLists.size();

            for (int i = 0; i < size; i++) {
                MovieDetail detail = movieDetailLists.get(i);
                System.out.print(detail.getMovieName() + ": ");
                System.out.print(detail.getStartTime().substring(0, 2) + "시" + detail.getStartTime().substring(2, 4) + "분");
                System.out.print(" ~ ");
                System.out.print(detail.getEndTime().substring(0, 2) + "시" + detail.getEndTime().substring(2, 4) + "분");
                if (i < size - 1) {
                    System.out.print(" / ");
                } else {
                    System.out.println(); // 마지막 요소는 "/"를 붙이지 않음
                }
            }
        }
        System.out.println();
    }

    private boolean isWithinSchedule(String schedule, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

        // 기존 스케줄의 시작 시간과 종료 시간
        LocalTime scheduleStart = LocalTime.parse(schedule.substring(2, 6), formatter);
        LocalTime scheduleEnd = LocalTime.parse(schedule.substring(6, 10), formatter);

        // 입력한 시작 시간
        LocalTime inputStart = LocalTime.parse(start, formatter);
        LocalTime inputEnd = LocalTime.parse(end, formatter);

        // 입력된 시간이 기존 스케줄과 겹치는지 확인
        return !((inputStart.isAfter(scheduleEnd.minusMinutes(1)) && inputEnd.isAfter(scheduleEnd)) ||
                inputStart.isBefore(scheduleStart) && inputEnd.isBefore(scheduleStart.plusMinutes(1)));
    }

    // 시간을 "H시 mm분" 형식으로 포맷하는 메소드
    private static String formatTime(LocalTime time) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("H시 mm분");
        return time.format(outputFormatter);
    }

    private String inputMovieStartTime() {
        Scanner sc = new Scanner(System.in);
        String startTime = sc.nextLine().trim();

        if(!checkStartTime(startTime)){
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }
        return startTime;
    }

    private boolean checkStartTime(String startTime) {
        return startTime.matches(RE.MOVIE_START_TIME.getValue());
    }

    private String inputMovieNumber() {
        Scanner sc = new Scanner(System.in);
        String movieNumber = sc.nextLine().trim();

        if(!checkMovieNumber(movieNumber)){
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }

        return movieNumber;
    }

    private boolean checkMovieNumber(String movieNumber) {
        if(movieNumber.isEmpty()) {
            return false;
        }
        return (movieList.size() >= Integer.parseInt(movieNumber))
                && Integer.parseInt(movieNumber) > 0;
    }

    private String inputScreenHall() {
        Scanner sc = new Scanner(System.in);
        String screenHalls = sc.nextLine();

        // 문법 체크
        if(!checkScreenHall(screenHalls)){
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }

        return screenHalls;
    }

    private boolean checkScreenHall(String screenHalls) {
        List<Seat> seatList = FileManager.seatList;

        if(!screenHalls.matches(RE.ROOM_NUMBER.getValue())){
            return false;
        }

        for (Seat seat : seatList) {
            if(seat.getTheaterNum().contains(screenHalls)){
                return true;
            }
        }

        return false;
    }

    private String inputRunningDate() {
        Scanner sc = new Scanner(System.in);
        String runningDate = sc.nextLine().trim();

        if (!checkRunningDate(runningDate)) {
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }
        return runningDate;
    }

    private boolean checkRunningDate(String runningDate) {
        return runningDate.matches(RE.NUM_EIGHT.getValue());
    }

    private void deleteMovie() {
        // 영화 목록 불러오기
        List<Movie> movies = FileManager.movieList;

        Scanner sc = new Scanner(System.in);

        // 영화 목록 출력
        System.out.println("[영화 삭제]");
        System.out.println("번호\t영화 이름\t러닝 타임");
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.printf("%d\t%s\t%d분\n", i + 1, movie.getName(), movie.getRunningTime());
        }

        // 사용자 입력 받기
        System.out.print("삭제할 영화 번호 입력(숫자만 입력): ");
        String input = sc.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return;
        }

        int movieIndex = Integer.parseInt(input) - 1;

        if (movieIndex < 0 || movieIndex >= movies.size()) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return;
        }

        // 삭제 확인
        System.out.println("[정말 삭제하시겠습니까?]");
        System.out.println("1. 삭제");
        System.out.println("2. 취소");
        System.out.print("번호 입력(숫자만 입력): ");
        String confirmInput = sc.nextLine().trim();

        if (!confirmInput.equals("1")) {

            return;
        }
        else{
            System.out.println("삭제가 완료되었습니다.");
            // 영화 삭제
            Movie removedMovie = movies.remove(movieIndex);


            // 업데이트된 영화 목록 저장
            FileManager.movieList = movies;
            FileManager.saveMovie();
        }

    }


    private void addMovie() {
        // FileManager 클래스의 movieList에 접근
        List<Movie> movies = FileManager.movieList;
        Scanner sc = new Scanner(System.in);

        // 영화 제목 입력
        System.out.println(Prompt.NEW_MENU_START.getPrompt());
        System.out.println("[영화 추가]");
        System.out.print("영화 제목 입력: ");
        String movieName = inputTitle();

        
        String movieDescription = null;
        // 영화 설명 입력
        while(true) {
             // 중복 아니면 직접 입력 받음\n\
            System.out.println("\n[영화 추가]");
            System.out.print("영화 제목:\t");
            System.out.println(movieName);
            System.out.print("영화 설명 입력(10자 이상):\t");
            movieDescription = inputDescription();

            if(movieDescription != null) {
                break;
            }
        }

        // 상영관 입력
        System.out.println("\n[영화 추가]");
        System.out.print("영화 제목:\t");
        System.out.println(movieName);
        System.out.print("영화 설명:\t");
        System.out.println(movieDescription);
        System.out.print("러닝 타임 입력(분 단위로 숫자만 입력): ");
        Integer runningTime = inputRunningTime(); // 설계서에서 자료형 수정 Integer, String

        Movie newMovie = new Movie(movieName, movieDescription, runningTime);

        movies.add(newMovie);

        // 영화 정보 출력
        System.out.println("\n[영화 추가 완료]");
        System.out.print("영화 제목:\t");
        System.out.println(movieName);
        System.out.print("영화 설명:\t");
        System.out.println(movieDescription);
        System.out.print("러닝 타임:\t");
        System.out.println(runningTime + "분");

        // movieList에 넣기
        FileManager.movieList = movies;
        FileManager.saveMovie();

    }

    private Integer inputRunningTime() {
        String runningTime;
        while (true) {
            Scanner sc = new Scanner(System.in);

            // Integer형으로 변환합니다.
            runningTime = sc.nextLine().trim();

            if (!checkRunningTime(runningTime)) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                System.out.print("\n러닝 타임 입력(분 단위로 숫자만 입력): ");
            } else {
                break;
            }
        }

        return Integer.valueOf(runningTime);
    }

    private String inputDescription() {

        Scanner sc = new Scanner(System.in);

        String movieDescription = sc.nextLine().trim();
        if (!checkDescription(movieDescription)) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return null;
        }
        return movieDescription;
        
    }

    private String inputTitle() {
        Scanner sc = new Scanner(System.in);

        String movieName = sc.nextLine().trim();

        if (!checkTitle(movieName)) { // 문법 맞는지 체크
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }
        
        return movieName;
    }

    private boolean checkTitle(String movieTitle) {
        if (!movieTitle.matches(RE.MOVIE_NAME.getValue())) { // 잘못된 형식
            return false;
        }

        for (Movie movie : movieList) { // 중복된 영화 이름
            if(movie.getName().equals(movieTitle)){
                return false;
            }
        }

        return true;
    }

    private boolean checkRunningTime(String runningTime) {
        // 숫자인지 검사
        for (char c : runningTime.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        // 크기 검사
        int integerRunningTime = Integer.parseInt(runningTime);
        if (integerRunningTime > 360 || integerRunningTime < 1) {
            return false;
        }
        // 자리수 검사
        if (runningTime.length() > 3 || runningTime.isEmpty()) {
            return false;
        }
        return true;//runningTime.matches(RE.MOVIE_RUNNINGTIME.getValue());
    }



    private boolean checkDescription(String movieDescription) {

        return movieDescription.matches(RE.MOVIE_INFO.getValue());
    }

    public static void movieListPrint() {
        movieList = FileManager.movieList;

        System.out.println("[영화 목록 출력]");
        System.out.println("영화 이름\t러닝 타임");
        for (Movie movie : movieList) {
            System.out.println(movie.getName() + "\t\t" + movie.getRunningTime());
        }
    }

    public static void main(String[] args) {

    }

}
