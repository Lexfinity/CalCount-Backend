package ca.development.calcount.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ca.development.calcount.exception.InvalidInputException;
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
        String passwordHash = "";

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
        u.setPassword(passwordHash);
        entityManager.persist(u);

        return u;

    }

}