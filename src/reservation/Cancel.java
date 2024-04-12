package reservation;

import entity.MovieDetail;
import entity.Ticket;
import file.FileManager;

import java.util.Scanner;

public class Cancel {
    private FileManager fileManager;

    public Cancel(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    public void run() {
        System.out.println("[예매 취소]");
        System.out.print("예매 번호 입력: ");
        Scanner scanner = new Scanner(System.in);
        String input_reservationId = scanner.nextLine();
        Ticket ticket = getTicket(input_reservationId);

        // 사용자가 입력한 예매 번호가 ticketInfoList<Ticket>에 존재하는 경우
        if (ticket != null) {
            System.out.print("예매 비밀번호 입력: ");
            String input_reservationPw = scanner.nextLine();

            // 사용자 입력과 예매 비밀번호가 일치하는 경우(모두 확인됨)
            if (input_reservationPw.equals(ticket.getReservationPw())) {
                printTicketInfo(ticket);
                System.out.println("[예매를 취소하시겠습니까?]");
                System.out.println("1. 예매 취소");
                System.out.println("2. 홈으로");
                System.out.print("번호 입력(숫자만 입력): ");
                int choice = scanner.nextInt();
                if (choice == 1) {
                    fileManager.ticketInfoList.remove(ticket);
                    System.out.println("예매 취소가 완료되었습니다.");
                }
                else if (choice == 2) {
                    // 홈으로
                }
                else
                    System.out.println("올바르지 않은 입력입니다.");
            }
            else
                System.out.println("비밀번호가 틀립니다.");
        }
        else
            System.out.println("존재하지 않는 예매 번호입니다.");
    }

    // 예매 번호가 존재하면 해당하는 Ticket 객체를 반환, 존재하지 않는 경우 null 반환
    public Ticket getTicket(String input_reservationId) {
        for (Ticket ticket : fileManager.ticketInfoList) {
            if (input_reservationId.equals(ticket.getReservationId())) {
                return ticket;
            }
        }
        return null;
    }

    // 해당 Ticket의 예매 정보 출력
    public void printTicketInfo(Ticket ticket) {
        int detailId = ticket.getDetailId();
        MovieDetail movieDetail = null;

        // ticket의 detailId를 통해서 detailList<MovieDetail>의 정보를 얻어옴
        for (MovieDetail md : FileManager.movieDetailList) {
            if (detailId == md.getDetailId()) {
                movieDetail = md;
            }
        }

        System.out.println("[예매 정보 확인]");
        System.out.println("예매 번호: " + ticket.getReservationId());
        // 아래 3개의 경우 detailList에 해당 detailId가 존재하지 않는 경우 예외 발생(아직 처리하지 않음)
        System.out.println("영화 제목: " + movieDetail.getName());
        System.out.println("상영관 : " + movieDetail.getTheaterNum());
        System.out.println("상영 시간: " + movieDetail.getTime());
        System.out.println();
    }

    public static void main(String[] args) {
        
    }
}

