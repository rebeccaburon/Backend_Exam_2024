package app.entities;

import app.dto.GuideDTO;
import app.dto.TripDTO;
import app.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @Column(name = "start_position", nullable = false)
    private String startPosition;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;
    private Category category;
    @ManyToOne
    @JoinColumn(name = "guid_id", nullable = true)
    private Guide guide;


    public Trip(TripDTO tripDTO) {
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.startPosition = tripDTO.getStartPosition();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();

    }

}

