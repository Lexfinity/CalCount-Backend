package ca.development.calcount.controller;

import ca.development.calcount.exception.*;
import ca.development.calcount.model.*;
import ca.development.calcount.repository.*;
import ca.development.calcount.exception.*;
import ca.development.calcount.service.*;
import ca.development.calcount.security.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.*;
// import javax.mail.*;
// import javax.mail.internet.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authentication;

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
     * Controller method that attempts to login
     * 
     * @param username
     * @param password
     * @return ResponseEntity
     */
    @GetMapping("/auth/{username}/{password}")
    public ResponseEntity login(@PathVariable("username") String username, @PathVariable("password") String password)
            throws Exception {
        // No exception thrown means the authentication succeeded
        try {
            authentication.login(username, password);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "Login successful"));
    }

    /** Controller method that attempts to logout
     * @param username
     * @return ResponseEntity
     */
    @GetMapping("/logout/{username}")
    public ResponseEntity logout(@PathVariable("username")String username) throws Exception {
        //No exception thrown means the authentication succeeded
        try {
            authentication.logout(username);
        }
        catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "Logout successful"));
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
        double calCount;
        try {
            User u = userRepository.getUser(username);
            userRepository.userRequiredCalories(username);
            calCount = u.getCalorieRequired(); 
        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        System.out.println(calCount);
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
        return ResponseEntity.status(HttpStatus.OK).body(user);

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

    @PostMapping("/create/foodItem/{foodName}/{itemCal}/{foodPortion}")
    public ResponseEntity createFoodItem(@PathVariable("foodName") String foodName, @PathVariable("itemCal") double itemCal,  @PathVariable("foodPortion") int foodPortion) {
        return null;
    }


    @PostMapping("/update/calCount/{username}/{foodName}")
    public ResponseEntity updateCalCount(@PathVariable("username") String username, @PathVariable("foodName") String foodName) throws Exception {
        FoodItem fi = userRepository.getFoodItem(foodName);
        userRepository.addConsummed(username, fi.getFoodName(), fi.getItemCalorie(), fi.getPortionSize());
        
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "User CalCount was successfully updated."));
    }

    @PostMapping("/{username}/consummed/{foodName}/{itemCal}/{foodPortion}")
    public ResponseEntity addConsummedFood(@PathVariable("username") String username, @PathVariable("foodName") String foodName, @PathVariable("itemCal") double itemCal,  @PathVariable("foodPortion") int foodPortion) throws Exception {
        User u = new User();
        Date date = new Date();
        //Date time = new Date();
        double calCount = u.getCaloriesConsummed();
        u = userRepository.getUser(username);
        List<FoodItem> userConsummed = u.getconsummedFoodItems();
        String lastConsummedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date2 = sdf.format(date);

        
        if(userConsummed.isEmpty() == false) {
            lastConsummedDate = userRepository.getLastItemConsummed(username);
        }

        else if (userConsummed.isEmpty() == true) {
            lastConsummedDate = date2;
        }

        Date dateInput = sdf.parse(lastConsummedDate);
        String date1 = sdf.format(dateInput);        
 
        //FoodItem fi = new FoodItem();
        try {

            if ((date1.compareTo(date2)) < 0) {
                u.setCaloriesConsummed(0);
            }
            //fi = userRepository.createFoodItem(foodName, itemCal, foodPortion);
            u = userRepository.addConsummed(username, foodName, itemCal, foodPortion);
        } catch (NullObjectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }


    @GetMapping("/{username}/consummed/all")
    public ResponseEntity allConsummed(@PathVariable("username") String username){
        List<FoodItem> consummed;
        try {
            consummed = userRepository.listAllConsummed(username);
        } catch (NullObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(consummed);
    }

    
}