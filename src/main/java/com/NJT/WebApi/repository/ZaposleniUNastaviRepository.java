package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.user.ZaposleniUNastavi;
import com.NJT.WebApi.model.user.ZaposleniVanNastave;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ZaposleniUNastaviRepository extends CrudRepository<ZaposleniUNastavi, Long> {

    Optional<ZaposleniUNastavi> findByUsername(String username);
}