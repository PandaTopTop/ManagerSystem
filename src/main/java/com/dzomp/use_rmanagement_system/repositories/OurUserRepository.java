package com.dzomp.use_rmanagement_system.repositories;

import com.dzomp.use_rmanagement_system.entities.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OurUserRepository extends JpaRepository< OurUser,Integer> {
    Optional<OurUser> findByEmail(String email);
    OurUser save(OurUser ourUser);
}
