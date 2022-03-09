package view;

import model.*;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * This class represents the GUI of the application
 * @author Khaled Badran
 * @version 1.00 - 6/12/2021
 */
public class ShowContacts extends Application{
	// creating the class variable that we will need 
	private GridPane root = new GridPane();
	private GridPane selectedContactRoot= new GridPane(); // grid pane/layout to display the info of the selected contact from the list view

	private Scene scene = new Scene(root, 600,  700); // adding root/GridPane to the scene

    private ListView<Contact> list = new ListView<>();
    private Button[] rootButtons = {new Button("Add Contact"),  new Button("Save Contact")}; // creating buttons for the main root
    private Button[] selectedContactRootButtons = {new Button("Add Email"), new Button("Save Email"), new Button("Delete Contact")}; // creating the buttons for the selected contact
    private Label[] labels = {new Label("First Name: "), new Label("Last Name: "),  new Label("Does The Contact Have Photo [y/n]: "), new Label("Email: ")}; // creating labels for the text fields 
    private TextField[] textFields = {new TextField(""),  new TextField(""),  new TextField(""), new TextField("")}; // creating text fields for the labels that we have
	TextField newEmailTextField = new TextField("");

    ImageView noPhotoImg = new ImageView(new Image (getClass().getResource("/resources/unknown.png").toString()));
    ImageView PhotoImg = new ImageView(new Image (getClass().getResource("/resources/man.png").toString()));

