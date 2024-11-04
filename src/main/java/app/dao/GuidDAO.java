package app.dao;


import app.dto.GuideDTO;
import app.entities.Guide;
import app.exceptions.JPAException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GuidDAO implements IDAO<GuideDTO> {
    private EntityManagerFactory emf;

    public GuidDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = new Guide(guideDTO);
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public List<GuideDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Guide> query = em.createQuery("SELECT g FROM Guide g", Guide.class);
            return GuideDTO.toGuideDTOList(query.getResultList());
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public GuideDTO getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                throw new EntityNotFoundException("Guide with id " + id + " not found");
            }
            return new GuideDTO(guide);
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public GuideDTO update(GuideDTO guideDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = new Guide(guideDTO);

            Guide existingGuide = em.find(Guide.class, guideDTO.getId());
            if (existingGuide == null) {
                throw new EntityNotFoundException("Guide with id " + guideDTO.getId() + " not found. Updated faild.");
            }
            em.getTransaction().begin();

            if (guide.getFirstName() != null) {
                existingGuide.setFirstName(guide.getFirstName());
            }
            if (guide.getLastName() != null) {
                existingGuide.setLastName(guide.getLastName());
            }
            if (guide.getEmail() != null) {
                existingGuide.setEmail(guide.getEmail());
            }
            if (guide.getPhone() != null) {
                existingGuide.setPhone(guide.getPhone());
            }
            if (guide.getYearOfExperience() != 0) {
                existingGuide.setYearOfExperience(guide.getYearOfExperience());
            }
            em.getTransaction().commit();
            return new GuideDTO(existingGuide);
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            if (guide != null) {
                em.remove(guide);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JPAException(400, e.getMessage());
        }

    }
}
