package app.controller;

import app.Populator.Populator;
import app.dao.TripDAO;
import app.dto.GuideDTO;
import app.dto.TripDTO;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class TripController {
    private TripDTO tripDTO;
    private TripDAO tripDAO;
    private Populator populator;

    public TripController(EntityManagerFactory emf) {
        this.tripDAO = tripDAO = new TripDAO(emf);
        this.populator = new Populator(emf);
    }

    //adding a Populator method

    public void populateDatabase(Context ctx) {
        try{
            List<TripDTO> tripsList = TripDTO.toDTOList(populator.create5Trips());
            List<GuideDTO> guidsList = GuideDTO.toGuideDTOList(populator.create5Guides());

            populator.create5Trips();
            populator.create5Guides();

            if (!tripsList.isEmpty() && !guidsList.isEmpty()) {
                ctx.status(201);
                ctx.json(tripsList);
                ctx.json(guidsList);
                ctx.result("The following trips and guids were created successfully.");
            }

        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    public void createTrips(Context ctx) {
        try{
            TripDTO[] newTrips = ctx.bodyAsClass(TripDTO[].class);
            TripDTO[] savedTrips = new TripDTO[newTrips.length];

            int i = 0;
            for (TripDTO trip : newTrips) {
                TripDTO savedTrip = tripDAO.create(trip);
                savedTrips[i] = savedTrip;
                i++;
            }
            ctx. res().setStatus(201);
            ctx.json(savedTrips, TripDTO.class);
        } catch(Exception e){
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getAllTrips(Context ctx) {
        List<TripDTO> listofTrips = tripDAO.getAll();

        try{
            if (listofTrips.isEmpty()){
                throw new ApiException(204, "No CONCTENT! No Trips was found in the database" );
            } else {
                ctx.status(200);
                ctx.json(listofTrips);
            }
        }catch (ApiException e){
            throw new ApiException(400, e.getMessage());
        }
    }
    public void getTripById(Context ctx) {
        Long tripId = Long.parseLong(ctx.pathParam("id"));

        tripDTO = tripDAO.getById(tripId);

        try{
            if (tripDTO != null){
                ctx.status(200);
                ctx.json(tripDTO);
            } else {
                throw new ApiException(204, "No CONTEN! Trip  with id " + tripId + " was not found");
            }
        } catch (ApiException e){
            throw new ApiException(400, e.getMessage());
        }
    }
    public void updateTrip(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("id"));
            TripDTO tripDTOUpdate = ctx.bodyAsClass(TripDTO.class);
            tripDTOUpdate.setId(tripId);

            TripDTO updatedTrip = tripDAO.update(tripDTOUpdate);
            ctx.res().setStatus(200);
            ctx.json(updatedTrip, TripDTO.class);
        } catch (EntityNotFoundException e) {
            throw new ApiException(400, e.getMessage());

        }
    }
    public void deleteTrip(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("id"));
            tripDAO.delete(tripId);
            ctx.status(204);
            ctx.result("Trip is deleted successfully");
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }
    public void addGuidToTrip(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("tripId"));

            Long guideId = Long.parseLong(ctx.pathParam("guideId"));

            tripDAO.addGuideToTrip(tripId, guideId);

            if ( tripId!= 0 && guideId != 0 ) {
                ctx.status(200);
                ctx.json("Guid with Id " + guideId + "added to Trip, with id " + tripId);
            } else {
                throw new ApiException(204, "NO CONTENT! Trip with id "+ tripId +" was not added");
            }

        } catch (ApiException e) {
            throw new ApiException(400, e.getMessage());
        }
    }
    public void getTripsByGuide(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("tripId"));
            List<TripDTO> tripsByGuid = tripDAO.getTripsByGuide(tripId);
            if (tripsByGuid.isEmpty()) {
                throw new ApiException(204, "No Content! No Trips by Guid, with id " + tripId + " was  found");
            } else {
                ctx.status(200);
                ctx.json(tripsByGuid);
            }
        } catch (ApiException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

}
