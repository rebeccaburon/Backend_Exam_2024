package app.populator;

import app.entities.Guide;
import app.entities.Trip;
import app.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Populator {

    private EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Trip> create5Trips() {
        List<Trip> trips = new ArrayList<>();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Create 5 trips with unique attributes
            Trip trip1 = new Trip();
            trip1.setStartTime(LocalTime.of(9, 0));
            trip1.setEndTime(LocalTime.of(11, 0));
            trip1.setStartPosition("Location A");
            trip1.setName("Mountain Adventure");
            trip1.setPrice(99.99);
            trip1.setCategory(Category.FOREST);

            Trip trip2 = new Trip();
            trip2.setStartTime(LocalTime.of(10, 0));
            trip2.setEndTime(LocalTime.of(12, 0));
            trip2.setStartPosition("Location B");
            trip2.setName("City Tour");
            trip2.setPrice(59.99);
            trip2.setCategory(Category.CITY);

            Trip trip3 = new Trip();
            trip3.setStartTime(LocalTime.of(14, 0));
            trip3.setEndTime(LocalTime.of(16, 0));
            trip3.setStartPosition("Location C");
            trip3.setName("River Cruise");
            trip3.setPrice(79.99);
            trip3.setCategory(Category.SEA);

            Trip trip4 = new Trip();
            trip4.setStartTime(LocalTime.of(8, 0));
            trip4.setEndTime(LocalTime.of(10, 0));
            trip4.setStartPosition("Location D");
            trip4.setName("Desert Safari");
            trip4.setPrice(129.99);
            trip4.setCategory(Category.SAFARI);

            Trip trip5 = new Trip();
            trip5.setStartTime(LocalTime.of(15, 0));
            trip5.setEndTime(LocalTime.of(17, 0));
            trip5.setStartPosition("Location E");
            trip5.setName("Historical Sites");
            trip5.setPrice(49.99);
            trip5.setCategory(Category.CITY);

            // Persist trips in database
            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);

            em.getTransaction().commit();

            // Add trips to the list
            trips.add(trip1);
            trips.add(trip2);
            trips.add(trip3);
            trips.add(trip4);
            trips.add(trip5);

        } finally {
            em.close();
        }

        return trips;
    }


    public List<Guide> create5Guides() {
        List<Guide> guides = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Create 5 guides with unique attributes
            Guide guide1 = new Guide();
            guide1.setFirstName("John");
            guide1.setLastName("Doe");
            guide1.setEmail("john.doe@example.com");
            guide1.setPhone("123-456-7890");
            guide1.setYearOfExperience(5);

            Guide guide2 = new Guide();
            guide2.setFirstName("Jane");
            guide2.setLastName("Smith");
            guide2.setEmail("jane.smith@example.com");
            guide2.setPhone("234-567-8901");
            guide2.setYearOfExperience(3);

            Guide guide3 = new Guide();
            guide3.setFirstName("Bob");
            guide3.setLastName("Brown");
            guide3.setEmail("bob.brown@example.com");
            guide3.setPhone("345-678-9012");
            guide3.setYearOfExperience(7);

            Guide guide4 = new Guide();
            guide4.setFirstName("Alice");
            guide4.setLastName("Johnson");
            guide4.setEmail("alice.johnson@example.com");
            guide4.setPhone("456-789-0123");
            guide4.setYearOfExperience(2);

            Guide guide5 = new Guide();
            guide5.setFirstName("Charlie");
            guide5.setLastName("Davis");
            guide5.setEmail("charlie.davis@example.com");
            guide5.setPhone("567-890-1234");
            guide5.setYearOfExperience(4);

            // Persist guides in database
            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);
            em.persist(guide4);
            em.persist(guide5);

            em.getTransaction().commit();

            // Add guides to the list
            guides.add(guide1);
            guides.add(guide2);
            guides.add(guide3);
            guides.add(guide4);
            guides.add(guide5);

        } finally {
            em.close();
        }

        return guides;
    }



    public void cleanup(Class<?> entityClass) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();

            em.createNativeQuery("ALTER SEQUENCE " + entityClass.getSimpleName() + "_id_seq RESTART WITH 1").executeUpdate();

            em.getTransaction().commit();
        }
    }

}


