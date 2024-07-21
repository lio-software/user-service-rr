package org.lio.userservicerr.infrastructure.outputadapter.mysqlrepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lio.userservicerr.domain.RequestData;
import org.lio.userservicerr.domain.User;
import org.lio.userservicerr.infrastructure.UserModel;
import org.lio.userservicerr.infrastructure.UserRepository;
import org.lio.userservicerr.infrastructure.outputport.EntityRepository;
import org.lio.userservicerr.infrastructure.utils.EncryptionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MysqlRepository implements EntityRepository {
    @Autowired
    private UserRepository userRepository;

    private final AmqpTemplate rabbitTemplate;


    private final EncryptionUtils encryptionUtils = new EncryptionUtils();

    public MysqlRepository(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public <T> UserModel updateUser(T reg, String uuid) {
        UserModel user = userRepository.findByUuid(uuid);

        if (user == null) {
            return null;
        }

        String password = user.getPassword();
        String posiblePassword = ((User) reg).getPassword();

        try {
            Boolean matches = encryptionUtils.matches(posiblePassword, password);

            if (!matches) {
                return null;
            }

            user.setFirstName(((User) reg).getFirstName());
            user.setLastName(((User) reg).getLastName());
            user.setEmail(((User) reg).getEmail());
            user.setPassword(encryptionUtils.encrypt(posiblePassword));
            user.setPhoneNumber(((User) reg).getPhoneNumber());
            user.setImageUrl(((User) reg).getImageUrl());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userRepository.save(user);
    }

    @Override
    public <T> Boolean deleteUser(T reg) {
        UserModel user = userRepository.findByUuid((String) reg);

        if (user == null) {
            return Boolean.FALSE;
        }
        userRepository.deleteById(user.getId());
        return Boolean.TRUE;
    }

    @Override
    public <T> UserModel getUserByUuid(T reg) {
        return userRepository.findByUuid((String) reg);
    }

    @RabbitListener(queues = "users-queue")
    public void receiveMessage(Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        String response = handleUserRequest(new String(message.getBody()));

        rabbitTemplate.convertAndSend(replyTo, response, msg -> {
            msg.getMessageProperties().setCorrelationId(correlationId);
            return msg;
        });
    }

    private String handleUserRequest(String requestData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RequestData request = objectMapper.readValue(requestData, RequestData.class);
            String uuid = request.getUserId();

            UserModel user = getUserByUuid(uuid);

            if (user != null) {
                String userJson = objectMapper.writeValueAsString(user);
                return "{\"status\":\"success\", \"data\":" + userJson + "}";
            } else {
                return "{\"status\":\"error\", \"message\":\"User not found\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\", \"message\":\"Internal Server Error\"}";
        }
    }
}
