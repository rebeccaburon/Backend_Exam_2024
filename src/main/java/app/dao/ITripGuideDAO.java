package app.dao;

import app.dto.TripDTO;

import java.util.List;
import java.util.Set;

public interface ITripGuideDAO {

    TripDTO addGuideToTrip(Long tripId, Long guideId);

    List<TripDTO> getTripsByGuide(Long guideId);

}
