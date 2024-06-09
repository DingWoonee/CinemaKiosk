package manager;

import entity.*;
import etc.Prompt;
import etc.RE;

import file.FileManager;

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
            if(!choiceString.matches(RE.MANAGER_MODE_INPUT.getValue())){
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
        }

    }
    public static String inputMovieDate() {
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 날짜 입력(8자리 숫자로 입력): ");
        String movieDeleteTime = sc.nextLine().trim();

        //날짜 형식 검사
        if (!movieDeleteTime.matches("\\d{8}")) {
            throw new InvalidInputException("올바르지 않은 입력입니다.");
        }

        return movieDeleteTime;
    }
    public static void execDeleteMovieSchedule() {
//        Scanner sc = new Scanner(System.in);
//
//        //날짜 입력 받기
//        String inputDate = inputMovieDate();
//
//        //해당 날짜의 영화 스케줄 가져오기
//        List<MovieDetail> scheduleList = movieDetailList.
//        for (MovieDetail movieDetail : movieDetailList) {
//            if(movieDetail.getSchedule()
//        }
//
//        //날짜에 해당하는 스케줄이 없을 경우
//        if (scheduleList == null || scheduleList.isEmpty()) {
//            System.out.println("올바르지 않은 입력입니다.");
//            return;
//        }
//
//        // 영화 스케줄 정보 출력
//        System.out.println("[영화 스케줄 삭제]");
//        System.out.println("스케줄 번호\t영화 이름\t상영 날짜\t상영관\t시작 시간");
//        for (int i = 0; i < scheduleList.size(); i++) {
//            MovieDetail movieDetail = scheduleList.get(i);
//            System.out.println((i + 1) + "\t" + movieDetail.getTitle() + "\t" + movieDetail.getRunningDate() + "\t" + movieDetail.getScreenHall() + "\t" + movieDetail.getStartTime());
//        }
//
//        System.out.print("삭제할 스케줄 번호 입력(숫자만 입력): ");
//        String input = sc.nextLine().trim();
//
//        // 입력 값 검증 및 삭제
//        if (!input.matches("\\d+")) {
//            System.out.println("올바르지 않은 입력입니다.");
//            return;
//        }
//
//        int scheduleIndex = Integer.parseInt(input) - 1;
//
//
//
//        // 스케줄 삭제
//        MovieDetail removedMovieDetail = scheduleList.remove(scheduleIndex);
//


    }

    private void execAddMovieSchedule() {
        List<Movie> movieLists = movieList;
        List<MovieDetail> movieDetailLists = movieDetailList;


        // 상영 날짜 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.print("상영 날짜 입력(8자리 숫자로 입력): ");
        String runningDate = inputRunningDate();

        // 상영관 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println();
        System.out.print("상영관 입력(상영관 번호 두 자리 숫자만 입력): ");
        String screenHall = inputScreenHall();




        // 영화 번호 입력
        int number = 1;
        System.out.println("[영화 스케줄 추가]");
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println("- 상영관: " + screenHall + "관");
        System.out.println("- 해당 날짜 " + screenHall + "관 기존 상영 정보");

        // 리스트 크기 확인
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
        System.out.println();
        // 해야할 것 : <영화이름>: (시작시간~끝시간 /)* 시작시간~끝시간

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
        start = inputMoiveStartTime();

        // 문자열을 시간으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        LocalTime startTime = LocalTime.parse(start, formatter);

        // 더한 시간 계산
        LocalTime endTime = startTime.plusMinutes(selectedMovie.getRunningTime());




        System.out.println("[영화 스케줄 추가 완료]");
        System.out.println("- 영화 이름: " + addMovieName);
        System.out.println("- 상영 날짜: " + runningDate.substring(0, 4) + "년 " + runningDate.substring(4, 6) + "월 " + runningDate.substring(6, 8) + "일 ");
        System.out.println("- 상영관: " + screenHall + "관");
        System.out.println("- 상영 시작 시간: " + formatTime(startTime));
        System.out.println("- 상영 종료 시간: " + formatTime(endTime));

        int movieListSize =  movieDetailLists.size();
        System.out.println(movieListSize);

        StringBuilder scheduleBuild = new StringBuilder();
        scheduleBuild.append(screenHall).append(start).append(endTime.format(formatter));
        String schedule = new String(scheduleBuild);


        List<Seat> seatList = FileManager.seatList;
        Seat seat = seatList.get(Integer.parseInt(screenHall) - 1);
        MovieDetail newMovieDetail = new MovieDetail(movieListSize, addMovieName, selectedMovie.getInfo(), schedule, selectedMovie.getRunningTime(), seat.getSeatArray());
        movieDetailLists.add(newMovieDetail);
        movieDetailList = movieDetailLists;
    }

    // 시간을 "H시 mm분" 형식으로 포맷하는 메소드
    private static String formatTime(LocalTime time) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("H시 mm분");
        return time.format(outputFormatter);
    }

    private String inputMoiveStartTime() {
        Scanner sc = new Scanner(System.in);
        String startTime = sc.nextLine().trim();

        if(!checkStartTime(startTime)){
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }
        return startTime;
    }

    private boolean checkStartTime(String startTime) {
        return true; //startTime.matches(RE.STARTTIME.getValue());
    }

    private String inputMovieNumber() {
        Scanner sc = new Scanner(System.in);
        String movieNumber = sc.nextLine().trim();

        if(!checkMoiveNumber(movieNumber)){
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }

        return movieNumber;
    }

    private boolean checkMoiveNumber(String movieNumber) {
        return true; //movieNumber.matches(RE.MOVIENUMBER.getValue());
    }

    private String inputScreenHall() {
        Scanner sc = new Scanner(System.in);
        String screenHalls = sc.nextLine().trim();

        // 문법 체크
        if(!checkScreenHall(screenHalls)){
            throw new InvalidInputException(Prompt.BAD_INPUT.getPrompt());
        }

        return screenHalls;
    }

    private boolean checkScreenHall(String screenHalls) {
        return screenHalls.matches(RE.ROOM_NUMBER.getValue());
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
        Scanner sc = new Scanner(System.in);

        System.out.println("[영화 삭제]");
        System.out.println("영화 번호\t영화 이름\t러닝 타임");
        for (int i = 0; i < movieDetailList.size(); i++) {
            MovieDetail movieDetail = movieDetailList.get(i);
            int id = movieDetail.getDetailId();
        }
        System.out.print("번호 입력(숫자만 입력): ");
        String input = sc.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println("올바르지 않은 입력입니다.");
            return;
        }

        int movieIndex = Integer.parseInt(input) - 1;

        if (movieIndex < 0 || movieIndex >= movieList.size()) {
            return;
        }

        System.out.println("[정말 삭제하시겠습니까?]");
        System.out.println("1. 삭제");
        System.out.println("2. 취소");
        System.out.print("번호 입력(숫자만 입력): ");
        String confirmInput = sc.nextLine().trim();

        if (!confirmInput.equals("1")) {
            return;
        }

        Movie removedMovie = movieList.remove(movieIndex);


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
            break;
        }

        // 상영관 입력
        System.out.println("\n[영화 추가]");
        System.out.print("영화 제목:\t");
        System.out.println(movieName);
        System.out.print("영화 설명:\t");
        System.out.println(movieDescription);
        System.out.print("러닝 타임 입력(분 단위로 숫자만 입력): ");
        Integer runningTime = inputRunningTime(); // 설계서에서 자료형 수정 Integer, String

        Movie newMoive = new Movie(movieName, movieDescription, runningTime);

        movies.add(newMoive);

        // movieList에 넣기
        FileManager.movieList = movies;
        FileManager.saveMovie();

    }

    private Integer inputRunningTime() {
        Scanner sc = new Scanner(System.in);

        // Integer형으로 변환합니다.
        String runningTime = sc.nextLine().trim();

        if (!checkRunningTime(runningTime)) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return null;
        }


        return Integer.valueOf(runningTime);
    }

    private String inputDescription() {

        Scanner sc = new Scanner(System.in);

        String movieDescription = sc.nextLine().trim();
        if (!checkDescription(movieDescription)) {
            System.out.println(Prompt.NOT_LENGTH10.getPrompt());
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

        if (movieList != null) { // 중복체크
            for (Movie movie : movieList) {
                if (movie.getName().equals(movieTitle)) {
                    isDuplicateTitle = true;
                }
            }
        }

        return true;
    }

    private boolean checkRunningTime(String runningTime) {
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
