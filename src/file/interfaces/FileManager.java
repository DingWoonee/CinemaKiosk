package file.interfaces;

import entity.*;

import java.util.ArrayList;
import java.util.List;

public interface FileManager {
    public static List<Movie> movieList = new ArrayList<>();
    public static List<Seat> seatList = new ArrayList<>();
    public static List<MovieDetail> detailList = new ArrayList<>();
    public static List<Ticket> ticketInfoList = new ArrayList<>();
    public static Manager manager = new Manager();
}
