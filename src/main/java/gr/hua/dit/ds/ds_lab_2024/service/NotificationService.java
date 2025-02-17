package gr.hua.dit.ds.ds_lab_2024.service;


import gr.hua.dit.ds.ds_lab_2024.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void deleteNotifications(int user_id) {
        notificationRepository.deleteByReceiverId(user_id);
    }
}
