package com.CS6301Backend1.ptt.controllers;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.validation.Valid;

import com.CS6301Backend1.ptt.objects.User;
import com.CS6301Backend1.ptt.repositories.ProjectsRepository;
import com.CS6301Backend1.ptt.objects.Projects;
import com.CS6301Backend1.ptt.repositories.UserRepository;
import com.CS6301Backend1.ptt.objects.Sessions;
import com.CS6301Backend1.ptt.repositories.SessionsRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class SessionsController {

    @Autowired // This means to get the bean called projectsRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private ProjectsRepository projectsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionsRepository sessionsRepository;

    @CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
    @GetMapping("/ptt/users/{userId}/projects/{projectId}/sessions")
    public @ResponseBody ResponseEntity<Iterable<Sessions>> getprojectSessions(@PathVariable int userId, @PathVariable int projectId) {
        Projects oldProject = projectsRepository.getUsersProject(userId, projectId);
        if(oldProject == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User mappedUser = oldProject.getUser();
        if (mappedUser.getId() != userId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sessionsRepository.getSessions(userId,projectId), HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
    @PostMapping("/ptt/users/{userId}/projects/{projectId}/sessions")
    public ResponseEntity<Sessions> postSession(@PathVariable int userId, @PathVariable int projectId,  @Valid @RequestBody Sessions newSession) {
        Projects oldProject = projectsRepository.getUsersProject(userId, projectId);
        if(oldProject == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User mappedUser = oldProject.getUser();
        if (mappedUser.getId() != userId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OffsetDateTime st;
        OffsetDateTime et;
        try {
            st = OffsetDateTime.parse(newSession.getstartTime());
            et = OffsetDateTime.parse(newSession.getendTime());
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (newSession.getcounter() < 0 || st.compareTo(et) > 0 ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

	    newSession.setId(0);
        newSession.setUser(mappedUser);
	    newSession.setProject(oldProject);
        sessionsRepository.save(newSession);
        return new ResponseEntity<>(newSession, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
    @PutMapping("/ptt/users/{userId}/projects/{projectId}/sessions/{sessionId}")
    public ResponseEntity<Sessions> putSession(@PathVariable int userId, @PathVariable int projectId, @PathVariable int sessionId, @Valid @RequestBody Sessions newSession) {
        Sessions oldSession = sessionsRepository.findSession(userId, projectId, sessionId);
        if(oldSession == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User mappedUser = oldSession.getUser();
        if (mappedUser.getId() != userId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

	    Projects mappedProject = oldSession.getProject();
        if (mappedProject.getId() != projectId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OffsetDateTime st;
        OffsetDateTime et;
        try {
            st = OffsetDateTime.parse(newSession.getstartTime());
            et = OffsetDateTime.parse(newSession.getendTime());
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (newSession.getcounter() < 0 || st.compareTo(et) > 0 ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        newSession.setId(sessionId);
        newSession.setUser(mappedUser);
	    newSession.setProject(mappedProject);
        sessionsRepository.save(newSession);
        return new ResponseEntity<>(newSession, HttpStatus.OK);
    }

}