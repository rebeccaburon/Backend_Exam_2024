package app.dao;

import app.dto.TripDTO;

import java.util.List;
import java.util.Set;

public interface ITripGuideDAO<T> {
    T create(T t);

    List<T> getAll();

    T getById(Long id);


    T update(T t);

    void delete(Long id);

    void addGuideToTrip(Long tripId, Long guideId);

    List<T> getTripsByGuide(Long guideId);

}
