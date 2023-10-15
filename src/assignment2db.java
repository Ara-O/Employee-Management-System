import javax.swing.*;
import java.sql.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.SystemColor; 

//Eyiara Oladipo
//2/24/2022
//Assignment 2
//Code to center the list items in the All Employees Section
class CenteredCellRenderer extends DefaultListCellRenderer {
    public CenteredCellRenderer() {
        setHorizontalAlignment(CENTER);
    }
}
public class assignment2db {
	//Declaring the necessary GUI variables
	private static JPanel addEmployeeSection;
	private static JPanel topSectionPanel;
	private static JPanel dropEmployeePanel;
	private static JPanel allEmployeesPanel;
	private static JPanel updateHiringDatePanel;
	private static JFrame window;
	private static Connection myConn;
	private static ArrayList<String> employeesList = new ArrayList<String>();
	static JPanel mainSectionPanel = new JPanel();
	private static JTextField textField;
	
	//Removes all the current panels being shown
	public static void removeAllViews() {
    	dropEmployeePanel.setVisible(false);
    	addEmployeeSection.setVisible(false);
    	allEmployeesPanel.setVisible(false);
    	updateHiringDatePanel.setVisible(false);
	}
	
	//Name: GetAllEmployees
	//Input: None
	//Output: None
	//Purpose:  Runs a SQL query that updates the employeesList ArrayList
	//Get the list of all the employees and add it to the employeesList arrayList
	public static void getAllEmployees() {
		employeesList.clear();
		Statement st;
		try {
			st = myConn.createStatement();
			String getEmployeesQuery = "SELECT * from employees";
			ResultSet rs = st.executeQuery(getEmployeesQuery);
			while(rs.next()) {
				employeesList.add(rs.getInt("EID") + " - " +rs.getString("fName") + " " + rs.getString("lName") + " , " + rs.getDate("DateOfJoining"));
		    }	
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Name: initializeSQL
	//Input: None
	//Output: None
	//Purpose:  Creates a connection with the mySQL server
	public static void initializeSQL() {
		try {
			//Getting connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/employees-schema", "root", "password");		
			
			//Create a table
			String tableQuery = "CREATE table IF NOT EXISTS employees(\n"
					+ "EID integer NOT NULL PRIMARY KEY, \n"
					+ "fName varchar(45) NOT NULL,\n"
					+ "lName varchar(45) NOT NULL,\n"
					+ "DateOfJoining date NOT NULL\n"
					+ ")";
			Statement st = myConn.createStatement();
			st.execute(tableQuery);
		}catch(Exception exc) {
			exc.printStackTrace();
		}finally {
			//Set Up Gui
			buildTopPanel();
			buildAddEmployeesSection();
			buildDropEmployeesSection();
			buildAllEmployeesSection();
			buildUpdateHiringDateSection();
		}
	}

	//Name: buildTopPanel
	//Input: None
	//Output: None
	//Purpose:  Build the panel with the options to select how the user interacts with the site
	public static void buildTopPanel() {
		//Defining the top panel sizes
		topSectionPanel = new JPanel();
		topSectionPanel.setPreferredSize(new Dimension(10, 65));
		window.getContentPane().add(topSectionPanel, BorderLayout.NORTH);

		//Adding the necessary buttons and button functionalities
		
		//-- ADD EMPLOYEES BUTTON
		JButton addEmployeeButton = new JButton("Add Employee");
		addEmployeeButton.setFocusPainted(false);
		addEmployeeButton.setBackground(Color.white);
		addEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	removeAllViews();
            	mainSectionPanel.add(addEmployeeSection);
            	addEmployeeSection.setVisible(true);
}
        });
		topSectionPanel.add(addEmployeeButton);
		
