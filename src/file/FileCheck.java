package file;

import etc.Prompt;

import java.io.*;

public class FileCheck {
    private final String movieListFileName = "movie_list.txt";
    private final String seatInfoFileName = "seat_info.txt";
    private final String movieDetailListDirectoryName = "movie_detail_list";
    private final String ticketInfoFileName = "ticket_info.txt";
    private final String managerInfoFileName = "manager_info.txt";
    public boolean ckeckAll() {
        if (checkMovieList()
                && checkSeatInfo()
                && checkMovieDetail()
                && checkTicketInfo()
                && checkManagerInfo()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkMovieList() {
        try (BufferedReader br = new BufferedReader(new FileReader(movieListFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line); // 파일 내용을 한 줄씩 읽어서 출력
            }
        } catch (FileNotFoundException e) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieListFileName))) {
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkSeatInfo() {
        return true;
    }

    private boolean checkMovieDetail() {
        return true;
    }

    private boolean checkTicketInfo() {
        return true;
    }

    private boolean checkManagerInfo() {
        return true;
    }

    public boolean checkAll() {
        return true;
    }
}
