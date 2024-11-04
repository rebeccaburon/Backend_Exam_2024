package app.routes;

import app.Populator.Populator;
import app.config.AppConfig;
import app.config.HibernateConfig;
import app.dao.TripDAO;
import app.dto.TripDTO;
import app.entities.Trip;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.emptyArray;
import static org.junit.jupiter.api.Assertions.*;

class TripRoutesTest {
    private static Javalin app;
    private static EntityManagerFactory emf;
    private static TripDAO tripDAO;
    private String BASE_URL = "http://localhost:7000/api";
    private static Populator populator;


    private List<Trip> trips;

    @BeforeAll
    static void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(emf);
        populator = new Populator(emf);
        tripDAO = new TripDAO(emf);
    }

    @BeforeEach
    void setUp() {
        trips = populator.create5Trips();

    }

    @AfterEach
    void tearDown() {
        populator.cleanup(Trip.class);
    }

    @AfterAll
    static void closeDown() {
        AppConfig.stopServer(app);
    }

    @Test
    void testGetAllTrips() {
        TripDTO[] trips = given()
                .when()
                .get(BASE_URL + "/trips")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO[].class);

        assertThat(trips, not(emptyArray()));
    }

    @Test
    void testGetTripById() {
        TripDTO trip = given()
                .when()
                .get(BASE_URL + "/trips/2")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(trip, notNullValue());
        assertThat(trip.getId(), equalTo(2L));
    }
}