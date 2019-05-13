package ca.development.calcount.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="Restaurants")
public class Restaurant {

    //Attributes
    @Id private String restoName;
     private List<String> restoMenuItem;
    // private List<Double> itemCalorie;
    // private List<Double> itemFat;
    // private List<Double> itemCarb;

    //Setters
	public void setRestaurantName(String restoName) {
        this.restoName = restoName;
    }
    public void setRestoMenuItem(List<String> restoMenuItem) {
        this.restoMenuItem = restoMenuItem;
    }
    


    //Getters
    public String getRestoName() {
        return restoName;
    }
    public List<String> getRestoMenuItem() {
        return restoMenuItem;
    }
}