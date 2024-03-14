package com.duckisoft.gardening.repository;

import com.duckisoft.gardening.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
