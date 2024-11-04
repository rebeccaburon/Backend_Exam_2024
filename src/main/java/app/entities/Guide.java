package app.entities;

import app.dto.GuideDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Guide")
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    private String email;
    private String phone;
    @Column(name = "year_of_experience", nullable = false)
    private int yearOfExperience;

    @OneToMany(mappedBy = "guide")
    private List<Trip> trips = new ArrayList<>();


    public Guide (GuideDTO guideDTO){
        this.firstName = guideDTO.getFirstName();
        this.lastName = guideDTO.getLastName();
        this.email = guideDTO.getEmail();
        this.phone = guideDTO.getPhone();
        this.yearOfExperience = guideDTO.getYearOfExperience();

    }
}

