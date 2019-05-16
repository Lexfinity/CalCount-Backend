package ca.development.calcount.controller;

import ca.development.calcount.exception.*;
import ca.development.calcount.model.*;
import ca.development.calcount.repository.*;
import ca.development.calcount.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
// import javax.mail.*;
// import javax.mail.internet.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    /**
     * Greeting
     * 
     * @return User connected
     */
    @RequestMapping("/")
    public String greeting() {
        return "User connected!";
    }

    /**
     * Method that creates a new account for a user. Username must be unique.
     * 
     * @param username
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return ResponseEntity
     */
    @PostMapping("/create/{username}/{firstName}/{lastName}/{email}/{password}")
    public ResponseEntity createAccount(@PathVariable("username") String username,
            @PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName,
            @PathVariable("email") String email, @PathVariable("password") String password) {

        try {
            User user = userRepository.createAccount(username, firstName, lastName, email, password);
            // sendRegistrationConfirmationEmail(email, firstName, username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "User account successfully created."));
    }

    @PostMapping("/bodyInfo/{username}/{age}/{sex}/{weight}/{height}/{physAct}")
    public ResponseEntity inputBodyInfo(@PathVariable("username") String username, @PathVariable("age") int age,
            @PathVariable("sex") String sex, @PathVariable("weight") double weight,
            @PathVariable("height") double height, @PathVariable("physAct") int physAct) throws InvalidInputException {
        try {
            User u = userRepository.getUser(username);
            userRepository.inputBodyInfo(username, age, sex, weight, height, physAct);

        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "User body info successfully updated."));

    }

    @PostMapping("/calculate/requiredCal/{username}")
    public ResponseEntity userRequiredCalories(@PathVariable("username") String username) {
        try {
            userRepository.userRequiredCalories(username);
        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "Required daily calories successfully updated."));

    }

    /**
     * Controller method that gets a AppUser
     * 
     * @param username
     * @return ResponseEntity
     */
    @GetMapping("/get/{username}")
    public ResponseEntity getAppUser(@PathVariable("username") String username) {
        //List<User> users;
        User user = new User();
        try {
            user = userRepository.getUser(username);
            // user = userRepository.getAppUserQuery(username);
        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);//user.get(0));

    }

    /**
     * Controller method that gets all users in the database
     * 
     * @return ResponseEntity
     */
    @GetMapping("/get/all")
    public ResponseEntity getAllUsers() {
        List<User> allUsers;
        try {
            allUsers = userRepository.getAllUsers();
        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    /**
     * Controller method that deletes user from the database given a username
     * 
     * @param username
     * @return ResponseEntity
     */
    @GetMapping("/delete/{username}")
    public ResponseEntity deleteUser(@PathVariable("username") String username) {
        try {
            userRepository.deleteUser(username);
        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "User account successfully deleted."));

    }


    
}