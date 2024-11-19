package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.VerificationToken;
import com.NJT.WebApi.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken,Long > {
    Optional<VerificationToken> findByToken(String token);

    long deleteByUser(User user);
}
