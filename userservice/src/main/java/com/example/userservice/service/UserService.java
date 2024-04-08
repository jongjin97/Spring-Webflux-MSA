package com.example.userservice.service;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
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
    public Mono<ResponseUser> saveUser(RequestUser requestUser){
        User user = new User(requestUser).encode(passwordEncoder);

        return userRepository.findUserByEmail(requestUser.getEmail())
                .flatMap(user1 -> {
                    if(user1.getId() == null)
                        return Mono.error(new RuntimeException("email exists."));
                    return userRepository.save(user1).map(ResponseUser::new);
                });
    }
}
