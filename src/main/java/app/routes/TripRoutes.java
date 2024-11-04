package app.routes;

import app.controller.TripController;
import app.dao.TripDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class TripRoutes {
    private TripController tripController;

    public TripRoutes(EntityManagerFactory emf) {
        tripController = new TripController((emf));
    }

    protected EndpointGroup getRoutes() {
        return () -> {

            get("/", tripController::getAllTrips);

            get("/{id}", tripController::getTripById);

            post("/", tripController::createTrips);

            put("/{id}", tripController::updateTrip);

            delete("/{id}", tripController::deleteTrip);

            put("/{tripId}/guides/{guideId}", tripController::addGuidToTrip);

            post("/populate", tripController::populateDatabase);


        };

    }
}
