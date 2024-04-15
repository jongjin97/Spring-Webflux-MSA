package com.example.userservice.service;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;
import com.example.userservice.entity.User;
import com.example.userservice.kafka.KafkaUserProducer;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaUserProducer kafkaUserProducer;
//    public Mono<ResponseUser> saveUser(RequestUser requestUser){
//        User user = new User(requestUser).encode(passwordEncoder);
//        return userRepository.save(user).map(ResponseUser::new);
//    }

    public Mono<ResponseUser> saveUser(RequestUser requestUser){
        User user = new User(requestUser).encode(passwordEncoder);
        Mono<User> findUser = userRepository.findUserByEmail(requestUser.getEmail())
                .hasElement()
                .flatMap(hasEelemnt -> {
                    if(hasEelemnt)
                        return Mono.error(new RuntimeException("User with email " + user.getEmail() + " already exists."));
                    else
                        return userRepository.save(user);
                });

        return findUser.map(ResponseUser::new);
    }

    public Mono<ResponseUser> saveUser2(RequestUser requestUser){
        User user = new User(requestUser).encode(passwordEncoder);
        Mono<User> findUser = userRepository.findUserByEmail(requestUser.getEmail())
                .hasElement()
                .flatMap(hasEelemnt -> {
                    if(hasEelemnt)
                        return Mono.error(new RuntimeException("User with email " + user.getEmail() + " already exists."));
                    else {
                        try {
                            return Mono.just(kafkaUserProducer.userSend("my_topic_user", requestUser)).map(User::new);
                        } catch (JsonProcessingException e) {
                            return Mono.error(new RuntimeException(e));
                        }
                    }
                });

        return findUser.map(ResponseUser::new);
    }
}
