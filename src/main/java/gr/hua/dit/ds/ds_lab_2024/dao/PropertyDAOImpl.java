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

    @Override
    public List<Property> findByFilters(String city, Integer minPrice, Integer maxPrice, String type) {
        // Build the query dynamically based on the provided filters
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Property p WHERE 1=1");

        if (city != null && !city.trim().isEmpty()) {
            queryBuilder.append(" AND LOWER(TRIM(p.city)) LIKE LOWER(CONCAT('%', TRIM(:city), '%'))");
        }
        if (minPrice != null) {
            queryBuilder.append(" AND p.price >= :minPrice");
        }
        if (maxPrice != null) {
            queryBuilder.append(" AND p.price <= :maxPrice");
        }
        if (type != null && !type.trim().isEmpty()) {
            queryBuilder.append(" AND p.type = :type");
        }
        // Create the query
        TypedQuery<Property> query = entityManager.createQuery(queryBuilder.toString(), Property.class);
        // Set parameters
        if (city != null && !city.trim().isEmpty()) {
            query.setParameter("city", city);
        }
        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        if (type != null && !type.trim().isEmpty()) {
            query.setParameter("type", type);
        }

        // Execute the query and return the results
        return query.getResultList();
    }

    @Override
    public List<Property> findPropertiesByOwner(Owner owner) {
        String jpql = "SELECT p FROM Property p WHERE p.owner = :owner";
        TypedQuery<Property> query = entityManager.createQuery(jpql, Property.class);
        query.setParameter("owner", owner);
        return query.getResultList();
    }


}
