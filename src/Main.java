import file.FileCheck;
import file.FileManager;
import manager.ManagerMain;
import reservation.interfaces.Cancel;
import reservation.interfaces.Reservation;

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

        FileManager fileManager = null;

        while (true) {
            // home
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            switch (Integer.parseInt(br.readLine())) {
                // 영화 예매
                case 1 -> {
                    Reservation res = null;
                    res.run();
                }
                // 예매 취소
                case 2 -> {
                    Cancel cancel = null;
                    cancel.run();
                }
                // 관리자 모드
                case 3 -> {
                    ManagerMain mm = null;
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