package ca.development.calcount.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.text.StyledEditorKit.ItalicAction;

import ca.development.calcount.exception.InvalidInputException;
import ca.development.calcount.exception.NullObjectException;
import ca.development.calcount.model.*;
import ca.development.calcount.security.Password;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.text.SimpleDateFormat;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;


        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        /// USER METHODS                 
        ///////////////////////////////////////////////////////////////////////////////////////////////////////


    double PAct;

    @Transactional
    public User createAccount(String username, String firstName, String lastName, String email, String password)
            throws Exception {
        String passwordHash = "";

        if (!email.contains("@") ) {
            throw new InvalidInputException("This is not a valid email address! (missing @)");
        }

        if (email.contains("@") ) {
            if (email.contains(".com") || email.contains(".ca") || email.contains(".org") || email.contains(".net") ) {

            }
            else {
                throw new InvalidInputException("This is not a valid email address! (invalid domain)");
            }
        }

        if (password.length() <= 6) {
            throw new InvalidInputException("Your password must be longer than 6 characters!");
        }
        if (username.length() <= 3) {
            throw new InvalidInputException("Username should be longer than 3 characters");
        }
        if (entityManager.find(User.class, username) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        
        try {
            passwordHash = Password.getSaltedHash(password);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
        
        User u = new User();
        u.setUsername(username);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(passwordHash);
        u.setCaloriesConsummed(0);
        entityManager.persist(u);
        return u;

    }

    @Transactional
    public User inputBodyInfo(String username, int age, String sex ,double weight, double height, int physAct)
            throws InvalidInputException, NullObjectException {

        User u = getUser(username);

        if (age <= 0) {
            throw new InvalidInputException("Age cannot be less than 1");
        }

        if (weight <= 0 || height <= 0) {
            throw new InvalidInputException("Weight and or Height cannot be less than 1");
        }

        // if (!sex.equals("M")  || !sex.equals("m") || !sex.equals("F") || !sex.equals("f") ) {
        //     throw new InvalidInputException("Invalid sex selection");
        // }

        if (sex.equals("M") || sex.equals("m")) {
            switch(physAct) {
            case 0:
            PAct = 1.0;
            case 1:
            PAct = 1.12;
            case 2:
            PAct = 1.27;
            case 3:
            PAct = 1.54;
            }
        }

        else if (sex.equals("F") || sex.equals("f") ) {
            switch(physAct) {
            case 0:
            PAct = 1.0;
            case 1:
            PAct = 1.14;
            case 2:
            PAct = 1.27;
            case 3:
            PAct = 1.45;
            }
        }

        else {
            throw new InvalidInputException("Invalid sex selection");
        }

        u.setAge(age);
        u.setWeight(weight);
        u.setHeight(height);
        u.setSex(sex);
        u.setPhysicalActivity(PAct);
        entityManager.merge(u);
        //entityManager.persist(u);
        return u;

    }

    @Transactional
    public void userRequiredCalories(String username) throws NullObjectException {
        User u = getUser(username);
        double pA = u.getPhysicalActivity();
        double weight = u.getWeight();
        double height = u.getHeight();
        int age = u.getAge();
        String sex = u.getSex();
        Double reqCal;

        if (sex.equals("M") || sex.equals("m")) {
            reqCal = 864 - (9.72 * age) + pA *(14.2*weight + 503*height);
        }
        else {
            reqCal = 387 - (7.31 * age) + pA *(10.9*weight + 660.7*height);
        }

        u.setCaloriesRequired(reqCal);
        entityManager.merge(u);

    }

    /**
         * Method that allows to delete a user's account given its username
         * @param username
         * @throws  NullObjectException
         */
        @Transactional
        public void deleteUser(String username) throws NullObjectException {
            User u = getUser(username);
            entityManager.remove(u);
        }


        /**
         * Method that allows get a user given its username
         * @param username
         * @return User
         * @throws  NullObjectException
         */
        @Transactional
        public User getUser(String username) throws NullObjectException {
            if(entityManager.find(User.class, username) == null) {
                throw new NullObjectException("User does not exist");
            }
            else {
                User user = entityManager.find(User.class, username);
                return user;
            }
        }

        /**
         * Method that gets all users in database using native SQL query statement
         * @return list of AppUsers
         */
        @Transactional
        public List<User> getAllUsers() throws NullObjectException{
            Query q = entityManager.createNativeQuery("SELECT * FROM users");
            @SuppressWarnings("unchecked")
            List<User> users = q.getResultList();
            if(users.isEmpty()){
                throw new NullObjectException("No users exist");
            }
            return users;
        }

        /**
         *Method that gets the number of users in the datase
         * @return number of users
         */
        @Transactional
        public int getNumberUsers(){
            int number = 0;
             try{
                 number = getAllUsers().size();
             }catch(NullObjectException e){
                 return 0;
             }
             return number;
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        /// FOOD ITEM METHODS                 
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        @Transactional
        public FoodItem createFoodItem(String foodName, double itemCal, int foodPortion) throws Exception {
            FoodItem fi = new FoodItem();
            Date date = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String consummedDate = sdf.format(date);

            if(foodName.equals(null)) {
                throw new InvalidInputException("Enter name of food item");
            }
            
            if (foodPortion == 0) {
                foodPortion = 1;
            }
            fi.setFoodName(foodName);
            fi.setItemCalorie(itemCal);
            fi.setPortionSize(foodPortion);
            fi.setDateConsummed(consummedDate);
            entityManager.persist(fi);
            return fi;
        }

        @Transactional
        public FoodItem getFoodItem(String foodName) throws NullObjectException {
            if(entityManager.find(FoodItem.class, foodName) == null) {
                throw new NullObjectException("Food Item does not exist");
            }
            else {
                FoodItem fi = entityManager.find(FoodItem.class, foodName);
                return fi;
            }
        }


        // public List<FoodItem> listAllConsummed(String username) throws NullObjectException {
        //     Query q = entityManager.createNativeQuery("SELECT * FROM foodItem WHERE foodName IN (SELECT foodName FROM consummedFoodItems WHERE username =:username)");
        //     q.setParameter("username", username);
        //     @SuppressWarnings("unchecked")
        //     List<FoodItem>likedRestaurants = q.getResultList();
        //     if (likedRestaurants.size() == 0){
        //         throw new NullObjectException ("User does not have liked restaurants");
        //     }
        //     return likedRestaurants;
        // }

        public List<FoodItem> listAllConsummed(String username) throws NullObjectException {
            User u = entityManager.find(User.class, username);
            List<FoodItem> consummed = u.getconsummedFoodItems();
            if (consummed.size() == 0){
                throw new NullObjectException ("User does not have any consummed items");
            }
            return consummed;
        }

    @Transactional
    public String getLastItemConsummed(String username) throws NullObjectException {
        User u = getUser(username);
        List<FoodItem> consummedItems = u.getconsummedFoodItems();
        FoodItem lastItem = consummedItems.get(consummedItems.size() -1);
        String lastConsummedDate = lastItem.getDateConsummed();
        return lastConsummedDate;
    }
    

    @Transactional
	public User addConsummed(String username, String foodName, double itemCal, int foodPortion) throws  Exception {
        User user = getUser(username);
        // Date date = new Date();
        // //Date time = new Date();
        // String lastConsummedDate = getLastItemConsummed(username);
		        
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // Date dateInput = sdf.parse(lastConsummedDate);
        // String date1 = sdf.format(dateInput);        
        // String date2 = sdf.format(date);
        
        FoodItem foodItem = new FoodItem();
		// try {
		// 	//foodItem = getFoodItem(foodName);
		// }
		// catch(NullObjectException e1){

        foodItem = createFoodItem(foodName, itemCal, foodPortion);
        
        double calCount = user.getCaloriesConsummed();
        double foodCal = foodItem.getItemCalorie();
        int fPortion = foodItem.getPortionSize();
        calCount = calCount + (foodCal*foodPortion);
        user.addConsummedFood(foodItem);
        
        user.setCaloriesConsummed(calCount);
        //foodItem.addUserConsumption(user);
        entityManager.merge(user);
        entityManager.merge(foodItem);
        return user;
    }

        @Transactional
        public void updateCalCount(String username, String foodName) throws NullObjectException {
            User u = getUser(username);
            FoodItem fi = getFoodItem(foodName);
            double calCount = u.getCaloriesConsummed();
            double foodCal = fi.getItemCalorie();
            int foodPortion = fi.getPortionSize();
            calCount =+ (foodCal*foodPortion);
            u.setCaloriesConsummed(calCount);
            entityManager.merge(u);

        }



}