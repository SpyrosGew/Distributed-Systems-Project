package gr.hua.dit.ds.ds_lab_2024.dao;
import gr.hua.dit.ds.ds_lab_2024.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PropertyDAOImpl implements PropertyDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Renter> getPropertiesRenters(int id) {
        TypedQuery<Renter> query = entityManager.createQuery(
                "SELECT renter FROM Application application " +
                        "JOIN application.renter renter " +
                        "JOIN application.property property " +
                        "WHERE property.id = :id AND application.status = :status",
                Renter.class
        );
        query.setParameter("id", id);
        query.setParameter("status", Status.ACCEPTED); // Assuming the enum is ApplicationStatus
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Application> getPropertiesApplications(int id) {
        TypedQuery<Application> query = entityManager.createQuery(
                "SELECT a FROM Application a JOIN a.property p WHERE p.id = :id", Application.class
        );
        query.setParameter("id", id);
        return query.getResultList();
    }
}
