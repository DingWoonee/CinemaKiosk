package file;

import entity.*;

import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<Movie> movieList = new ArrayList<>();
    public static List<Seat> seatList = new ArrayList<>();
    public static List<MovieDetail> movieDetailList = new ArrayList<>();
    public static List<Ticket> ticketInfoList = new ArrayList<>();
    public static Manager manager = new Manager("1234");

    public static void saveMovie() {
        String fileContent = "";
        for (Movie movie : movieList) {
            String newLine = "";
            
        }
    }

    public static void saveTicketInfo() {

    }
}
