package com.fokatindia.repository.notification;

import com.fokatindia.entity.notification.Notifications;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notifications, Long> {
}