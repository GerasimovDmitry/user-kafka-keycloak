package com.example.userkafkakeycloak.service;

import com.example.userkafkakeycloak.entity.Userkk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
        import org.apache.kafka.clients.producer.Producer;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.kafka.annotation.KafkaListener;
        import org.springframework.stereotype.Service;

        import java.io.IOException;
        import java.util.List;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "delete_companies", groupId = "group_id")
    public void consume(String message) throws IOException {
        System.out.println(message);
        logger.info(String.format("#### -> Producing message -> %s", message));
        List<Userkk> users = userService.findByCompanyId(Integer.valueOf(message));
        for (Userkk user : users) {
            userService.updateCompany(Math.toIntExact(user.getId()));
        }
    }
}