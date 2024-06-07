package entity;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String name;
    private String info;
    private Integer runningTime;

    public Movie(String name, String info, Integer runningTime) {
        this.name = name;
        this.info = info;
        this.runningTime = runningTime;
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

    public Integer getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(Integer runningTime) {
        this.runningTime = runningTime;
    }
}