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
        Movie tempMovie = new Movie();
        isDuplicateTitle = false;

        // 영화 제목 입력
        System.out.println(Prompt.NEW_MENU_START.getPrompt());
        System.out.println("[영화 추가]");
        System.out.print("영화 제목 입력: ");
        String movieName = sc.nextLine().trim();

        if (!checkTitle(movieName)) { // 문법 맞는지 체크
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return; // 관리자 프롬프트로 이동
        }
        tempMovie.setName(movieName);


        // 영화 설명 입력
        while(true) {
            String movieDescription = null;
            if(isDuplicateTitle){ // 영화 이름 중복 시 설명 넣어주기

                for (Movie movie : movies) {
                    if (tempMovie.getName().equals(movie.getName())) {
                        tempMovie.setInfo(movie.getInfo());
                        break;
                    }
                }
                break;
            }
            else{ // 중복 아니면 직접 입력 받음\n\
                System.out.println("\n[영화 추가]");
                System.out.print("영화 제목:\t");
                System.out.println(tempMovie.getName());
                System.out.print("영화 설명 입력(10자 이상):\t");
                sc = new Scanner(System.in);
                movieDescription = sc.nextLine().trim();
                if (!checkDescription(movieDescription)) {
                    System.out.println(Prompt.NOT_LENGTH10.getPrompt());
                    continue; // 다시 입력
                }
                tempMovie.setInfo(movieDescription);
                break;
            }
        }

        // 상영관 입력
        System.out.println("\n[영화 추가]");
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

        // 입력 토큰화 및 중복 제거
        String[] tokens = theaterNums.split("\\s*\\|\\s*");
        Set<String> uniqueTokens = new LinkedHashSet<>(Arrays.asList(tokens));


        List<String> newTheaterNumList = new ArrayList<>();
        for (String token : uniqueTokens) {
            newTheaterNumList.add(token);
        }
        tempMovie.setTheaterNumList(newTheaterNumList);


        // 상영 시간 입력
        System.out.println("\n[영화 추가]");
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
        if (!checkMovieTimes(movieTimes)) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return;
        }

        String[] movieTime = movieTimes.split("\\s*\\|\\s*");
        Set<String> uniqueTimes = new LinkedHashSet<>(Arrays.asList(movieTime));
        Set<String> uniqueTimes2 = new LinkedHashSet<>(Arrays.asList(movieTime));


        // 상영관하고 상영시간 겹치는 경우
        for (String newTheaterNum : newTheaterNumList) { // 상영관 입력한 것

            if (movies == null || movies.isEmpty()) break;

            for (Movie movie : movies) {  // 저장된 영화들
                List<String> theaterNumList = movie.getTheaterNumList();

                for (String theaterNum_ : theaterNumList) { // 상영관 등록된 것
                    if (newTheaterNum.equals(theaterNum_)) { // 상영관 같으면
                        for (String time : uniqueTimes) { // 시간 같은지 확인
                            if (time.equals(movie.getTime().getTime())) {
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
            for (String time : uniqueTimes) {
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

                for (String newMovieTime : uniqueTimes) {
                    // 시간이 다른 경우
                    if(!originTime.equals(newMovieTime)){
                        continue;
                    }

                    // 시간 같은경우, 상영관 확인 (시작)
                    int leftUniqueTime = uniqueTimes.size();
                    List<String> originTheaterNumList = movie.getTheaterNumList();
                    for (String newTheaterNum : newTheaterNumList) {
                        for (String originTheaterNum : originTheaterNumList) {
                            // 시간도 같고 상영관도 같으면 패스
                            if(newTheaterNum.equals(originTheaterNum)){
                                continue;
                            }
                            // 시간 같지만, 상영관 다른 경우
                            if(movie.getName().equals(tempMovie.getName())) {
                                movie.getTheaterNumList().add(newTheaterNum);
                                Collections.sort(movie.getTheaterNumList());

                                // Num 빼기
                                leftUniqueTime -= 1;
                                if(leftUniqueTime == 0){
                                    uniqueTimes.remove(newMovieTime);
                                }

                                break;
                            }
                        } // 상영관 번호 확인 (끝)
                    } // 시간 같은경우, 상영관 확인 (끝)
                } // 영화 시간 탐색 (끝)
            } // 영화 전체 탐색(끝)

            if(!uniqueTimes.isEmpty()){
                Movie newMovie;
                for (String time : uniqueTimes) {
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
        } // else문 (끝)

        StringBuilder theaterTime = new StringBuilder();
        for (String time : uniqueTimes2) {
            theaterTime.append(time);
            theaterTime.append(", ");
        }
        theaterTime.deleteCharAt(theaterTime.length() - 1); // 맨뒤 공백 제거
        theaterTime.deleteCharAt(theaterTime.length() - 1); // 맨뒤 , 제거

        System.out.println("\n[영화 추가 완료]");
        System.out.print("영화 제목:\t");
        System.out.println(tempMovie.getName());
        System.out.print("영화 설명:\t");
        System.out.println(tempMovie.getInfo());
        System.out.print("상영관:\t");
        System.out.println(theaterNum);
        System.out.print("상영시간:\t");
        System.out.println(theaterTime);

        // movieList에 넣기
        FileManager.movieList = movies;
        FileManager.saveMovie();

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

    private boolean checkDescription(String movieDescription) {

        return movieDescription.matches(RE.MOVIE_INFO.getValue());
    }


    private static boolean checkTheaterNums(String theaterNums) {

        // 상영관 번호는 숫자여야한다.
        String[] tokens = theaterNums.split("\\s*\\|\\s*");

        List<Seat> seatList = FileManager.seatList;

        // 정규표현식 확인
        for (String s : tokens) {
            if(!s.matches(RE.ROOM_NUMBER.getValue())){ // "^(0?[1-9]|[1-9][0-9]?)$"
                return false;
            }

            if (!FileManager.isTheaterNumExist(s)) {
                return false;
            }
        }



        return true;
    }

    private static boolean checkMovieTimes(String newMovieTimes) {
        String[] movieTimes = newMovieTimes.split("\\s*\\|\\s*");
        for (String movieTime : movieTimes) {
            // 상영시간 문법 규칙
            if (!movieTime.matches(RE.MOVIE_TIME.getValue())) {
                return false;
            }
        }
        return true;
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
