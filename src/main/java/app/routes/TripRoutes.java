package app.routes;

import app.controller.TripController;
import app.dao.TripDAO;
import app.security.enums.Role;
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
            //Adding security
            get("/", tripController::getAllTrips, Role.ANYONE);

            // with added additional data from API
            get("/{id}", tripController::getTripById, Role.ADMIN);

            //Adding security
            post("/", tripController::createTrips, Role.ADMIN);

            put("/{id}", tripController::updateTrip, Role.ADMIN);

            delete("/{id}", tripController::deleteTrip, Role.ADMIN);

            put("/{tripId}/guides/{guideId}", tripController::addGuidToTrip);

            post("/populate", tripController::populateDatabase);

            //adding getTripsByGuid
            get("/guides/{guideId}", tripController :: getTripsByGuide);

            //adding getTripsByCategory
            get("/category/{category}", tripController :: getTripsByCategory, Role.USER);

            //adding getTotalPriceByGuid
            get("/guides/total-price/{guideId}", tripController :: getTotalPriceByGuid);

            //adding getPackingItemsWeightSum
            get("/packing-weight/{id}", tripController :: getPackingItemsWeightSum, Role.ADMIN);


        };

    }
}
