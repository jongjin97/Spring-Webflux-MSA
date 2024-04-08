package com.example.userservice.dto;

import com.example.userservice.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResponseUser {
    private Long id;
    private String email;

    public ResponseUser(User user){
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
