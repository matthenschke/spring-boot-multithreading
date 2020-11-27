package com.henschke.matthew.spring_boot_multithreading.repositories;

import com.henschke.matthew.spring_boot_multithreading.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
