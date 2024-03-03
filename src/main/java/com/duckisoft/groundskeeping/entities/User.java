package com.duckisoft.groundskeeping.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "users")
public class User {
    @Setter
    private long id;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private boolean enabled;

    @Primary
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="users_id_seq")
    @SequenceGenerator(name="users_id_seq", sequenceName="users_id_seq", allocationSize = 1)
    public long getId() {
        return id;
    }

}
