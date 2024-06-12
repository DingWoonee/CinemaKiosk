package reservation;

import entity.MovieDetail;
import entity.Seat;
import entity.Ticket;
import etc.Prompt;
import file.FileManager;

import java.util.Scanner;

public class Cancel {
	private FileManager fileManager;

	public Cancel(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void run() {
		System.out.println(Prompt.NEW_MENU_START.getPrompt());
		System.out.println("[예매 취소]");
		System.out.print("예매 번호 입력: ");
		Scanner scanner = new Scanner(System.in);

		Ticket ticket = getTicket(scanner.nextLine().trim());

		// 사용자가 입력한 예매 번호가 ticketInfoList<Ticket>에 존재하는 경우
		if (ticket != null) {
			// 비밀 번호 일치 여부 확인
			if (checkPw(ticket)) {
				printTicketInfo(ticket);
				// 최종 승인
				int final_choice = finalCheck();
				switch (final_choice) {
					case 1 -> {
						// 예매 취소 및 좌석 삭제
//						cancelRerservation(ticket);
						// TicketInfo을 업데이트
						FileManager.saveTicketInfo();
						System.out.println("예매 취소가 완료되었습니다.");
					}
					case 2 -> { return; }
					default -> System.out.println(Prompt.BAD_INPUT.getPrompt());
				}
			}
			else
				System.out.println("\n비밀번호가 틀립니다.");
		}
		else
			System.out.println(Prompt.NOT_EXIST_RESERVATION.getPrompt());
	}

	// 입력받은 예매 번호가 존재하면 해당하는 Ticket 객체를 반환, 존재하지 않는 경우 null 반환
	public Ticket getTicket(String input_reservationId) {
		for (Ticket ticket : FileManager.ticketInfoList)
			if (input_reservationId.equals(ticket.getReservationId()))
				return ticket;
		return null;
	}

	// 비밀번호 확인 (일치하면 true, 불일치하면 false 반환)
	public boolean checkPw(Ticket ticket) {
		System.out.print("\n예매 비밀번호 입력: ");
		Scanner scanner = new Scanner(System.in);

		String input_reservationPw = scanner.nextLine().trim();
		if (input_reservationPw.equals(ticket.getReservationPw()))
			return true;
		else return false;
	}

	// 해당 Ticket의 예매 정보 출력
	public void printTicketInfo(Ticket ticket) {
		MovieDetail movieDetail = getMovieDetailFromTicket(ticket);

		System.out.println(Prompt.NEW_MENU_START.getPrompt());
		System.out.println("[예매 정보 확인]");
		System.out.println("예매 번호: " + ticket.getReservationId());
		// 아래 3개의 경우 detailList에 해당 detailId가 존재하지 않는 경우 예외 발생(아직 처리하지 않음)
		try {
			System.out.println("영화 제목: " + movieDetail.getMovieName());
			System.out.println("상영관: " + movieDetail.getTheaterNumber());
			System.out.println("상영 시간: " + movieDetail.getFormattedTime(movieDetail.getStartTime()) + " ~ " + movieDetail.getFormattedTime(movieDetail.getEndTime()));
		} catch (NullPointerException e) {
			System.out.println("NullPointerException: printTicketInfo");
		}
		System.out.println();
	}

	// 최종 승인
	public int finalCheck() {
		System.out.println("\n[예매를 취소하시겠습니까?]");
		System.out.println("1. 예매 취소");
		System.out.println("2. 홈으로");
		System.out.print("번호 입력(숫자만 입력): ");
		Scanner scanner = new Scanner(System.in);

		String input = scanner.nextLine().trim();
		if (input.equals("1"))
			return 1;
		else if (input.equals("2"))
			return 2;
		else return -1;
	}

	// 예매 취소 및 좌석 삭제

	public void cancelRerservation(Ticket ticket) {
		MovieDetail movieDetail = getMovieDetailFromTicket(ticket);

		// ticketInfoList에서 해당 Ticket 객체를 삭제
		FileManager.ticketInfoList.remove(ticket);
		try {
			int[][] seatArray = movieDetail.getSeatArray();
			String seatCode = ticket.getSeatCode();

			// seatCode에서 알파벳과 숫자를 분리
			char alphabet = seatCode.charAt(0);
			String num_str = seatCode.substring(1);

			// 주어진 알파벳과 숫자를 인덱스로 변환
			int row = alphabet - 65;
			int col = Integer.parseInt(num_str) - 1;

			// 해당 좌석을 0으로 설정
			seatArray[row][col] = 0;
		} catch (NullPointerException e) {
			System.out.println("NullPointerException: cancelReservation");
		}
	}

	//
	public static MovieDetail getMovieDetailFromTicket(Ticket ticket) {
		// 영화 이름, 상영관, 상영 시작 시간이 모두 같아야 함
		for (MovieDetail md : FileManager.movieDetailList)
			if (ticket.getMovieName().equals(md.getMovieName()))
				if (getTheaterNumFromTicket(ticket).equals(md.getTheaterNumber()))
					if (getStartTimeFromTicket(ticket).equals(md.getStartTime()))
						return md;
		return null;
	}

	// Ticket 객체로부터 상영관 번호를 얻는 메서드 (ex. "01", "12")
	public static String getTheaterNumFromTicket(Ticket ticket) {
		return ticket.getReservationId().substring(3, 5);
	}

	// Ticket 객체로부터 상영 시작 시간을 얻는 메서드 (ex. "0930", "1600")
	public static String getStartTimeFromTicket(Ticket ticket) {
		return ticket.getReservationId().substring(5, 9);
	}

	public static void main(String[] args) {

	}
}

