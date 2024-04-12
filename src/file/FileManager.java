package file;

import entity.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    }
}
