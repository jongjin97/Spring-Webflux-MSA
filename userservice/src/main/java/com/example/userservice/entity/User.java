package com.example.userservice.entity;

import com.example.userservice.dto.RequestUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

@Table("user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    private String email;

    private String password;

    public User(RequestUser requestUser){
        this.email = requestUser.getEmail();
        this.password = requestUser.getPassword();
    }
    public User encode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }
}
