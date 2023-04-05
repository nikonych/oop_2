package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.enums.Role;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NamedQuery(name = "User.authenticate", query = "from User where name = :name")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String name;

    Integer balance = 5000;

    public User(String name) {
        this.name = name;
    }
    @Enumerated(EnumType.STRING)
    Role role = Role.USER;

    public User() {

    }
}
