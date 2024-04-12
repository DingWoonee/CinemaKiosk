package file;

import entity.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import static etc.RE.*;

public class FileCheck {
    private final String movieListFileName = "movie_list.txt";
    private final String seatInfoFileName = "seat_info.txt";
    private final String movieDetailListDirectoryName = "movie_detail_list";
    private final String ticketInfoFileName = "ticket_info.txt";
    private final String managerInfoFileName = "manager_info.txt";
    public int movieOrder = 1;
    public boolean checkAll() {
        if (checkMovieList()
                && checkSeatInfo()
                && checkTicketInfo()
                && checkManagerInfo()
                && checkMovieDetail()) {
            return true;
        } else {
            return false;
        }
    }

    // MovieList의 한 줄 한 줄을 검사하는 함수
    public boolean checkMovieDataLine(String line) {
        String[] elements = line.split("\\$"); //영화이름, 영화정보, 상영관, 상영시간
        if (elements.length == 4) {
            if (!Pattern.matches(MOVIE_NAME.getValue(), elements[0]) ||
                    !Pattern.matches(MOVIE_INFO.getValue(), elements[1]) ||
                    !Pattern.matches(ROOM_NUMBERS.getValue(), elements[2]) ||
                    !Pattern.matches(MOVIE_TIME.getValue(), elements[3])) {
                System.out.println("Movie list file content format does not match");
                return false;
            } else {
                String[] theaters = elements[2].split("\\|");
                List<String> theaterNumList = new ArrayList<>(Arrays.asList(theaters));
                Movie newMovie = new Movie(elements[0], elements[1], theaterNumList, MovieTime.getMovieTime(elements[3]));
                FileManager.movieList.add(newMovie);
            }
        } else {
            System.out.println("Movie list file content format does not match");
            return false;
        }
        return true;
    }
    private boolean checkMovieList() {
        try (BufferedReader br = new BufferedReader(new FileReader(movieListFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkMovieDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieListFileName))) {
                System.out.println("Movie list file created");
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
    public boolean checkSeatDataLine(String line) {
        String[] elements = line.split("\\$"); //상영관, 좌석배열
        if (elements.length == 2) {
            if (!Pattern.matches(ROOM_NUMBER.getValue(), elements[0]) ||
                    !Pattern.matches(SEAT_CHART.getValue(), elements[1])) {
                System.out.println("Seat info file content format does not match");
                return false;
            } else {
                String[] rows = elements[1].split("\\|");
                String[] colForCount = rows[0].split(":");
                int[][] seatArray = new int[rows.length][colForCount.length - 1];
                for (int i = 0; i < rows.length; i++) {
                    String[] cols = rows[i].split(":");
                    for (int j = 1; j < cols.length; j++) {
                        seatArray[i][j - 1] = Integer.parseInt(cols[j]);
                    }
                }
                Seat seat = new Seat(elements[0], seatArray);

                FileManager.seatList.add(seat);
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean checkSeatInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(seatInfoFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkSeatDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(seatInfoFileName))) {
                System.out.println("Seat info file created");
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


    // MovieDetail의 한 줄 한 줄을 검사하는 함수
    public boolean checkDetailDataLine(String line) {
        String[] elements = line.split("\\$"); //상영순서, 영화이름, 영화정보, 상영관, 상영시간, 좌석배열
        if (elements.length == 6) {
            if (!Pattern.matches(MOVIE_ORDER.getValue(), elements[0]) ||
                    !Pattern.matches(MOVIE_NAME.getValue(), elements[1]) ||
                    !Pattern.matches(MOVIE_INFO.getValue(), elements[2]) ||
                    !Pattern.matches(ROOM_NUMBER.getValue(), elements[3]) ||
                    !Pattern.matches(MOVIE_TIME.getValue(), elements[4]) ||
                    !Pattern.matches(SEAT_CHART.getValue(), elements[5])) {
                System.out.println("Movie detail file content format does not match");
                return false;
            }
        } else {
            System.out.println("Movie detail file content format does not match");
            return false;
        }
        return true;
    }
    public boolean writeMovieDetailListContents(BufferedWriter bw) throws IOException {
        for (Movie movie : FileManager.movieList) {
            for (String theaterNum : movie.getTheaterNumList()) {
                Seat seat = null;
                for (Seat seat1 : FileManager.seatList) {
                    if (seat1.getTheaterNum().equals(theaterNum)) {
                        seat = seat1;
                    }
                }
                // 해당 상영관이 FileManager.seatList에 없을 경우 종료
                if (seat == null) {
                    return false;
                }
                // 이미 동시에 중복되는 상영관과 상영시간이 있을 경우 종료
                for (MovieDetail movieDetail : FileManager.movieDetailList) {
                    if (movieDetail.getTime().equals(movie.getTime()) && movieDetail.getTheaterNum().equals(theaterNum)) {
                        return false;
                    }
                }
                MovieDetail newMovieDetail = new MovieDetail(
                        0,
                        movie.getName(),
                        movie.getInfo(),
                        theaterNum,
                        movie.getTime(),
                        Arrays.copyOf(seat.getSeatArray(), seat.getSeatArray().length)
                );
                FileManager.movieDetailList.add(newMovieDetail);
                String[] timeList = {"조조", "미들", "심야"};
                FileManager.movieDetailList.sort((a, b) -> {
                    return Arrays.binarySearch(timeList, a.getTime().getTime()) - Arrays.binarySearch(timeList, b.getTime().getTime());
                });
                for (int i = 0; i < FileManager.movieDetailList.size(); i++) {
                    FileManager.movieDetailList.get(i).setDetailId(i);
                }
                String newLine = "";
                for (MovieDetail movieDetail : FileManager.movieDetailList) {
                    newLine += movieDetail.getDetailId() + "$" + movieDetail.getName() + "$" + movieDetail.getInfo() + "$" + movieDetail.getTheaterNum() + "$" + movieDetail.getTime().getTime() + "$" + movieDetail + "$" + Seat.seatToString(movieDetail.getSeatArray()) + "\n";
                }
                bw.write(newLine);
            }
        }
        return true;
    }
    //무비 디테일 디렉토리 안에 날짜에 맞는 파일이 있는지 확인하고 있으면 내용확인 없으면 내용 담은 파일 생성
    private boolean checkMovieDetail() {
        try (BufferedReader br = new BufferedReader(new FileReader(movieDetailListDirectoryName + "/" + FileManager.todayDate))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkDetailDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieDetailListDirectoryName + "/" + FileManager.todayDate))) {
                System.out.println("Seat info file created");
                if (!writeMovieDetailListContents(bw)) {
                    return false;
                }
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


    // TicketInfo의 한 줄 한 줄을 검사하는 함수
    public boolean checkTicketDataLine(String line) {
        String[] elements = line.split("\\$"); //예매번호, 예매비밀번호, 좌석번호
        if (elements.length == 4) {
            if (!Pattern.matches(TICKET_NUMBER.getValue(), elements[0]) ||
                    !Pattern.matches(TICKET_PASSWORD.getValue(), elements[1]) ||
                    !Pattern.matches(MOVIE_ORDER.getValue(), elements[2]) ||
                    !Pattern.matches(SEAT_NUMBER.getValue(), elements[3])) {
                System.out.println("Ticket info file content format does not match");
                return false;
            } else {
                Ticket newTicket = new Ticket(elements[0], elements[1], Integer.parseInt(elements[2]), elements[3]);
                FileManager.ticketInfoList.add(newTicket);
            }
        } else {
            System.out.println("Ticket info file content format does not match");
            return false;
        }
        return true;
    }
    private boolean checkTicketInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(ticketInfoFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkTicketDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ticketInfoFileName))) {
                System.out.println("Ticket info file created");
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


    // ManagerInfo의 한 줄 한 줄을 검사하는 함수
    public boolean checkManagerDataLine(String line) {
        if (line != null) {
            if (!Pattern.matches(ADMIN_PASSWORD.getValue(), line)) {
                System.out.println("Manager info file content format does not match");
                return false;
            } else {
                FileManager.manager.setManagerPw(line);
            }
        } else {
            System.out.println("Manager info file content format does not match");
            return false;
        }
        return true;
    }
    private boolean checkManagerInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(managerInfoFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkManagerDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(managerInfoFileName))) {
                System.out.println("Manager info file created");
                //관리자 정보 파일 생성 후 초기 비밀번호 입력 받기
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter admin password: ");
                String password = scanner.nextLine();
                bw.write(password);
                System.out.println("Save");
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

    public static void main(String[] args) {
        // 테스트를 위한 임시 테스트 main함수
        FileCheck fileCheck = new FileCheck();
        fileCheck.checkAll();
    }
}
