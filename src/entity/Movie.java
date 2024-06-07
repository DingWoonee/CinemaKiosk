package entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class Movie {
    private String name;
    private String info;
    private Integer runningTime;

    public Movie(String name, String info, Integer runningTime) {
        this.name = name;
        this.info = info;
        this.runningTime = runningTime;
    }
}