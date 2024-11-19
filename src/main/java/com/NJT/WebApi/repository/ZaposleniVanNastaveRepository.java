package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.user.ZaposleniVanNastave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ZaposleniVanNastaveRepository extends CrudRepository<ZaposleniVanNastave, Long> {


    Optional<ZaposleniVanNastave> findByUsername(String username);
}