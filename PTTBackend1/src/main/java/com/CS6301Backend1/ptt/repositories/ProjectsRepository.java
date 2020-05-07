package com.CS6301Backend1.ptt.repositories;

import com.CS6301Backend1.ptt.objects.Projects;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ProjectsRepository extends CrudRepository<Projects, Integer> {
    @Query(value = "SELECT * FROM projects p WHERE p.user_id = ?1", nativeQuery = true)
    Collection<Projects> getUsersProjects(int userId);

    @Query(value = "SELECT count(userid) FROM projects p WHERE p.user_id = ?1", nativeQuery =  true)
    Integer getNumberOfProjectsForUser(int userId);

    @Query(value = "SELECT * from projects p WHERE p.user_id = ?1 and p.id = ?2", nativeQuery = true)
    Projects getUsersProject(int userId, int projectId);

    @Query(value = "SELECT * from projects p WHERE p.id = ?1", nativeQuery = true)
    Projects findByprojectId(int projectId);

    @Query(value = "SELECT * from projects p WHERE p.projectname = ?1 and p.user_id = ?2", nativeQuery = true)
    Optional<Projects> findByprojectname(String projectname, int userId);

}
