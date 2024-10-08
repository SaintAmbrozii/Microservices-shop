package com.example.userservice.dto;


import com.example.userservice.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String picture;
    private Integer enabled;
    private String address;
    private String locale;
    private String gender;

    private Set<GrantedAuthority> authority;

    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setAddress(user.getAddress());
        dto.setPhone(user.getPhone());
        dto.setEnabled(user.getEnabled());
        dto.setAuthority(user.getAuthority());
        dto.setPassword(user.getPassword());
        dto.setPicture(user.getPicture());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
