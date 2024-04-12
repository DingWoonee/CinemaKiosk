package file;

import etc.Prompt;

import java.io.*;

public class FileCheck {
    private final String movieListFileName = "movie_list.txt";
    private final String seatInfoFileName = "seat_info.txt";
    private final String movieDetailListDirectoryName = "movie_detail_list";
    private final String ticketInfoFileName = "ticket_info.txt";
    private final String managerInfoFileName = "manager_info.txt";
    public boolean checkAll() {
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

    // MovieList의 한 줄 한 줄을 검사하는 함수
    public boolean checkMovieDataLine() {
        return true;
    }
    private boolean checkMovieList() {
        try (BufferedReader br = new BufferedReader(new FileReader(movieListFileName))) {
            // 여기서 파일 내용 한줄 한줄 검사하고 FileManager.movieList에 데이터 하나씩 추가.
            // 위의 checkMovieDataLine함수를 이용해서 한 줄 한 줄 검사하는데,
            // 형식에 안 맞는게 한 줄이라도 있으면 바로 return false하면됨.
            // 아래는 일단 테스트로 파일 내용 출력하는 코드임.
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line); // 파일 내용을 한 줄씩 읽어서 출력
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieListFileName))) {
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // SeatInfo의 한 줄 한 줄을 검사하는 함수
    public boolean checkSeatDataLine() {
        return true;
    }
    private boolean checkSeatInfo() {
        return true;
    }


    // MovieDetail의 한 줄 한 줄을 검사하는 함수
    public boolean checkDetailDataLine() {
        return true;
    }
    private boolean checkMovieDetail() {
        return true;
    }


    // TicketInfo의 한 줄 한 줄을 검사하는 함수
    public boolean checkTicketDataLine() {
        return true;
    }
    private boolean checkTicketInfo() {
        return true;
    }


    // ManagerInfo의 한 줄 한 줄을 검사하는 함수
    public boolean checkManagerDataLine() {
        return true;
    }
    private boolean checkManagerInfo() {
        return true;
    }

    public static void main(String[] args) {
        // 테스트를 위한 임시 테스트 main함수
        FileCheck fileCheck = new FileCheck();
        fileCheck.checkMovieList();
    }
}
