package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.user.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.odobren = :odobren AND u.role = '' AND u.potvrdjenMail = true")
    List<User> findAllByOdobren(boolean odobren);
}
