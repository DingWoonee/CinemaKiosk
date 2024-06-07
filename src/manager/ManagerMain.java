package manager;

import entity.*;
import etc.Prompt;
import etc.RE;

import file.FileManager;

import java.util.*;

public class ManagerMain {
    private FileManager fileManager;
    static List<Movie> movieList = new ArrayList<>();

    private boolean isDuplicateTitle = false;

    private int movieDetailId = 0;


    public ManagerMain(FileManager fileManager) {
        this.fileManager = fileManager;
        this.movieList = FileManager.movieList;
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
            System.out.println("4. 홈");
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

    private void execAddMovieSchedule() {


        // 상영 날짜 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.println("상영 날짜 입력(8자리 숫자로 입력): ");
        String runningDate = inputRunningDate();

        // 상영관 입력
        System.out.println("[영화 스케줄 추가]");
        System.out.println("- 상영 날짜: " + runningDate);
        System.out.println();
        System.out.print("상영관 입력(상영관 번호 두 자리 숫자만 입력): ");
        String screenHall = inputScreenHall();



        // 영화 번호 입력
        System.out.print("추가할 영화 번호 입력(숫자만 입력): ");
        String movieNumber = inputMovieNumber();

        // 상영 시작 시간 입력
        System.out.println("상영 시작 시간 입력(4자리 숫자로 입력): ");
        String start;
        start = inputMoiveStartTime();

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

    }

    public static void main(String[] args) {

    }

}
