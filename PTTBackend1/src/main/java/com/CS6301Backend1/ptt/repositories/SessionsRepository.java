package com.CS6301Backend1.ptt.repositories;

import com.CS6301Backend1.ptt.objects.Sessions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SessionsRepository extends CrudRepository<Sessions, Integer> {

    @Query(value = "SELECT * from sessions s WHERE s.user_id = ?1 and s.project_id = ?2", nativeQuery = true)
    Collection<Sessions> getSessions(int userId, int projectId);

    @Query(value = "SELECT * from sessions s WHERE s.user_id = ?1 and s.project_id = ?2 and s.id = ?3", nativeQuery = true)
    Sessions findSession(int userId, int projectId, int sessionId);

}
