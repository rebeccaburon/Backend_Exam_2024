package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;


public class Routes {
    private TripRoutes tripRoutes;


    public Routes(EntityManagerFactory emf) {
    tripRoutes = new TripRoutes(emf);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/trips", tripRoutes.getRoutes());

        };
    }
}