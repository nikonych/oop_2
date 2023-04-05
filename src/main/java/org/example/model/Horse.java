package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "horses")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String name;

    Integer betCount = 1;

    public Horse(String name) {
        this.name = name;
    }

    public Horse() {

    }
}
