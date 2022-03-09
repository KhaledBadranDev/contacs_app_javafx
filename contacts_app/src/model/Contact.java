package model;

import java.util.ArrayList;

/**
 * This class represents a contact
 * @author Khaled Badran
 * @version 1.00 - 6/12/2021
 */
public class Contact {
	
	public String firstName;
	public String lastName;
	public String hasPhoto;
	public ArrayList<String> emails;

	/**
     * constructor method of the Contact class
     * 
     * @author Khaled Badran
     * 
	 * @param firstName first name of the contact
	 * @param lastName last name of the contact 
	 */
	public Contact(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.hasPhoto = "";
		this.emails = new ArrayList<>();
	}
	
	/**
	 * override the default toString()
	 * @return String to be displayed on the list view
	 * @author Khaled Badran
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format(this.firstName + " " + this.lastName);
	}
	
}
