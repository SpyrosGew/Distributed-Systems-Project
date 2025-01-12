package gr.hua.dit.ds.ds_lab_2024.dao;

import gr.hua.dit.ds.ds_lab_2024.entities.Notification;

import java.util.List;

public interface UserDAO {
    public List<Notification> getUserNotifications(int userId);
}
