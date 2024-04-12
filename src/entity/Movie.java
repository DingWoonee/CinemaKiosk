package entity;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String name;
    private String info;
    private List<String> theaterNumList;
    private MovieTime time;

    public Movie() {
        this.theaterNumList = new ArrayList<>();
    }

    public Movie(String name, String info, List<String> theaterNumList, MovieTime time) {
        this.name = name;
        this.info = info;
        this.theaterNumList = theaterNumList;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<String> getTheaterNumList() {
        return theaterNumList;
    }

    public void setTheaterNumList(List<String> theaterNum) {
        this.theaterNumList = theaterNum;
    }

    public MovieTime getTime() {
        return time;
    }

    public void setTime(MovieTime time) {
        this.time = time;
    }
}