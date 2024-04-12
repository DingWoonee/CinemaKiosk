package manager;

import entity.Manager;
import entity.Movie;
import entity.MovieDetail;
import file.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerMain {
    private FileManager fileManager;
    //List<Movie> movieList =  FileManager.movieList;
    static List<Movie> movieList;

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

    private static void deleteMovie(Scanner sc) {
        System.out.println("[영화 삭제]");
        System.out.println("영화 번호\t\t영화 제목\t\t상영관\t\t상영시간");
        int i = 0;
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
            System.out.printf("%d\t\t%s\t\t%s\t\t%s", ++i, movie.getName(), sumNum, movie.getTime());

        }
        System.out.print("번호 입력(숫자만 입력): ");
        String input = (sc.nextLine()).trim();
        int choice;
        try {
            choice = Integer.valueOf(input);


        } catch (Exception e) {
            System.out.println("올바르지 않은 입력입니다.");
            return;
        }
        try {
            FileManager.movieList.remove(choice - 1);

        } catch (Exception e) {
            System.out.println("올바르지 않은 입력입니다.");
        }

    }

    private static void addMovie(Scanner sc) {
        System.out.println("[영화 추가]");
        System.out.println("영화 제목 입력: ");
        String movieName = sc.nextLine();
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

}
