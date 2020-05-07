package com.CS6301Backend1.ptt.repositories;

import com.CS6301Backend1.ptt.objects.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
@Query(value = "SELECT t FROM User t WHERE t.email = ?1")
Optional<User> findByemail (String email);
}