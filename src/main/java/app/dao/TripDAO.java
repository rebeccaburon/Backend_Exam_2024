package app.dao;

import app.dto.TripDTO;
import app.entities.Guide;
import app.entities.Trip;
import app.enums.Category;
import app.exceptions.ApiException;
import app.exceptions.JPAException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;

public class TripDAO implements IDAO<TripDTO>, ITripGuideDAO {
    private EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public TripDTO create(TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = new Trip(tripDTO);
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public List<TripDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t", Trip.class);
            return TripDTO.toDTOList(query.getResultList());
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public TripDTO getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new EntityNotFoundException("Trip with id " + id + " not found");
            }
            return new TripDTO(trip);
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public TripDTO update(TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = new Trip(tripDTO);

            Trip existingTrip = em.find(Trip.class, tripDTO.getId());
            if (existingTrip == null) {
                throw new EntityNotFoundException("Trip with id " + trip.getId() + " not found. Updated faild.");
            }
            em.getTransaction().begin();

            if (trip.getStartTime() != null) {
                existingTrip.setStartTime(trip.getStartTime());
            }
            if (trip.getEndTime() != null) {
                existingTrip.setEndTime(trip.getEndTime());
            }
            if (trip.getStartPosition() != null) {
                existingTrip.setStartPosition(trip.getStartPosition());
            }
            if (trip.getName() != null) {
                existingTrip.setName(trip.getName());
            }
            if (trip.getPrice() != 0) {
                existingTrip.setPrice(trip.getPrice());
            }
            if (trip.getCategory() != null) {
                existingTrip.setCategory(trip.getCategory());
            }
            em.getTransaction().commit();
            return new TripDTO(existingTrip);
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }

    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                em.remove(trip);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }

    }

    @Override
    public TripDTO addGuideToTrip(Long tripId, Long guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);

            if (trip != null && guide != null) {
                trip.setGuide(guide);
               guide.getTrips().add(trip);

                em.merge(trip);
                em.merge(guide);

                em.getTransaction().commit();
                return new TripDTO(trip);

            }
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
        return null;
    }

    @Override
    public List<TripDTO> getTripsByGuide(Long guideId) {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<TripDTO> query = em.createQuery("SELECT new app.dto.TripDTO(t) FROM Trip t JOIN FETCH t.guide g WHERE g.id = :guideId ", TripDTO.class);

            query.setParameter("guideId", guideId);

            return query.getResultList();

        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

}