		//DROP EMPLOYEES BUTTON
		JButton dropEmployeeButton = new JButton("Drop Employee");
		dropEmployeeButton.setFocusPainted(false);
		dropEmployeeButton.setBackground(Color.white);
		dropEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	removeAllViews();
            	//Rebuild the employees section
               	buildDropEmployeesSection();
            	dropEmployeePanel.setVisible(true);        		
}
        });
		topSectionPanel.add(dropEmployeeButton);
		
		//UPDATE HIRING DATE BUTTON 
		JButton updateHiringDateButton = new JButton("Update Employee Hiring Date");
		updateHiringDateButton.setFocusPainted(false);
		updateHiringDateButton.setBackground(Color.WHITE);
		updateHiringDateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	removeAllViews();
            	buildUpdateHiringDateSection();
            	updateHiringDatePanel.setVisible(true);
}
        });
		topSectionPanel.add(updateHiringDateButton);
		
		
		//COUNT EMPLOYEES BUTTON
		JButton countEmployeesButton = new JButton("Count Employees");
		countEmployeesButton.setFocusPainted(false);
		countEmployeesButton.setBackground(Color.WHITE);
		countEmployeesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		Statement myStmt = myConn.createStatement();
            		ResultSet rs = myStmt.executeQuery("SELECT count(*) FROM employees");
            		while(rs.next()) {
            			JOptionPane.showMessageDialog(null, "You have " + rs.getString("count(*)") + " employees");
            		}
            		rs.close();
            	}catch(SQLException exc) {
//            			exc.printStackTrace();
            }
            	
        }});
		topSectionPanel.add(countEmployeesButton);
		
		
		//DISPLAY ALL EMPLOYEES BUTTON
		JButton displayAllEmployeesButton = new JButton("Display All Employees");
		displayAllEmployeesButton.setFocusPainted(false);
		displayAllEmployeesButton.setBackground(Color.WHITE);
		displayAllEmployeesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	removeAllViews();
            	//Rebuild the employees section
            	buildAllEmployeesSection();
            	allEmployeesPanel.setVisible(true);   
            	
}
        });
		topSectionPanel.add(displayAllEmployeesButton);
		
		JButton exitButton = new JButton("Exit Button");
		exitButton.setBackground(Color.WHITE);
		
		exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				 JOptionPane.showMessageDialog(null, "Have a great day!");
            	System.exit(0);
            	try {
					myConn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
		topSectionPanel.add(exitButton);
	}

	//Name: buildAddEmployeesSection
	//Input: None
	//Output: None
	//Purpose:  Build the panel with the buildAddEmployees Section
	public static void buildAddEmployeesSection() {
		//Setting up the layout of the add employees section
		addEmployeeSection = new JPanel();
		addEmployeeSection.setLocation(5, 5);
		addEmployeeSection.setSize(new Dimension(500, 200));
		addEmployeeSection.setPreferredSize(new Dimension(500, 200));
		addEmployeeSection.setMinimumSize(new Dimension(100, 100));
		addEmployeeSection.setVisible(false);
		addEmployeeSection.setLayout(new BorderLayout(0, 0));
		
		JLabel addEmployeeLabel = new JLabel("Add Employee");
		addEmployeeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		addEmployeeLabel.setBorder(new EmptyBorder(7, 0, 0, 0));
		addEmployeeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		addEmployeeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		addEmployeeSection.add(addEmployeeLabel, BorderLayout.NORTH);
		
		JPanel inputEmployeeInfoPanel = new JPanel();
		addEmployeeSection.add(inputEmployeeInfoPanel, BorderLayout.CENTER);
		inputEmployeeInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		
		JLabel eidNumber = new JLabel("EID Number");
		inputEmployeeInfoPanel.add(eidNumber);
		
		JTextField eidNumberInput = new JTextField();
		eidNumberInput.setColumns(12);
		inputEmployeeInfoPanel.add(eidNumberInput);

		JLabel firstNameLabel = new JLabel("First Name");
		inputEmployeeInfoPanel.add(firstNameLabel);
		
		JTextField firstNameInput = new JTextField();
		firstNameInput.setColumns(12);
		inputEmployeeInfoPanel.add(firstNameInput);

		JLabel lastNameLabel = new JLabel("Last Name");
		inputEmployeeInfoPanel.add(lastNameLabel);
		
		JTextField lastNameInput = new JTextField();
		lastNameInput.setColumns(12);
		inputEmployeeInfoPanel.add(lastNameInput);
		
		JLabel dateOfEmploymentLabel = new JLabel("Date of Employment");
		inputEmployeeInfoPanel.add(dateOfEmploymentLabel);
		
		JTextField dateOfEmploymentInput = new JTextField();
		dateOfEmploymentInput.setColumns(12);
		inputEmployeeInfoPanel.add(dateOfEmploymentInput);
		
		JButton addEmployeeButton = new JButton("Add Employee");
		addEmployeeButton.setFocusPainted(false);
		addEmployeeButton.setPreferredSize(new Dimension(150, 35));
		addEmployeeButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		addEmployeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addEmployeeButton.setForeground(new Color(255, 255, 255));
		addEmployeeButton.setBackground(new Color(0, 0, 0));
		inputEmployeeInfoPanel.add(addEmployeeButton);
		
		JLabel errorLabel = new JLabel("There has been an error, please check your input");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		errorLabel.setPreferredSize(new Dimension(330, 14));
		errorLabel.setForeground(new Color(255, 0, 0));
		errorLabel.setVisible(false);
		inputEmployeeInfoPanel.add(errorLabel);
		
		JLabel successLabel = new JLabel("The new employee has been added");
		successLabel.setHorizontalAlignment(SwingConstants.CENTER);
		successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		successLabel.setPreferredSize(new Dimension(330, 14));
		successLabel.setForeground(new Color(0, 128, 0));
		successLabel.setVisible(false);
		inputEmployeeInfoPanel.add(successLabel);

		addEmployeeButton.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
//				  System.out.println(firstNameInput.getText());
				  try {
					  
				  errorLabel.setVisible(false);successLabel.setVisible(false);
				  if(eidNumberInput.getText().trim().length() == 0 ) {
					  errorLabel.setText("There has been an error, please enter a valid EID");
					  errorLabel.setVisible(true);
				  }else if(firstNameInput.getText().trim().length()== 0){
					  errorLabel.setText("There has been an error, please enter a valid first name");
					  errorLabel.setVisible(true);
				  }else if(lastNameInput.getText().trim().length()== 0){
					  errorLabel.setText("There has been an error, please enter a valid last name");
					  errorLabel.setVisible(true);
				  }else if(dateOfEmploymentInput.getText().trim().length()== 0){
					  errorLabel.setText("Please enter a valid employment date");
					  errorLabel.setVisible(true);
				  }else {
					  
	              String sqlQuery = "INSERT INTO employees(EID, fName, lName, DateOfJoining)" + "VALUES(?, ?, ?, ?);";
	              PreparedStatement pstmt = myConn.prepareStatement(sqlQuery);
	              pstmt.setInt(1, Integer.parseInt(eidNumberInput.getText()));
	              pstmt.setString(2, firstNameInput.getText());
	              pstmt.setString(3, lastNameInput.getText());
	              pstmt.setString(4, dateOfEmploymentInput.getText());
	              pstmt.executeUpdate();
	              successLabel.setVisible(true);

//	              Resetting the text
	              eidNumberInput.setText("");
	              firstNameInput.setText("");
	              lastNameInput.setText("");
	              dateOfEmploymentInput.setText("");
				  }
				  }catch(SQLException exc) {
//						exc.printStackTrace();
						System.out.println(exc.getMessage());
						if(exc.getMessage().toString().contains("Duplicate entry")) {
							  errorLabel.setText("The inputted EID number already exists");
						}else if(exc.getMessage().toString().contains("Incorrect date value")){
							  errorLabel.setText("Check the date format please");

						}else {
							errorLabel.setText("There has been an error, please check your input");
						}
						  errorLabel.setVisible(true);


				  }	
				}			  
	        
			  
		});
		
	}
	
	//Name: buildAddEmployeesSection
	//Input: None
	//Output: None
	//Purpose:  Build the panel with the buildDropEmployees Section
	public static void buildDropEmployeesSection() {
		dropEmployeePanel = new JPanel();
		dropEmployeePanel.setVisible(false);
		
		
		dropEmployeePanel.setBounds(5, 0, 500, 205);
		dropEmployeePanel.setLayout(null);
		mainSectionPanel.add(dropEmployeePanel);
		JLabel chooseEmployeeLabel = new JLabel("Choose Employee To Delete");
		chooseEmployeeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		chooseEmployeeLabel.setBounds(0, 11, 500, 14);
		dropEmployeePanel.add(chooseEmployeeLabel);
		
		JButton dropEmployeeButton = new JButton("Drop Employee");
		dropEmployeeButton.setPreferredSize(new Dimension(150, 35));
		dropEmployeeButton.setForeground(Color.WHITE);
		dropEmployeeButton.setFocusPainted(false);
		dropEmployeeButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		dropEmployeeButton.setBackground(Color.BLACK);
		dropEmployeeButton.setAlignmentX(0.5f);
		dropEmployeeButton.setBounds(178, 85, 150, 35);
		
		dropEmployeePanel.add(dropEmployeeButton);
		
		getAllEmployees();
	
		String[] employeesArray = employeesList.toArray(new String[0]);
		
		if(employeesArray.length == 0) {
			chooseEmployeeLabel.setText("There are no employees to delete");
		}else {
			chooseEmployeeLabel.setText("Choose Employee To Delete");
		}
		
		JComboBox employeesList = new JComboBox(employeesArray);
		employeesList.setBounds(134, 44, 229, 22);
		dropEmployeePanel.add(employeesList);
		
		dropEmployeeButton.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   String selectedItem = (String)employeesList.getSelectedItem();
				   String[] userEid = selectedItem.split("-");
				   try {
					   String deleteQuery = "DELETE FROM employees WHERE EID = ?";
					   PreparedStatement pstmt = myConn.prepareStatement(deleteQuery);
					   pstmt.setInt(1, Integer.parseInt(userEid[0].trim()));			 
					   pstmt.executeUpdate();
					   JOptionPane.showMessageDialog(null, "Employee Dropped");
					   removeAllViews();
					   pstmt.close();
				   }catch(Exception exc) {
						exc.printStackTrace();
					}
	         }
		});
	}
	
	//Name: buildAllEmployeesSection
	//Input: None
	//Output: None
	//Purpose:  Build the panel to deal with the build All Employees Section
	public static void buildAllEmployeesSection() {
		getAllEmployees();
		allEmployeesPanel = new JPanel();
		allEmployeesPanel.setBounds(5, 0, 500, 205);
		allEmployeesPanel.setLayout(null);
		JList list = new JList(employeesList.toArray(new String[0]));
		
		list.setBackground(SystemColor.control);
		list.setPreferredSize(new Dimension(40, 50));
		list.setCellRenderer(new CenteredCellRenderer());
		list.setBounds(0, 26, 500, 179);
		allEmployeesPanel.add(list);
		allEmployeesPanel.setVisible(false);
		mainSectionPanel.add(allEmployeesPanel);
		
		JLabel lblNewLabel = new JLabel("Employees List");
		list.setBorder(new EmptyBorder(10, 0, 5, 0));

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 490, 15);
		allEmployeesPanel.add(lblNewLabel);
		
	}
	
	//Name: buildAllEmployeesSection
	//Input: None
	//Output: None
	//Purpose:  Build the panel to deal with the build Update Hiring Date Section Section
	public static void buildUpdateHiringDateSection() {
		
		//Setting up the GUI
		getAllEmployees();
		updateHiringDatePanel = new JPanel();
		updateHiringDatePanel.setBounds(5, 0, 500, 205);
		updateHiringDatePanel.setLayout(null);
		mainSectionPanel.add(updateHiringDatePanel);
		updateHiringDatePanel.setVisible(false);
		JComboBox employeesList_1 = new JComboBox(employeesList.toArray(new String[0]));
		employeesList_1.setBounds(131, 42, 226, 22);
		updateHiringDatePanel.add(employeesList_1);
		
		JLabel selectEmployeeToUpdateLabel = new JLabel("Select Employee To Update");
		selectEmployeeToUpdateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		selectEmployeeToUpdateLabel.setBounds(0, 17, 500, 14);
		updateHiringDatePanel.add(selectEmployeeToUpdateLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Updated Date");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 75, 500, 14);
		updateHiringDatePanel.add(lblNewLabel_1);
		
		JTextField newHiringData = new JTextField();
		newHiringData.setBounds(171, 100, 157, 20);
		updateHiringDatePanel.add(newHiringData);
		newHiringData.setColumns(10);
		
		JButton btnUpdateDate = new JButton("Update Date");
		btnUpdateDate.setPreferredSize(new Dimension(150, 35));
		btnUpdateDate.setForeground(Color.WHITE);
		btnUpdateDate.setFocusPainted(false);
		btnUpdateDate.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		btnUpdateDate.setBackground(Color.BLACK);
		btnUpdateDate.setAlignmentX(0.5f);
		btnUpdateDate.setBounds(182, 145, 136, 35);
		JLabel errorLabel = new JLabel("There has been an error, please check your input");
		errorLabel.setBounds(0, 188, 500, 14);
		updateHiringDatePanel.add(errorLabel);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		errorLabel.setPreferredSize(new Dimension(330, 14));
		errorLabel.setForeground(new Color(255, 0, 0));
		errorLabel.setVisible(false);
		
		//Sets the button to update the function
		btnUpdateDate.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   errorLabel.setVisible(false);
	            	try {
	            		if(newHiringData.getText().trim().length() == 0) {
	            			errorLabel.setText("Please enter a new hiring date");
	            			errorLabel.setVisible(true);
	            		} else {
	     
	            		String selectedEmployee = (String) employeesList_1.getSelectedItem();
						String query= "UPDATE employees SET DateOfJoining = ?" + "WHERE EID = ?;";
						PreparedStatement pstmt = myConn.prepareStatement(query);
						
						pstmt.setString(1, newHiringData.getText());
						pstmt.setString(2, selectedEmployee.split("-")[0].trim());
						pstmt.executeUpdate();
            			JOptionPane.showMessageDialog(null, "The hiring date has been changed");
            			pstmt.close();
            			removeAllViews();
	            		}
					} catch (Exception e1) {
						errorLabel.setText("Please check the format of your hiring date");
						errorLabel.setVisible(true);
// 						e1.printStackTrace();
					}
	            	
	            }
		});
		updateHiringDatePanel.add(btnUpdateDate);
		
		
	}
	
	public static void main(String[] args) {
		//Defining window size
		window = new JFrame();
		window.setTitle("Employees Database");
		window.setSize(522, 326);
		
		//Set up SQL
		initializeSQL();
		
	
		mainSectionPanel.setLayout(null);

			
		window.getContentPane().add(mainSectionPanel, BorderLayout.CENTER);
		
		window.setVisible(true);
		
    	removeAllViews();	
	}
}
