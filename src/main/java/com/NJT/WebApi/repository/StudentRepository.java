package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.user.Student;
import com.NJT.WebApi.model.user.ZaposleniVanNastave;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {

    Optional<Student> findByUsername(String username);
}