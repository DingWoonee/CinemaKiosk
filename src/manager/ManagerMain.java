package manager;

import entity.Manager;
import entity.Movie;
import entity.MovieDetail;
import entity.MovieTime;
import etc.Prompt;
import etc.RE;

import file.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ManagerMain {
    private FileManager fileManager;
    static List<Movie> movieList;

    private boolean isDuplicateTitle = false;

    private int movieDetailId = 0;


    public ManagerMain(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean isGoHome = false;

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
                    addMovie();
                    break;
                case 3:
                    deleteMovie(sc);
                    break;
                case 4:
                    // 홈 프롬프트 돌아갑니다..
                    isGoHome = true;
                    break;

                default:
                    System.out.println("올바르지 않은 입력입니다.");
            }
            if(isGoHome){
                isGoHome = false;
                break;
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

    private void addMovie() {
        // FileManager 클래스의 movieList에 접근
        List<Movie> movies = FileManager.movieList;
        Scanner sc = new Scanner(System.in);
        Movie tempMovie = new Movie();
        isDuplicateTitle = false;

        // 영화 제목 입력
        System.out.println("[영화 추가]");
        System.out.print("영화 제목 입력: ");
        String movieName = sc.nextLine().trim();

        if (!checkTitle(movieName)) { // 문법 맞는지 체크
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return; // 관리자 프롬프트로 이동
        }
        tempMovie.setName(movieName);


        // 영화 설명 입력
        String movieDescription = null;
        while(true) {
            if(isDuplicateTitle){ // 영화 이름 중복 시 설명 넣어주기
                for (Movie movie : movies) {
                    if(tempMovie.getName().equals(movie.getName())){
                        tempMovie.setInfo(movie.getInfo());
                        break;
                    }
                }
                break;
            }
            if(movieDescription == null){ // 중복 아니면 직접 입력 받음
                System.out.println("[영화 추가]");
                System.out.print("영화 제목:\t");
                System.out.println(tempMovie.getName());
                System.out.print("영화 설명 입력(10자 이상):\t");
                sc = new Scanner(System.in);
                movieDescription = sc.nextLine().trim();
                if (!checkDescription(movieDescription)) {
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                    continue; // 다시 입력
                }
                tempMovie.setInfo(movieDescription);
                break;
            }
        }

        // 상영관 입력
        System.out.print("영화 제목:\t");
        System.out.println(tempMovie.getName());
        System.out.print("영화 설명:\t");
        System.out.println(tempMovie.getInfo());
        System.out.print("상영관 입력(' | '로 구분해서 여러개 입력 가능): ");
        String theaterNums = sc.nextLine().trim();

        // 문법 체크
        if(!checkTheaterNums(theaterNums)){
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return; // 관리자 프롬프트로 이동
        }

        String[] tokens = theaterNums.split("\\s*\\|\\s*");

        List<String> newTheaterNumList = new ArrayList<>();
        for (String token : tokens) {
            newTheaterNumList.add(token);
        }
        tempMovie.setTheaterNumList(newTheaterNumList);


        // 상영 시간 입력
        System.out.print("영화 제목:\t");
        System.out.println(tempMovie.getName());
        System.out.print("영화 설명:\t");
        System.out.println(tempMovie.getInfo());

        StringBuilder theaterNum = new StringBuilder();
        for (String newTheaterNum : newTheaterNumList) {
            theaterNum.append(newTheaterNum);
            theaterNum.append("관, ");
        }
        theaterNum.deleteCharAt(theaterNum.length() - 1); // 맨뒤 공백 제거
        theaterNum.deleteCharAt(theaterNum.length() - 1); // 맨뒤 , 제거
        System.out.print("상영관:\t");
        System.out.println(theaterNum);
        System.out.print("상영 시간 입력(' | '로 구분해서 여러개 입력 가능):");
        String movieTimes = sc.nextLine().trim();
        if(!checkMovieTimes(movieTimes)){
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return;
        }

        String[] movieTime = movieTimes.split("\\s*\\|\\s*");
        // 상영관하고 상영시간 겹치는 경우
        for (String newTheaterNum : newTheaterNumList) { // 상영관 입력한 것

            if(movies == null || movies.isEmpty()) break;

            for (Movie movie : movies) {  // 저장된 영화들
                List<String> theaterNumList = movie.getTheaterNumList();

                for (String theaterNum_ : theaterNumList) { // 상영관 등록된 것
                    if(newTheaterNum.equals(theaterNum_)){ // 상영관 같으면
                        for (String time : movieTime) { // 시간 같은지 확인
                            if(time.equals(movie.getTime().getTime())){
                                System.out.println(Prompt.BAD_INPUT.getPrompt());
                                return;
                            }
                        }
                    }
                }
            }
        }

        if(!isDuplicateTitle) {
            Movie newMovie;
            for (String time : movieTime) {
                newMovie = new Movie();
                List<String> inputTheaterNumList = new ArrayList<>(tempMovie.getTheaterNumList());
                newMovie.setName(tempMovie.getName());
                newMovie.setInfo(tempMovie.getInfo());
                newMovie.setTheaterNumList(inputTheaterNumList);

                if (time.equals(MovieTime.Time1.getTime())) {
                    newMovie.setTime(MovieTime.Time1);
                } else if (time.equals(MovieTime.Time2.getTime())) {
                    newMovie.setTime(MovieTime.Time2);
                } else if (time.equals(MovieTime.Time3.getTime())) {
                    newMovie.setTime(MovieTime.Time3);
                }

                movies.add(newMovie);
            }
        }
        else{

            for (Movie movie : movies) { // 중복이라는 건 movie가 무조건 있음
                String originTime = movie.getTime().getTime();

                for (String newMovieTime : movieTime) {
                    System.out.println("newMovieTime = " + newMovieTime);
                    System.out.println("originTime = " + originTime);
                    if(!originTime.equals(newMovieTime)){
                        continue;
                    }

                    // 시간 같은경우, 상영관 확인
                    List<String> originTheaterNumList = movie.getTheaterNumList();
                    for (String newTheaterNum : newTheaterNumList) {
                        for (String originTheaterNum : originTheaterNumList) {
                            if(newTheaterNum.equals(originTheaterNum)){ // 시간도 같고 상영관도 같으면 패스
                                continue;
                            }
                            // 시간 같지만, 상영관 다른 경우
                            movie.getTheaterNumList().add(newTheaterNum);
                            Collections.sort(movie.getTheaterNumList());
                            break;
                        }
                    }
                }
            }
        }



        //movieDetailList에 넣기
//        MovieDetail movieDetail = null;
//        for (Movie movie : movies != null ? movies : null) {
//            for (String movieTheaterNum : movie.getTheaterNumList()) {
//                movieDetail = new MovieDetail(++movieDetailId, movie.getName(), movie.getInfo(), theaterNums, movie.getTime(), );
//            }
//            FileManager.movieDetailList.add(movieDetail);
//        }

        // movieList에 넣기
        FileManager.movieList = movies;
        FileManager.saveMovie();

    }

    private boolean checkTitle(String movieTitle) {
        if(!movieTitle.matches(RE.MOVIE_NAME.getValue())){ // 잘못된 형식
            return false;
        }

        if(movieList != null) { // 중복체크
            for (Movie movie : movieList) {
                if (movie.getName().equals(movieTitle)) {
                    isDuplicateTitle = true;
                }
            }
        }

        return true;
    }
    private boolean checkDescription(String movieDescription) {

        return !movieDescription.matches(RE.MOVIE_INFO.getValue());
    }



    private static boolean checkTheaterNums(String theaterNums) {

        // 중복된것 있으면 하나로 처리

        // 문법형식
        if(!theaterNums.matches("^\\s*\\d+\\s*(?:\\|\\s*\\d+\\s*)*$")){
            return false;
        }


        // 상영관 번호는 숫자여야한다. (의미 규칙)
        String[] tokens = theaterNums.split("\\s*\\|\\s*");

        for (String s : tokens) {
            if(!s.matches("^(0?[1-9]|[1-9][0-9]?)$")){
                return false;
            }
        }

        return true;
    }

    private static boolean checkMovieTimes(String newMovieTimes) {
        String[] movieTimes = newMovieTimes.split("\\s*\\|\\s*");
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

            System.out.printf("%s\t\t%s\t\t%s\n", movie.getName(), sumNum, movie.getTime().getTime());
        }
    }

    public static void main(String[] args) {

    }

}
