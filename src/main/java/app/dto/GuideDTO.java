package app.dto;

import app.entities.Guide;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GuideDTO {
    private long id;

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String phone;
    @JsonProperty("year_of_experience")
    private int yearOfExperience;
    @JsonProperty("trips")
    private List<TripDTO> trips;

    public GuideDTO (Guide guide){
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();
        this.yearOfExperience = guide.getYearOfExperience();
        this.trips = TripDTO.toDTOList(guide.getTrips());

    }


    public static List<GuideDTO> toGuideDTOList(List<Guide> guide){
        return guide.stream().map(GuideDTO :: new).toList();
    }
}
