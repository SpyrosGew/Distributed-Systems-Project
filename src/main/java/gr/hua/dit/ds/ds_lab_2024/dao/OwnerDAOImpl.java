package gr.hua.dit.ds.ds_lab_2024.dao;


import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Properties;

@Repository
public class OwnerDAOImpl implements OwnerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Property> getOwnersProperties() {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p JOIN p.owner o", Property.class
        );
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Application> getOwnersApplications() {
        TypedQuery<Application> query = entityManager.createQuery(
                "SELECT a FROM Application a JOIN a.owner o", Application.class
        );
        return query.getResultList();
    }
}
