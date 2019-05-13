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
public class RestaurantRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Restaurant createRestaurant(String restoName) {
        Restaurant r = new Restaurant();
        r.setRestaurantName(restoName);
        r.setRestoMenuItem(null); 
        return r;
    }

    @Transactional
    public Restaurant addMenuItem(Restaurant resto, FoodItem fi) {
        resto.getRestoMenuItem().add(fi);
        return resto;
    }
}