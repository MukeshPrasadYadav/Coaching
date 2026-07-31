package com.projects.coaching_offline_support.notification;


import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationStrategyFactory {

    private final Map<NotificationMode,NotificationStrategy> strategies;


    public NotificationStrategyFactory(List<NotificationStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        NotificationStrategy::getMode,
                        Function.identity()
                ));
    }

    public NotificationStrategy get(NotificationMode mode) {
        NotificationStrategy strategy = strategies.get(mode);

        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for " + mode);
        }

        return strategy;
    }



}
