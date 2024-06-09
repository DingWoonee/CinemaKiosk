package file;

import entity.*;
import etc.Prompt;
import etc.RE;
import reservation.InputRetryException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {
    public static String todayDate = "";
    public static List<Movie> movieList = new ArrayList<>();
    public static List<Seat> seatList = new ArrayList<>();
    public static List<MovieDetail> movieDetailList = new ArrayList<>();
    public static List<Ticket> ticketInfoList = new ArrayList<>();
    public static Manager manager = new Manager();

    private final static String movieListFileName = "movie_list.txt";
    private final static String ticketInfoFolder = "ticket_info";
    private final static String movieDetailFolder = "movie_detail_list";

    public static boolean isTheaterNumExist(String num) {
        for (Seat seat : seatList) {
            if (seat.getTheaterNum().equals(num)) {
                return true;
            }
        }
        return false;
    }


    public static void inputDate() {
        Pattern pattern;
        String input;
        String trimmedInput;
        Matcher matcher;
        while (true) {
            System.out.print("오늘 날짜 입력: ");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                input = br.readLine();
                trimmedInput = input.replaceAll("\\s+", "");
                if (validateInputReturnBoolWithRE(trimmedInput, RE.NUM_EIGHT.getValue())) {
                    int year = Integer.parseInt(trimmedInput.substring(0, 4));
                    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) { // 윤년
                        pattern = Pattern.compile(String.valueOf(RE.DATE_EIGHT2.getValue()));
                    } else { // 윤년 아님
                        pattern = Pattern.compile(String.valueOf(RE.DATE_EIGHT.getValue()));
                    }
                    matcher = pattern.matcher(trimmedInput);
                    if (matcher.matches()) {
                        break;
                    } else {
                        System.out.println(Prompt.BAD_INPUT.getPrompt());
                    }
                }
            } catch (IOException ignored) {
            }
        }
        todayDate = trimmedInput;
    }

    // 영화 리스트 저장.
    // 저장 성공 시 true, 실패 시 false 반환.
    public static boolean saveMovie() {
        String fileContent = "";
        for (Movie movie : movieList) {
            String newLine = movie.getName() + "$" + movie.getInfo() + "$" + movie.getRunningTime() + "\n";
            fileContent += newLine;
        }
        // fileContent 변수의 내용을 movie_list.txt에 덮어씀
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieListFileName))) {
            bw.write(fileContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveTicketInfo() {
        String fileContent = "";
        for (Ticket ticket : ticketInfoList) {
            String newLine = ticket.getReservationId() + "$" + ticket.getReservationPw() + "$" + ticket.getMovieName() + "$" + ticket.getSeatCode() + "\n";

            fileContent += newLine;
        }
        // fileContent 변수의 내용을 movie_list.txt에 덮어씀
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ticketInfoFolder + "/" + todayDate + ".txt"))) {
            bw.write(fileContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveMovieDetail() {
        String fileContent = "";
        for (MovieDetail movieDetail : movieDetailList) {
            String newLine = movieDetail.getDetailId() + "$" + movieDetail.getMovieName() + "$" + movieDetail.getMovieInfo() + "$" + movieDetail.getSchedule() + "$" + movieDetail.getRunningTime() + "$" + Seat.seatToString(movieDetail.getSeatArray());

            fileContent += newLine;
        }
        // fileContent 변수의 내용을 movie_list.txt에 덮어씀
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieDetailFolder + "/" + todayDate + ".txt"))) {
            bw.write(fileContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveMovieDetail2(String date, List<MovieDetail> movieDetailList) {
        String fileContent = "";
        for (MovieDetail movieDetail : movieDetailList) {
            String newLine = movieDetail.getDetailId() + "$" + movieDetail.getMovieName() + "$" + movieDetail.getMovieInfo() + "$" + movieDetail.getSchedule() + "$" + movieDetail.getRunningTime() + "$" + Seat.seatToString(movieDetail.getSeatArray());

            fileContent += newLine;
        }
        // fileContent 변수의 내용을 movie_list.txt에 덮어씀
        // 파일과 디렉토리가 없을 경우 생성
        try {
            File directory = new File(movieDetailFolder);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리가 없으면 생성
            }

            File file = new File(movieDetailFolder + "/" + date + ".txt");
            if (!file.exists()) {
                file.createNewFile(); // 파일이 없으면 생성
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(fileContent.toString());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 정규 표현식과 input을 비교하는 메소드
    public static void validateInputWithRE(String input, String regexExpression) throws InputRetryException {
        if (!input.matches(regexExpression)) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            throw new InputRetryException("잘못된 입력입니다. 다시 입력하세요.");
        }
    }
    public static boolean validateInputReturnBoolWithRE(String input, String regexExpression) throws InputRetryException {
        if (!input.matches(regexExpression)) {
            System.out.println(Prompt.BAD_INPUT.getPrompt());
            return false;
        } else {
            return true;
        }
    }
}
