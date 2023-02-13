package com.bci.client.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBCI {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime created;
    private String role;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    @OneToMany(targetEntity = Phone.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="user_fk",referencedColumnName = "id")
    private List<Phone> phones;

    @PrePersist
    public void onInsert() {
        this.setRole("USER");
        this.created = LocalDateTime.now();
    }

}
