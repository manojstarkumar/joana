package com.avacado.stupidapps.joana.domain.notifiers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;

@Service
public class JoanaFcmNotifier implements JoanaNotifier {

    private Logger logger = LoggerFactory.getLogger(JoanaFcmNotifier.class);

    @Autowired
    private JoanaUserRepository joanaUserRepository;

    @Override
    public void notify(List<String> emailIds, String message) {
	logger.debug("Called FCM Notifier with " + emailIds + " and " + message);
	emailIds.stream().forEach(emailId -> {
	    JoanaUser user = joanaUserRepository.findByEmail(emailId);
	    if (user != null && user.getPushToken() != null) {
		sendFcmNotification(message, user.getPushToken());
	    }
	});
    }

    private void sendFcmNotification(String message, String token) {

    }
}
