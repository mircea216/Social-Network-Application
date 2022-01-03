package com.example.social_network_bastille.domain.dto;

import java.util.Objects;

public class UserDTO {
    Long id;
    String username;

    public UserDTO(String username, Long id) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return username.equals(userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
