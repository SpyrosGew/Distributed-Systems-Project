package gr.hua.dit.ds.ds_lab_2024.dao;

import gr.hua.dit.ds.ds_lab_2024.entities.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDAOImpl implements UserDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Notification> getUserNotifications(int id) {
        TypedQuery<Notification> query = entityManager.createQuery(
                "SELECT n FROM Notification n JOIN n.receiver r WHERE r.id =:id", Notification.class
        );
        query.setParameter("id",id);
        return query.getResultList();
    }
}
