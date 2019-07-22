package ca.development.calcount.model;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

 @Entity
 @Table(name="FoodItem")
 public class FoodItem {

    //FoodItem Attributes
     @Id private String foodName;
     private double itemCalorie;
     private int portionSize;

    //Associations
    @ManyToMany(mappedBy = "consummedFoodItems")
    @JsonIgnoreProperties("consummedFoodItems")
	private Set<User> userConsumption;


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

     public void setUserConsumption(Set<User> userConsumption) {
		this.userConsumption = userConsumption;
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
    public Set<User> getUserConsumption() {
		return userConsumption;
    }
    


    public void addUserConsumption(User user){
		if(this.userConsumption == null){
			this.userConsumption = new HashSet();
		}
		this.userConsumption.add(user);
	}

	public boolean removeUserConsumption(User user) {
		if(this.userConsumption.contains(user)) {
			this.userConsumption.remove(user);
			return true;
		}
		return false;
	}

    @Override
	public String toString() {
		return "FoodItem [Food Name=" + foodName + ", Food Calorie=" + itemCalorie + ", Portion=" + portionSize + "]";
	}

}