import etc.Prompt;
import file.FileCheck;
import file.FileManager;
import manager.ManagerMain;
import reservation.Cancel;
import reservation.GoHomePromptException;
import reservation.Reservation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws IOException {
        // 날짜 입력 받기
        FileManager.inputDate();

        FileCheck fileCheck = new FileCheck();
        if (!fileCheck.checkAll()) {
            System.out.println("file check error");
            exit(1);
        }

        FileManager fileManager = new FileManager();

        while (true) {
            try {
                System.out.println(Prompt.MAIN.getPrompt());
                System.out.println("[홈]");
                System.out.println("1. 영화예매");
                System.out.println("2. 예매취소");
                System.out.println("3. 관리자");
                System.out.println("0. 종료");
                System.out.print("번호입력(숫자만입력):");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine().trim();
                switch (input) {
                    // 영화 예매
                    case "1" -> {
                        Reservation res = new Reservation(fileManager);
                        res.run();
                    }
                    // 예매 취소
                    case "2" -> {
                        Cancel cancel = new Cancel(fileManager);
                        cancel.run();
                    }
                    // 관리자 모드
                    case "3" -> {
                        ManagerMain mm = new ManagerMain(fileManager);
                        mm.run();
                    }
                    // 종료
                    case "0" -> {
                        exit(0);
                    }
                    default -> {
                        System.out.println(Prompt.BAD_INPUT.getPrompt());
                    }

                }
            }
            catch (GoHomePromptException e){
                Prompt.BAD_INPUT.getPrompt();
            }
            catch (Exception e){
                Prompt.BAD_INPUT.getPrompt();
            }
        }
    }
}