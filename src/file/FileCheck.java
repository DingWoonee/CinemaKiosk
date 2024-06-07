package file;

import entity.*;
import etc.RE;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static etc.RE.*;

public class FileCheck {
    private final String movieListFileName = "movie_list.txt";
    private final String seatInfoFileName = "seat_info.txt";
    private static final String movieDetailListDirectoryName = "movie_detail_list";
    private final String ticketInfoDirectoryName = "ticket_info";
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
        if(elements.length == 3) {
            if(!Pattern.matches(MOVIE_NAME.getValue(),elements[0])||
                    !Pattern.matches(MOVIE_INFO.getValue(),elements[1])||
                    !Pattern.matches(RUNNING_TIME.getValue(),elements[2])){
                System.out.println("Movielistfilecontentformatdoesnotmatch1");
                return false;
            } else {
                Movie newMovie = new Movie(elements[0],elements[1],Integer.parseInt(elements[2]));
                FileManager.movieList.add(newMovie);
            }
        } else {
            System.out.println("Movie list file content format does not match2");
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
                    !Pattern.matches(SCHEDULE.getValue(), elements[3]) ||
                    !Pattern.matches(RUNNING_TIME.getValue(), elements[4]) ||
                    !Pattern.matches(SEAT_CHART.getValue(), elements[5])) {
                System.out.println("Movie detail file content format does not match");
                return false;
            } else {
                MovieDetail movieDetail = new MovieDetail(Integer.parseInt(elements[0]), elements[1], elements[2], elements[3], Integer.parseInt(elements[4]), Seat.stringToArray(elements[5]));
                FileManager.movieDetailList.add(movieDetail);
            }
        } else {
            System.out.println("Movie detail file content format does not match");
            return false;
        }
        return true;
    }

    //무비 디테일 디렉토리 안에 날짜에 맞는 파일이 있는지 확인하고 있으면 내용확인 없으면 내용 담은 파일 생성
    private boolean checkMovieDetail() {
        try (BufferedReader br = new BufferedReader(new FileReader(movieDetailListDirectoryName + "/" + FileManager.todayDate + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkDetailDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            return true;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    // TicketInfo의 한 줄 한 줄을 검사하는 함수
    public boolean checkTicketDataLine(String line) {
        String[] elements = line.split("\\$"); //예매번호, 예매비밀번호, 영화이름, 좌석번호
        if (elements.length == 4) {
            if (!Pattern.matches(TICKET_NUMBER.getValue(), elements[0]) ||
                    !Pattern.matches(TICKET_PASSWORD.getValue(), elements[1]) ||
                    !Pattern.matches(MOVIE_NAME.getValue(), elements[2]) ||
                    !Pattern.matches(SEAT_NUMBER.getValue(), elements[3])) {
                System.out.println("Ticket info file content format does not match");
                return false;
            } else {
                Ticket newTicket = new Ticket(elements[0], elements[1], elements[2], elements[3]);
                FileManager.ticketInfoList.add(newTicket);
            }
        } else {
            System.out.println("Ticket info file content format does not match");
            return false;
        }
        return true;
    }
    private boolean checkTicketInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(ticketInfoDirectoryName + "/" + FileManager.todayDate + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!checkTicketDataLine(line)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            return true;
            } catch (IOException e2) {
                e2.printStackTrace();
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
            //관리자 정보 파일 생성 후 초기 비밀번호 입력 받기
            Scanner scanner = new Scanner(System.in);
            String password;
            do {
                System.out.print("관리자 비밀번호 설정: ");
                password = scanner.nextLine();
            } while (!FileManager.validateInputReturnBoolWithRE(password, ADMIN_PASSWORD.getValue()));
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(managerInfoFileName))) {
                System.out.println("Manager info file created");
                bw.write(password);
                System.out.println("Save");
                FileManager.manager.setManagerPw(password);
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

    public static List<MovieDetail> getMovieDetail(String date) {
        List<MovieDetail> movieDetailList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(movieDetailListDirectoryName + "/" + date + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split("\\$");
                if (elements.length == 6) {
                    if (!Pattern.matches(MOVIE_ORDER.getValue(), elements[0]) ||
                            !Pattern.matches(MOVIE_NAME.getValue(), elements[1]) ||
                            !Pattern.matches(MOVIE_INFO.getValue(), elements[2]) ||
                            !Pattern.matches(SCHEDULE.getValue(), elements[3]) ||
                            !Pattern.matches(RUNNING_TIME.getValue(), elements[4]) ||
                            !Pattern.matches(SEAT_CHART.getValue(), elements[5])) {
                        System.out.println(date + " Movie detail file content format does not match");
                    }
                    MovieDetail movieDetail = new MovieDetail(Integer.parseInt(elements[0]), elements[1], elements[2], elements[3], Integer.parseInt(elements[4]), Seat.stringToArray(elements[5]));
                    movieDetailList.add(movieDetail);
                }
            }
            return movieDetailList;
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // 테스트를 위한 임시 테스트 main함수
        FileCheck fileCheck = new FileCheck();
        fileCheck.checkAll();
    }
}
