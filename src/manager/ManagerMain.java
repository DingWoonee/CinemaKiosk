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
                case 5:
                    execDeleteMovieSchedule();

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

    private void execDeleteMovieSchedule() {
    }

    private void execAddMovieSchedule() {

    }

    private void deleteMovie() {
        Scanner sc = new Scanner(System.in);
        movieList = FileManager.movieList;
        while (true) {
            System.out.println(Prompt.NEW_MENU_START.getPrompt());
            System.out.println("[영화 삭제]");
            System.out.println("영화 번호\t\t영화 제목\t\t상영관\t\t상영시간");
            int i = 0;
            for (Movie movie : movieList) {
                StringBuilder sumNum = new StringBuilder();
                for (String num : movie.getTheaterNumList()) {
                    sumNum.append(num).append(",");
                }
                if (sumNum.length() > 0) {
                    sumNum.deleteCharAt(sumNum.length() - 1);
                }
                System.out.printf("%10d\t\t%-12s\t\t%-5s\t\t%-3s\n", ++i, movie.getName(), sumNum, movie.getTime().getTime());
            }

            System.out.print("번호 입력(숫자만 입력): ");
            String input = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > movieList.size()) {
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                return;
            }



            System.out.println("\n[정말 삭제하시겠습니까?]");
            System.out.println("1. 삭제");
            System.out.println("2. 취소");
            System.out.print("번호 입력(숫자만 입력): ");
            input = sc.nextLine().trim();

            try {
                int confirmChoice = Integer.parseInt(input);
                if (confirmChoice == 1) {
                    movieList.remove(choice - 1);
                    fileManager.saveMovie(); // 변경사항을 파일에 저장합니다.
                    System.out.println("\n삭제가 완료되었습니다.");
                    break;
                } else if (confirmChoice == 2) {
                    break;
                } else {
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                }
            } catch (NumberFormatException e) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
            }
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
        return runningTime.matches(RE.MOVIE_RUNNINGTIME.getValue());
    }



    private boolean checkDescription(String movieDescription) {

        return movieDescription.matches(RE.MOVIE_INFO.getValue());
    }

    public static void movieListPrint() {
        movieList = FileManager.movieList;
        System.out.println(Prompt.NEW_MENU_START.getPrompt());
        System.out.println("[영화 목록 출력]");
        System.out.printf("%-12s\t%-5s\t%-3s\n", "영화 제목", "상영관", "상영 시간");
        for (Movie movie : movieList) {
            StringBuilder sumNum = new StringBuilder();
            for (String num : movie.getTheaterNumList()) {
                sumNum.append(num);
                sumNum.append(",");
            }
            // 마지막 쉼표 제거
            if (!sumNum.isEmpty()) {
                sumNum.deleteCharAt(sumNum.length() - 1);
            }

            System.out.printf("%-12s\t%-5s\t%-3s\n", movie.getName(), sumNum, movie.getTime().getTime());
        }
    }

    public static void main(String[] args) {

    }

}
