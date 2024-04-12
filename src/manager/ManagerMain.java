package manager;

import entity.Manager;
import entity.Movie;
import entity.MovieDetail;
import entity.MovieTime;
import etc.Prompt;
import etc.RE;

import file.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerMain {
    private FileManager fileManager;
    //List<Movie> movieList =  FileManager.movieList;
    static List<Movie> movieList;

    boolean isDuplicateTitle;
    boolean isDuplicateTime;

    public ManagerMain(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        System.out.print("관리자 비밀번호 입력: ");
        String inputPw = sc.nextLine();
        if (!inputPw.equals("1234")) {
            System.out.println("관리자 비밀번호가 틀렸습니다.");
        }
        while (true) {
            System.out.println("[관리자 메뉴]");
            System.out.println("1. 영화 목록 출력");
            System.out.println("2. 영화 추가");
            System.out.println("3. 영화 삭제");
            System.out.println("4. 홈");


            System.out.print("번호 입력(숫자만 입력): ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    movieListPrint();
                    break;
                case 2:
                    addMovie(sc);
                    break;
                case 3:
                    deleteMovie(sc);
                    break;
                case 4:
                    // 홈 프롬프트 돌아갑니다..
                    break;

                default:
                    System.out.println("올바르지 않은 입력입니다.");

            }
        }

    }

    private static void deleteMovie(Scanner sc) {
        while (true) {
            System.out.println("[영화 삭제]");
            System.out.println("영화 번호\t\t영화 제목\t\t상영관\t\t상영시간");
            int i = 0;
            for (Movie movie : movieList) {
                StringBuilder sumNum = new StringBuilder();
                for (String num : movie.getTheaterNumList()) {
                    sumNum.append(num).append(",");
                }
                // 마지막 쉼표 제거

                if (sumNum.length() > 0) {
                    sumNum.deleteCharAt(sumNum.length() - 1);
                }
                System.out.printf("%d\t\t%s\t\t%s\t\t%s\n", ++i, movie.getName(), sumNum, movie.getTime());
            }

            System.out.print("번호 입력(숫자만 입력): ");
            String input = sc.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("올바르지 않은 입력입니다.");
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                continue;
            }

            if (choice < 1 || choice > movieList.size()) {
                System.out.println("올바르지 않은 입력입니다.");
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                continue;
            }

            System.out.println("[정말 삭제하시겠습니까?]");
            System.out.println("1. 삭제");
            System.out.println("2. 취소");
            System.out.print("번호 입력(숫자만 입력): ");

            input = sc.nextLine().trim();
            int choice2;
            try {
                choice2 = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("올바르지 않은 입력입니다.");
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                continue;
            }

            if (choice2 == 1) {
                movieList.remove(choice - 1);
                System.out.println("삭제가 완료되었습니다.");
                break;
            } else if (choice2 == 2) {
                System.out.println("삭제가 취소되었습니다.");
                break;
            } else {
                System.out.println("올바르지 않은 입력입니다.");
                System.out.println(Prompt.BAD_INPUT.getPrompt());
            }
        }
    }

    private void addMovie(Scanner sc) {
        Movie newMoive = new Movie();

        // 1단계
        System.out.println("[영화 추가]");
        System.out.print("영화 제목 입력: ");
        String movieName = sc.nextLine().trim();

        if (!checkTitle(movieName)) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return; // 관리자 프롬프트로 이동
        }
        newMoive.setName(movieName);

        // 2단계
        while(true) {
            if(isDuplicateTitle){
                break;
            }

            System.out.print("영화 설명 입력(10자 이상):\t");
            String movieDescription = sc.nextLine().trim();
            if (!checkDescription(movieDescription)) {
                System.out.println(Prompt.BAD_INPUT.getPrompt());
                continue; // 다시 입력
            }
            newMoive.setInfo(movieDescription);
            break;
        }

        System.out.print("상영관 입력(\\'\\|\\'로 구분해서 여러개 입력 가능): ");
        String theaterNums = sc.nextLine().trim();
        if(!checkTheaterNums(theaterNums)){
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            // 후 처리
            return; // 관리자 프롬프트로 이동
        }
        String[] tokens = theaterNums.split(" \\| ");
        List<String> newTheaterNumList = new ArrayList<>();
        for (String token : tokens) {
            newTheaterNumList.add(token);
        }
        newMoive.setTheaterNumList(newTheaterNumList);

        System.out.println("상영 시간 입력(\\'\\|\\'로 구분해서 여러개 입력 가능):");
        String movieTimes = sc.nextLine().trim();
        if(!checkMovieTimes(movieTimes)){
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return;
        }

        String[] movieTime = movieTimes.split(" \\| ");


        // 상영관하고 상영시간 겹치는 경우
        for (String newTheaterNum : newTheaterNumList) { // 입력한 것
            for (Movie movie : movieList) { // 등록된 것
                List<String> theaterNumList = movie.getTheaterNumList();

                for (String theaterNum : theaterNumList) {
                    if(newTheaterNum.equals(theaterNum)){ // 상영관 같으면
                        for (String time : movieTime) {
                            if(time.equals(movie.getTime().getTime())){
                                System.out.println(Prompt.BAD_INPUT.getPrompt());
                                return;
                            }
                        }
                        return; // 관리자 프롬프트
                    }
                }

            }
        }

//        MovieTime.
//        for (String time : movieTime) {
//            newMoive.setTime(time);
//
//        }




    }

    private boolean checkTitle(String movieTitle) {

        if(movieTitle.matches(RE.MOVIE_NAME.getValue())){
            return false;
        }

        for (Movie movie : movieList) {
            if (movie.getName().equals(movieTitle)){
                isDuplicateTitle = true;
            }
        }

        return true;
    }
    private boolean checkDescription(String movieDescription) {

        return !movieDescription.matches(RE.MOVIE_INFO.getValue());
    }



    private static boolean checkTheaterNums(String theaterNums) {
        // "|" 기준으로 나눠서 넣기
        // 상영관 번호는 숫자여야한다.
        // 중복된것 있으면 하나로 처리

        String[] token = theaterNums.split(" \\| ");

        for (String s : token) {
            if(!s.matches(RE.ROOM_NUMBER.getValue())){
                return false;
            }
        }

        return true;
    }

    private static boolean checkMovieTimes(String newMovieTimes) {
        String[] movieTimes = newMovieTimes.split(" \\| ");
        for (String movieTime : movieTimes) {
            // 상영시간 문법 규칙
            if(!movieTime.matches(RE.MOVIE_TIME.getValue())){
                return false;
            }
        }
        return true;
    }





    public static void movieListPrint() {
        movieList = FileManager.movieList;
        System.out.println("[영화 목록 출력]");
        System.out.println("영화 제목\t\t상영관\t\t상영시간");
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

            System.out.printf("%s\t\t%s\t\t%s", movie.getName(), sumNum, movie.getTime());
        }
    }

    public static void main(String[] args) {

    }

}
