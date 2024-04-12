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
        FileCheck fileCheck = null;
        if (fileCheck.checkAll()) {
            exit(1);
        }

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
// 확인용 커밋