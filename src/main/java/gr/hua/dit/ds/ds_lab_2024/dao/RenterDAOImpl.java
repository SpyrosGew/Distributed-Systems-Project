package gr.hua.dit.ds.ds_lab_2024.dao;
import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import gr.hua.dit.ds.ds_lab_2024.entities.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RenterDAOImpl implements RenterDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Application> getRentersApplications(int id) {
        TypedQuery<Application> query = entityManager.createQuery(
                "SELECT a FROM Application a JOIN a.renter r WHERE r.id = :id", Application.class
        );
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Property> getAcceptedProperties(int renterId) {

        TypedQuery<Application> query = entityManager.createQuery(
                "SELECT a FROM Application a JOIN a.renter r WHERE r.id = :renterId AND a.status = :status", Application.class
        );
        query.setParameter("renterId", renterId);
        query.setParameter("status", Status.ACCEPTED);

        List<Application> acceptedApplications = query.getResultList();

        return acceptedApplications.stream()
                .map(Application::getProperty)
                .distinct()
                .toList();
    }


}
