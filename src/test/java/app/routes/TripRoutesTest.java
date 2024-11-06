package app.routes;


import app.enums.Category;
import app.populator.Populator;
import app.config.AppConfig;
import app.config.HibernateConfig;
import app.dao.TripDAO;
import app.dto.TripDTO;
import app.entities.Trip;
import app.security.controller.SecurityController;
import app.security.daos.SecurityDAO;
import app.security.dtos.UserDTO;
import app.security.entities.User;
import app.security.exceptions.ValidationException;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.emptyArray;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TripRoutesTest {
    private static Javalin app;
    private static EntityManagerFactory emf;
    private static TripDAO tripDAO;
    private final String BASE_URL = "http://localhost:7000/api";
    private static Populator populator;

    //adding security
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static SecurityDAO securityDAO;
    private static SecurityController securityController;

    private Trip t1;
    private List<Trip> trips;

    @BeforeAll
    static void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(emf);
        populator = new Populator(emf);
        tripDAO = new TripDAO(emf);
        securityDAO = new SecurityDAO(emf);
        securityController = SecurityController.getInstance();
    }

    @BeforeEach
    void setUp() {
        trips = populator.create5Trips();
        t1 = trips.get(0);


        //Adding security
        UserDTO[] users = Populator.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, userDTO.getUsername());
            System.out.println("User " + userDTO.getUsername() + "was found");
        }
        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        }
        catch (ValidationException e) {
            throw new RuntimeException(e);
        }


    }

    @AfterEach
    void tearDown() {
        populator.cleanup(Trip.class);
        populator.cleanUpUsers();
    }

    @AfterAll
    static void closeDown() {
        AppConfig.stopServer(app);
    }

    //Test for fetching all trips
    @Test
    void testGetAllTrips() {
        TripDTO[] trips = given()
                .when()
                .header("Authorization", adminToken, userToken)
                .get(BASE_URL + "/trips")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO[].class);

        assertThat(trips, not(emptyArray()));
    }

    //Test for fetching a trip by its ID
    @Test
    void testGetTripById() {
        TripDTO trip = given()
                .when()
                .header("Authorization", adminToken)
                .get(BASE_URL + "/trips/2")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(trip, notNullValue());
        assertThat(trip.getId(), equalTo(2L));
        assertThat(trip.getPackingItems(), not(empty())); // Verify packing items are present
    }

    // Test for creating a new trip
    @Test
    void testCreateTrip() {
        TripDTO[] tripArray = {new TripDTO(LocalTime.of(8, 0), LocalTime.of(17, 00), "85th street", "Central Station", 50.00, Category.CITY)};

        TripDTO[] createdTrips = given()
                .contentType("application/json")
                .body(tripArray)
                .when()
                .header("Authorization", adminToken)
                .post(BASE_URL + "/trips")
                .then()
                .statusCode(201)
                .extract()
                .as(TripDTO[].class);

        assertThat(createdTrips, notNullValue());
        assertThat(createdTrips[0].getName(), is(tripArray[0].getName()));
    }

    // Test for updating an existing trip
    @Test
    void testUpdateTrip() {
        TripDTO updatedTrip = new TripDTO(t1);
        updatedTrip.setName("UPDATED NAME");
        updatedTrip.setCategory(Category.LAKE);


        TripDTO result = given()
                .contentType("application/json")
                .body(updatedTrip)
                .when()
                .header("Authorization", adminToken)
                .put(BASE_URL + "/trips/3")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(result, notNullValue());
        assertThat(result.getName(), equalTo("UPDATED NAME"));
        assertThat(result.getCategory(), equalTo(Category.LAKE));
    }

    // Test for deleting a trip by ID
    @Test
    void testDeleteTrip() {
        given()
                .when()
                .header("Authorization", adminToken)
                .delete(BASE_URL + "/trips/1")
                .then()
                .statusCode(204);

        assertThat(tripDAO.getAll(), hasSize(4));
        assertThat(tripDAO.getAll().get(0).getId(), equalTo(2L));
    }


    // Test for getting trips by category
    @Test
    void testGetTripsByCategory() {
        TripDTO[] tripsByCategory = given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/trips/category/SEA")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO[].class);

        assertThat(tripsByCategory, not(emptyArray()));

    }


    // Test for getting the packing items' weight sum for a trip
    @Test
    void testGetPackingItemsWeightSum() {
        Float weightSum = given()
                .when()
                .header("Authorization", adminToken)
                .get(BASE_URL + "/trips/packing-weight/2")
                .then()
                .statusCode(200)
                .extract()
                .as(Float.class);

        assertThat(weightSum, greaterThan(0.0f));
    }
    // Test for assigning a guide to a trip
    // Test getting Total price by Guide Id
    // Test for getting trips by guide ID
}
