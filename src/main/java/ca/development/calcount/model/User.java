package ca.development.calcount.model;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="Users")
public class User {

    //User Attributes
	@Id private String username;
	private String firstName;
	private String lastName;
	private int age;
	private double weight;
	private double height;
	private double physAct; 
	private String sex; //M or F
	private String email;
    private String password;
	private double calorieConsummed;
	private double calorieRequired;


	//User Associations
	@ManyToMany
	@JoinTable(name = "consummedFoodItems",
			joinColumns = @JoinColumn(name = "username"),
			inverseJoinColumns = @JoinColumn(name = "foodName"))
	@JsonIgnoreProperties("userConsumption")
	private List<FoodItem> consummedFoodItems;

	// private Set<String> consummedFoodItems;


    //Setters
	public void setUsername(String username) { this.username = username; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setEmail(String email) { this.email = email; }
	public void setPassword(String password) { this.password = password; }
	public void setCaloriesConsummed(double caloriesC) { this.calorieConsummed = caloriesC; }
	public void setCaloriesRequired(double caloriesR) { this.calorieRequired = caloriesR; }
	public void setAge(int age) { this.age = age; }
	public void setWeight(double weight2) { this.weight = weight2; }
	public void setHeight(double height2) { this.height = height2; }
	public void setPhysicalActivity(double PA) { this.physAct = PA; }
	public void setSex(String sex) { this.sex = sex; }
	public void setconsummedFoodItems(List<FoodItem> consummedFoodItems) {
		this.consummedFoodItems = consummedFoodItems;
	}

 
    

    //Getters
	public String getUsername()
	{
		return this.username;
	}
	public String getFirstName()
	{
		return this.firstName;
	}
	public String getLastName()
	{
		return this.lastName;
	}
	public String getEmail()
	{
		return this.email;
	}
	public String getPassword()
	{
		return this.password;
    }
    public double getCaloriesConsummed() {

        return this.calorieConsummed;
	}
	public double getCalorieRequired() {

        return this.calorieRequired;
	}
	public double getWeight() {
		
		return this.weight;
	}
	public double getHeight() {
		
		return this.height;
	}
	public int getAge() {

		return this.age;
	}
	public double getPhysicalActivity() {
		return this.physAct;
	}
	public String getSex() {

		return this.sex;
	}
	public List<FoodItem> getconsummedFoodItems() {
		return consummedFoodItems;
	}


	//Liked list
	public void addConsummedFood(FoodItem consummedFoodItems){
		if(this.consummedFoodItems == null){
			this.consummedFoodItems = new ArrayList<FoodItem>();
		}
		this.consummedFoodItems.add(consummedFoodItems);
	}




    @Override
	public String toString() {
		return "User [username=" + username + ", First Name=" + firstName + ", Last Name=" + lastName +
		 ", Age=" + age + ", Weight=" + weight + ", Height=" + height + ", Physical Activity=" + physAct + ", Email="
				+ email + ", Password=" + password + ", Calories Required=" + calorieRequired +
				 ", Calories Consummed=" + calorieConsummed + "]";
	}

}