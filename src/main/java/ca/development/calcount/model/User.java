package ca.development.calcount.model;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="Users")
public class User {

    //User Attributes
	@Id private String username;
	private String firstName;
	private String lastName;
	private String email;
    private String password;
    private double calorieConsummed;


    //Setters
	public void setUsername(String username) { this.username = username; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setEmail(String email) { this.email = email; }
	public void setPassword(String password) { this.password = password; }
	public void setCaloriesConsummed(double calories) {
		this.calorieConsummed = calories;
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


    @Override
	public String toString() {
		return "User [username=" + username + ", First Name=" + firstName + ", Last Name=" + lastName + ", Email="
				+ email + ", Password=" + password + ", Calories Consummed=" + calorieConsummed + "]";
	}

}