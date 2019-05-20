package ca.development.calcount.model;

import javax.persistence.*;
import java.util.*;

 @Entity
 @Table(name="FoodItem")
 public class FoodItem {

     private String foodName;
     private double itemCalorie;
     private int portionSize;


// /*    
//     //Associations
//     @ManyToMany
//     @JoinTable(name ="menuItem", 
//     joinColumns = @JoinColumn(name = "restaurant"),
//     inverseJoinColumns = @JoinColumn(name = "foodName"))
//     private Set<FoodItem> restoMenuItem; 
// */

     //Setters
     public void setFoodName(String foodName) {
         this.foodName = foodName;
     }
     public void setItemCalorie(double itemCalorie) {
         this.itemCalorie = itemCalorie;
     }

     public void setPortionSize(int portionSize) {
         this.portionSize = portionSize;
     }



    //Getters
    public String getFoodName() {
        return foodName;
    }
    public double getItemCalorie() {
        return itemCalorie;
    }
    public int getPortionSize() {
        return portionSize;
    }

    @Override
	public String toString() {
		return "FoodItem [Food Name=" + foodName + ", Food Calorie=" + itemCalorie + ", Portion=" + portionSize + "]";
	}

}