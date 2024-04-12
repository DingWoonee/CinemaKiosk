package file;

import entity.Movie;

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
                && checkMovieDetail()
                && checkTicketInfo()
                && checkManagerInfo()) {
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
                /*Movie newMovie = new Movie(elements[0], elements[1], elements[], elements[3]);
                FileManager.movieList.add(newMovie)*/
            }
        } else {
            System.out.println("Movie list file content format does not match");
            return false;
        }
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
                if (checkMovieDataLine(line)) return true;
                //System.out.println(line); // 파일 내용을 한 줄씩 읽어서 출력
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
        if (elements.length == 4) {
            if (!Pattern.matches(String.valueOf(ROOM_NUMBER), elements[0]) ||
                    !Pattern.matches(String.valueOf(SEAT_CHART), elements[1])) {
                System.out.println("Seat info file content format does not match");
                return false;
            }
        } else {
            System.out.println("Seat info file content format does not match");
            return false;
        }
        return true;
    }

    private boolean checkSeatInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(seatInfoFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (checkSeatDataLine(line)) return true;
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
            if (!Pattern.matches(String.valueOf(MOVIE_ORDER), elements[0]) ||
                    !Pattern.matches(String.valueOf(MOVIE_NAME), elements[1]) ||
                    !Pattern.matches(String.valueOf(MOVIE_INFO), elements[2]) ||
                    !Pattern.matches(String.valueOf(ROOM_NUMBER), elements[3]) ||
                    !Pattern.matches(String.valueOf(MOVIE_TIME), elements[4]) ||
                    !Pattern.matches(String.valueOf(SEAT_CHART), elements[5])) {
                System.out.println("Movie detail file content format does not match");
                return false;
            }
        } else {
            System.out.println("Movie detail file content format does not match");
            return false;
        }
        return true;
    }
    public void writeMovieDetailListContents(BufferedWriter bw) {
        try (BufferedReader br = new BufferedReader(new FileReader(movieListFileName))) {
            int i;
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split("\\$");
                String movieName = elements[0];
                String movieInfo = elements[1];
                List<String> roomNumbers = new ArrayList<>();
                roomNumbers.addAll(Arrays.asList(elements[2].split("\\|")));
                String movieTime = elements[3];
                try (BufferedReader br2 = new BufferedReader(new FileReader(seatInfoFileName))) {
                    String[] seatInfo; //상영관 $ 좌석배열
                    List<String> roomArrays = new ArrayList<>();
                    String line2;
                    while ((line2 = br2.readLine()) != null) {
                        seatInfo = line2.split("\\$");
                        for (i = 0; i < roomNumbers.size(); i++) { //상영관[] 순서에 따른 좌석 배열[]
                            if (seatInfo[0].equals(roomNumbers.get(i))) {
                                roomArrays.add(seatInfo[1]);
                            }
                        }
                    }
                }

                for (i = 0; i < roomNumbers.size(); i++) {
                    bw.write(movieOrder + "$");
                    bw.write(movieName + "$");
                    bw.write(movieInfo + "$");
                    bw.write(roomNumbers.get(i) + "$");
                    bw.write(movieTime + "$");
                    bw.write("\n");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeSeatInfoFileContents(BufferedWriter bw) {

    }
    //무비 디테일 디렉토리 안에 날짜에 맞는 파일이 있는지 확인하고 있으면 내용확인 없으면 내용 담은 파일 생성
    private boolean checkMovieDetail() {
        try (BufferedReader br = new BufferedReader(new FileReader(movieDetailListDirectoryName + "/" + FileManager.todayDate))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (checkSeatDataLine(line)) return true;
            }
        } catch (FileNotFoundException e) {
            // 파일이 없을 때 파일 생성하는 부분.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(seatInfoFileName))) {
                System.out.println("Seat info file created");
                writeMovieDetailListContents(bw);
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
        if (elements.length == 3) {
            if (!Pattern.matches(String.valueOf(TICKET_NUMBER), elements[0]) ||
                    !Pattern.matches(String.valueOf(TICKET_PASSWORD), elements[1]) ||
                    !Pattern.matches(String.valueOf(SEAT_NUMBER), elements[2])) {
                System.out.println("Ticket info file content format does not match");
                return false;
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
                if (checkTicketDataLine(line)) return true;
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
            if (!Pattern.matches(String.valueOf(ADMIN_PASSWORD), line)) {
                System.out.println("Manager info file content format does not match");
                return false;
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
                if (checkManagerDataLine(line)) return true;
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
