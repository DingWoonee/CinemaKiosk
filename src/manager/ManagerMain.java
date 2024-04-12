package manager;

import entity.Manager;
import entity.Movie;
import file.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerMain {
    private FileManager fileManager;
    List<Movie> movieList =  FileManager.movieList;

    public ManagerMain(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        System.out.print("관리자 비밀번호 입력: ");
        String inputPw = sc.nextLine();
        if(!inputPw.equals()){
            System.out.println("관리자 비밀번호가 틀렸습니다.");
        }

        System.out.println("[관리자 메뉴]");
        System.out.println("1. 영화 목록 출력");
        System.out.println("2. 영화 추가");
        System.out.println("3. 영화 삭제");
        System.out.println("4. 홈");


        System.out.print("번호 입력(숫자만 입력): ");
        int choice = sc.nextInt();

        switch(choice){
            case 1:
                movieListPrint();
                break;
            case 2:
                addMovie(sc);
                break;
            case 3:
                deleteMovie();
                break;
            case 4:
                // 홈 프롬프트 돌아갑니다..
                break;

            default:
                System.out.println("올바르지 않은 입력입니다.");

        }

    }

    private static void deleteMovie() {
        System.out.println("[영화 삭제]");
    }

    private static void addMovie(Scanner sc) {
        System.out.println("[영화 추가]");
        System.out.println("영화 제목 입력: ");
        String movieName = sc.nextLine();
    }

    private void movieListPrint() {
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
