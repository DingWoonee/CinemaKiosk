import file.FileCheck;
import file.FileManager;
import manager.ManagerMain;
import reservation.Cancel;
import reservation.Reservation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws IOException {
<<<<<<< HEAD
        FileCheck fileCheck = null;
//        if (fileCheck.checkAll()) {
//            exit(1);
//        }
=======
        FileCheck fileCheck = new FileCheck();
        if (fileCheck.checkAll()) {
            exit(1);
        }
>>>>>>> 5c9b8a564c4468dd00111a5d453e87ff539c3f26

        FileManager fileManager = new FileManager();

        while (true) {
            // home
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            switch (Integer.parseInt(br.readLine())) {
                // 영화 예매
                case 1 -> {
                    Reservation res = new Reservation(fileManager);
                    res.run();
                }
                // 예매 취소
                case 2 -> {
                    Cancel cancel = new Cancel(fileManager);
                    cancel.run();
                }
                // 관리자 모드
                case 3 -> {
                    ManagerMain mm = new ManagerMain(fileManager);
                    mm.run();
                }
                // 종료
                case 0 -> {
                    exit(0);
                }
                // 예외
                default -> {

                }
            }
        }
    }
}