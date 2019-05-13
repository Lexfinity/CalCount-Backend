package ca.development.calcount.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ca.development.calcount.exception.InvalidInputException;
import ca.development.calcount.exception.NullObjectException;
import ca.development.calcount.model.*;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User createAccount(String username, String firstName, String lastName, String email, String password)
            throws Exception {
        //String passwordHash = "";

        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidInputException("This is not a valid email address!");
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
        /*
        try {
            passwordHash = Password.getSaltedHash(password);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
        */
        User u = new User();
        u.setUsername(username);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(password);
        u.setCaloriesConsummed(0);
        entityManager.persist(u);
        return u;

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
         * @return AppUser
         * @throws  NullObjectException
         */
        @Transactional
        public User getUser(String username) throws NullObjectException {
            if(entityManager.find(User.class, username) == null) {
                throw new NullObjectException("User does not exist");
            }
            else {
                User appUser = entityManager.find(User.class, username);
                return appUser;
            }
        }

        /**
         * Method that gets all users in database using native SQL query statement
         * @return list of AppUsers
         */
        @Transactional
        public List<User> getAllUsers() throws NullObjectException{
            Query q = entityManager.createNativeQuery("SELECT * FROM app_users");
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



}