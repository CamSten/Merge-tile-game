package Server.Database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class HighscoreEntry implements Serializable {
    private String name;
    private int points;
    private LocalDateTime date;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    @JsonIgnore
    public LocalDateTime getDate() {
        return date;
    }

    public HighscoreEntry (){

    }
    public HighscoreEntry(String name, int points, LocalDateTime date){
        this.name = name;
        this.points = points;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
    @JsonProperty("date")
    public void setDateString(String dateString) {
        this.date = LocalDateTime.parse(dateString, formatter);
    }
    @JsonProperty("date")
    public String getFormattedDate() {
        return date.format(formatter);
    }

    @Override
    public String toString() {
        return name + ";" + points + ";" + date;
    }
}
