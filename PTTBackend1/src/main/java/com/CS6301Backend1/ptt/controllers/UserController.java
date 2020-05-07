package com.CS6301Backend1.ptt.controllers;

import java.util.*;
import javax.validation.Valid;
import com.CS6301Backend1.ptt.objects.Projects;
import com.CS6301Backend1.ptt.repositories.UserRepository;
import com.CS6301Backend1.ptt.objects.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class UserController {

	@Autowired // This means to get the bean called userRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;

	@CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
	@GetMapping("/ptt/users")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
	@GetMapping("/ptt/users/{userId}")
	public ResponseEntity<User> getUser(@PathVariable int userId) {

		Optional<User> result = userRepository.findById(userId);

		if (!result.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(result.get(), HttpStatus.OK);
	}

	@CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
	@PostMapping("/ptt/users")
	public ResponseEntity<User> postUser(@Valid @RequestBody User newUser) {

		if(userRepository.findByemail(newUser.getEmail()).isPresent()){
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		newUser.setId(0);
		userRepository.save(newUser);
		return new ResponseEntity<>(newUser,HttpStatus.CREATED);
	}

	@CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
	@PutMapping("/ptt/users/{userId}")
	public ResponseEntity<User> putUser(@Valid @RequestBody User updatedUser, @PathVariable int userId) {
		if(!userRepository.existsById(userId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

//		 if(updatedUser.getFirstName().equals("") || updatedUser.getLastName().equals("")) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}

		 
		 User oldUser = userRepository.findById(userId).get();

		if (!updatedUser.getEmail().equals(oldUser.getEmail())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		 updatedUser.setId(userId);
		 updatedUser.setEmail(oldUser.getEmail());
		 userRepository.save(updatedUser);
		 return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@CrossOrigin(origins = {"http://localhost:3000", "http://gazelle.cc.gatech.edu:9102", "http://gazelle.cc.gatech.edu:9104"})
	@DeleteMapping("/ptt/users/{userId}")
	public ResponseEntity<User> deleteUser(@PathVariable int userId) {
		if(!userRepository.existsById(userId)) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}

		User deletedUser = userRepository.findById(userId).get();
		userRepository.deleteById(userId);
		return new ResponseEntity<User>(deletedUser, HttpStatus.OK);
	}

}