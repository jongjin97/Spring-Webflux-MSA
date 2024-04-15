package com.example.userservice.kafka;

import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaUserProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fieldList = Arrays.asList(
        //Field.builder().type("int32").optional(true).field("id").build(),
            Field.builder().type("string").optional(true).field("email").build(),
            Field.builder().type("string").optional(true).field("password").build()
    );
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fieldList)
            .optional(false)
            .name("user")
            .build();

    public User userSend(String topic, User user) throws JsonProcessingException {
        UserPayload userPayload = UserPayload.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        KafkaUserDto kafkaUserDto = KafkaUserDto.builder()
                .schema(schema)
                .payload(userPayload)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(kafkaUserDto);

        kafkaTemplate.send(topic, jsonString);

        return user;
    }
}
