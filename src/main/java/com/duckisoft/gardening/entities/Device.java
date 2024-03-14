package com.duckisoft.gardening.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "device")
public class Device {
    @Setter
    private int id;

    @Setter
    private User user;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private boolean temperature;

    @Getter
    @Setter
    private boolean humidity;

    @Primary
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="device_id_seq")
    @SequenceGenerator(name="device_id_seq", sequenceName="device_id_seq", allocationSize = 1)
    public int getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }
}
