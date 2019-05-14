package ca.development.calcount.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="FoodItem")
public class FoodItem {

    private String foodName;
    private double itemCalorie;


/*    
    //Associations
    @ManyToMany
    @JoinTable(name ="menuItem", 
    joinColumns = @JoinColumn(name = "restaurant"),
    inverseJoinColumns = @JoinColumn(name = "foodName"))
    private Set<FoodItem> restoMenuItem; 
*/

    //Setters
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public void setItemCalorie(double itemCalorie) {
        this.itemCalorie = itemCalorie;
    }



    //Getters
    public String getFoodName() {
        return foodName;
    }
    public double getItemCalorie() {
        return itemCalorie;
    }

}