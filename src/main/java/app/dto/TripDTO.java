package app.dto;

import app.entities.Trip;
import app.enums.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TripDTO {
    private long id;
    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_time")
    private LocalTime endTime;
    @JsonProperty("start_position")
    private String startPosition;
    private String name;
    private double price;
    private Category category;
    private GuideDTO guide;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.startPosition = trip.getStartPosition();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.guide = trip.getGuide() != null ? new GuideDTO(trip.getGuide()) : null;


    }

    public TripDTO(LocalTime startTime, LocalTime endTime, String startPosition, String name, double price, Category category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public static List<TripDTO>toDTOList (List<Trip> trips){
        return trips.stream().map(TripDTO::new).toList();
    }
}

