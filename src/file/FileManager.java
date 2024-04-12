package file;

import entity.*;
import etc.Prompt;
import etc.RE;

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
    public static Manager manager = new Manager("1234");

    private final static String movieListFileName = "movie_list.txt";
    private final static String ticketInfoFileName = "ticket_info.txt";

    public static void inputDate() {
        Pattern pattern = Pattern.compile(String.valueOf(RE.DATE_EIGHT.getValue()));
        String input;
        Matcher matcher;
        while (true) {
            System.out.print("오늘 날짜 입력:");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                input = br.readLine();
                matcher = pattern.matcher(input);
                if (matcher.matches()) {
                    break;
                } else {
                    System.out.println(Prompt.BAD_INPUT.getPrompt());
                }
            } catch (IOException ignored) {
            }
        }
        todayDate = input;
    }

    // 영화 리스트 저장.
    // 저장 성공 시 true, 실패 시 false 반환.
    public static boolean saveMovie() {
        String fileContent = "";
        for (Movie movie : movieList) {
            String newLine = movie.getName() + "$" + movie.getInfo() + "$";
            for (int i = 0; i < movie.getTheaterNumList().size(); i++) {
                String theater = movie.getTheaterNumList().get(i);
                if (i == movie.getTheaterNumList().size() - 1) {
                    newLine += (theater);
                } else {
                    newLine += (theater + "|");
                }
            }
            newLine += "$" + movie.getTime() + "\n";
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
            String newLine = ticket.getReservationId() + "$" + ticket.getReservationPw() + "$" + ticket.getDetailId() + "$" + ticket.getSeatCode() + "\n";

            fileContent += newLine;
        }
        // fileContent 변수의 내용을 movie_list.txt에 덮어씀
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ticketInfoFileName))) {
            bw.write(fileContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            List<String> a = new ArrayList<>();
            a.add("01");
            a.add("02");
            Movie movie = new Movie("이름", "재밌어요", a, MovieTime.Time1);
            movieList.add(movie);
        }
        //saveMovie();
        System.out.println(RE.DATE_EIGHT.getValue());

        FileManager.inputDate();
        System.out.println(todayDate);
    }
}