	private ContactList contacts = new ContactList();

	
    /**
     * The is the Main method to start the application.
     * 
     * @author Khaled Badran
     * @param args arguments giving while compiling the program
     */
	public static void main(String[] args) {
		launch(args);
	}

	
	/**
	 * This the main loop of javaFX library which will always update the GUI.
     * @author Khaled Badran
     * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{		
		setGeometry();		
		addwidgetsToRoot();
		setwidgetsNotVisible();
				
		rootButtons[0].setOnAction(e -> addContact()); // add contact button
		rootButtons[1].setOnAction(e -> saveContact()); // save contact button
		
		manageAndDisplaySelectedContactInfo();
		
		primaryStage.setTitle("Contacts Application");
		primaryStage.setScene(scene); // add the scene to the stage
		primaryStage.show();		
	}
	
	/**
	 * Manage all the widgets and events of the selected Item/contact of the view list and display all the widgets.
     * @author Khaled Badran
	 */
	private void manageAndDisplaySelectedContactInfo(){
		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {
			@Override
			public void changed(ObservableValue<? extends Contact> arg0, Contact arg1, Contact arg2 /*new value*/) {
				selectedContactRoot.getChildren().clear(); // clear all the children of the selectedContactRoot/grid pane
				
				if (arg2 != null) {
		        	int row = 0;
			        // display the photo of the selected person
					if (arg2.hasPhoto.length() > 0 && arg2.hasPhoto.equalsIgnoreCase("y"))
			        	selectedContactRoot.add(PhotoImg, 0, row++);
			        else
			        	selectedContactRoot.add(noPhotoImg, 0, row++);
			        
			        // display the name 
					Label name = new Label( " " + arg2.toString());
					name.setFont(new Font("Comic Sans MS", 26));
		        	selectedContactRoot.add(name, 0, row++);
		        	
			        // display the e-mails
		        	int emailNumber = 1;
		        	for(String email: arg2.emails)
		        		selectedContactRoot.add(new Label( String.format("   %d. Email: ", emailNumber++) + email), 0, row++);
		        		
		        	// adding buttons to add more e-mails and a button to deleted the selected contact
		        	selectedContactRoot.add(selectedContactRootButtons[0], 0, row++); // add email button
		        	selectedContactRoot.add(newEmailTextField, 0, row++); 
		        	selectedContactRoot.add(selectedContactRootButtons[1], 0, row++); // save email button
		        	selectedContactRoot.add(selectedContactRootButtons[2], 0, row++); // delete contact button

		        	selectedContactRootButtons[1].setVisible(false);
		        	newEmailTextField.setVisible(false);
		        	
		        	selectedContactRootButtons[0].setOnAction(e -> addEmail()); // bind add email button with an add function
		        	selectedContactRootButtons[1].setOnAction(e -> saveEmail(arg2)); // bind save email button with a save function
		        	selectedContactRootButtons[2].setOnAction(e -> deleteSelected(arg2)); // bind delete contact button with a delete function
		        	
				}
			}
		});
	}
	
	
	/**
	 * delete the selected Item/contact when the users clicks on the delete button
     * @author Khaled Badran
	 * @param toDelete the contact to be deleted
	 */
	private void deleteSelected(Contact toDelete) {
		contacts.contactList.remove(toDelete);
		updateListView(); 
	} 
	
	
	/**
	 * display all the needed widgets to user to enable the user to add a new email,
	 * when the the users clicks on the Add Email button
     * @author Khaled Badran
	 */
	private void addEmail(){ 
		setwidgetsNotVisible(); // make the widgets of root not visible
    	selectedContactRootButtons[1].setVisible(true);
    	newEmailTextField.setVisible(true);
	}

	
	/**
	 * save the entered email to the list of e-mails of the selected contact.
	 * @author Khaled Badran
	 * @param selected the contact that the user wants to add an e-mail to it.
	 */
	private void saveEmail(Contact selected){
    	String email = newEmailTextField.getText();
		if (email.length() > 0) {
    		if (isEmailsValid(email)) {
    			selected.emails.add(email);
    			updateListView();
    		}
    		else { // if the email isn't valid, display an error
            	Alert errorAlert = new Alert(AlertType.ERROR);
            	errorAlert.setHeaderText("Email not valid");
            	errorAlert.setContentText("Email must be at least 3 chars, must contain @ and can't start or end with @");
            	errorAlert.showAndWait();
    		}
		}
    	
    	newEmailTextField.clear(); // to clear/reset the newEmailTextField after fetching the data from it.
		selectedContactRootButtons[1].setVisible(false);
    	newEmailTextField.setVisible(false);
	}
	
	
	/**
	 * update the list view 
	 * by fetching data from the ArrayList and then adding the data to the list view.
	 * @author Khaled Badran
	 */
	private void updateListView(){ // adding the contacts from the contact list to the view list
		list.getItems().clear(); // clear the list firstly and then update it.
		for (Contact c: contacts.contactList)
	    	list.getItems().add(c);
	}
	
	
	/**
	 * make some widgets not visible.
	 * hide some widgets
	 * @author Khaled Badran
	 */
    private void setwidgetsNotVisible(){
    	for(int i = 1; i < rootButtons.length; i++) // making buttons not visible except the Add contact button/the first button
    		rootButtons[i].setVisible(false);
    	
    	for(int i = 0; i < labels.length; i++)  // making all labels not visible
    		labels[i].setVisible(false);
    	
    	for(int i = 0; i < textFields.length; i++)  // making all text fields not visible
    		textFields[i].setVisible(false);
    }
    
    
	/**
	 * make some widgets visible.
	 * display some widgets
	 * @author Khaled Badran
	 */
    private void setwidgetsVisible(){
    	for(int i = 1; i < rootButtons.length; i++) // making buttons not visible except the Add contact button/the first button
    		rootButtons[i].setVisible(true);
    	
    	for(int i = 0; i < labels.length; i++)  // making all labels not visible
    		labels[i].setVisible(true);
    	
    	for(int i = 0; i < textFields.length; i++)  // making all text fields not visible
    		textFields[i].setVisible(true);
    }
    
    
    /**
     * adjust the Geometry of the widgets and roots.
	 * @author Khaled Badran
     */
    private void setGeometry(){
        // space between rows
        root.setHgap(10);
        // space between columns
        root.setVgap(10);
        //space around the element root
        root.setPadding(new Insets(40, 40, 40, 40));
        
        // set the width and the height of the list view
		list.setPrefWidth(250);
		list.setPrefHeight(300);
		
        // spacing the buttons of the selectedContactRoot
    	selectedContactRoot.setHgap(10);
    	selectedContactRoot.setVgap(10);
    }
    
    
    /**
     * adding some widgets to the root(layout)
	 * @author Khaled Badran
     */
    private void addwidgetsToRoot(){
    	// widget/element, column, row
    	root.add(list,0,0); // adding the list of contacts
    	root.add(selectedContactRoot, 1, 0);
    			
    	int row = 2;
    	for(int i = 0; i < labels.length; i++, row++) {  // making all labels not visible
    		root.add(labels[i], 0, row);
    		root.add(textFields[i], 1, row);
    	}
    	
        root.add(rootButtons[0], 0, 1); // Add Contact button
        root.add(rootButtons[1], 0, row); // Save Contact button
    }
	
    
    /**
	 * display all the needed widgets to user to enable the user to add a new contact,
	 * when the the users clicks on the Add Contact button
	 * @author Khaled Badran
     */
    private void addContact(){
    	updateListView(); // to remove focusing/selecting items from the list
    	resetTextFields(); // to make sure to clear/reset all the TextFields after fetching the data from them.
    	setwidgetsVisible(); // make some widgets visible again 
    }
	
    
    /**
	 * save and append the list of contacts with information entered by the user
	 * @author Khaled Badran
     */
    private void saveContact(){
    	String firstName =  textFields[0].getText();
    	String lastName = textFields[1].getText();
    	String hasPhoto =  textFields[2].getText();
    	String  email = textFields[3].getText();
    	
    	Contact toAdd;
    	
    	if (firstName.length() == 0 || lastName.length() == 0) { // to make sure that first name and last name exist.
    		
        	Alert errorAlert = new Alert(AlertType.ERROR);
        	errorAlert.setHeaderText("Input not valid");
        	errorAlert.setContentText("First Name and Last Name must exist");
        	errorAlert.showAndWait();
        	
        	resetTextFields(); // to clear/reset all the TextFields after fetching the data from them.
        	setwidgetsNotVisible(); // make some widgets not visible again 
        	return;
    	} else
    		toAdd = new Contact(firstName, lastName);
    	
    	if (email.length() > 0) {
    		if (isEmailsValid(email))
    			toAdd.emails.add(email);
    		else { // if the email isn't valid, display an error
            	Alert errorAlert = new Alert(AlertType.ERROR);
            	errorAlert.setHeaderText("Email not valid");
            	errorAlert.setContentText("Email must be at least 3 chars, must contain @ and can't start or end with @");
            	errorAlert.showAndWait();
            	
            	resetTextFields(); // to clear/reset all the TextFields after fetching the data from them.
            	setwidgetsNotVisible(); // make some widgets not visible again 
            	return;
    		}
    			
    		
    	}
    	
    	if(hasPhoto.length() > 0)
    		toAdd.hasPhoto = hasPhoto;

    	contacts.contactList.add(toAdd);
		updateListView();

    	resetTextFields(); // to clear/reset all the TextFields after fetching the data from them.
    	setwidgetsNotVisible(); // make some widgets not visible again 
    }

    
    /**
	 * check whether the e-mail format is accurate/valid or not. 
	 * valid e-mail here must be at least 3 chars, must contain @ and can't start or end with @
	 * e.g: max@gmail, m@g These are valid e-mails according to this application.
	 * when the the users clicks on the Add Contact button
	 * @author Khaled Badran
     */
	private boolean isEmailsValid(String email) {
		if (email.length() < 3 || email.charAt(0) == '@' || email.charAt(email.length()-1) == '@') // if the email is not valid, return false
            return false;                   

        for(int i = 1; i < email.length()-1; i++) // if the email is valid, return false
            if (email.charAt(i) == '@'){
                return true;   
            }
        
        return false;
	}

	
	/**
	 * reset/clear all the textFields in the root(layout)
	 * @author Khaled Badran
	 */
    private void resetTextFields(){
    	for(int i = 0; i < textFields.length; i++)  // making all text fields not visible
    		textFields[i].clear();
    }
    
    
}
