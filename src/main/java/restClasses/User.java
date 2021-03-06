package restClasses;

import java.sql.*;
import java.util.*;

//Class to hold information about a user
public class User {

    //member variables to store attributes from DB
    private long user_id;
    private String user_name;
    private String diet_type;
    private String user_allergy;
    private String gluten;
    private String kosher;
    private String lactose;
    private String meats;
    private String eating_environment;

    //constructor that sets all member values
    public User (Long user_id, String user_name, String diet_type, String user_allergy, String gluten, String kosher, String lactose, String meats, String eating_environment){
        this.user_id = user_id;
        this.user_name = user_name;
        this.diet_type = diet_type;
        this.user_allergy = user_allergy;
        this.gluten = gluten;
        this.kosher = kosher;
        this.lactose = lactose;
        this.meats = meats;
        this.eating_environment = eating_environment;

    }


    //constructor that creates user class from only userID
    public User (Long user_id){
        this.user_id = user_id;
        
        //Parameters to log into database
        String url = "jdbc:mysql://localhost:3306/preferate";
        String username = "root";
        //String password = "CrackerWindow654";
        String password = Globals.pass;

        System.out.println("Connecting database...");

        //Try to connect to the database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");

            //check to see if user is already in database
            Statement userCheck = connection.createStatement();
            String userQuery = "SELECT * FROM user WHERE user_id =" + this.user_id;
            PreparedStatement userPreparedStmt = connection.prepareStatement(userQuery);
            ResultSet rs = userPreparedStmt.executeQuery();
        
            if(rs.next()){

                this.user_name = rs.getString("user_name");
                this.diet_type = rs.getString("diet_type");
                this.user_allergy = rs.getString("user_allergy");
                this.gluten = rs.getString("gluten");
                this.kosher = rs.getString("kosher");
                this.lactose = rs.getString("lactose");
                this.meats = rs.getString("meats");
                this.eating_environment = rs.getString("eating_environment");

            }

        }catch (SQLException e) {
            System.out.println(e);
            throw new IllegalStateException("Cannot connect the database!", e);
        }




    }    


    //Connect to Database and insert Account
    public void insertOrEditUser(){


        //Parameters to log into database
        String url = "jdbc:mysql://localhost:3306/preferate";
        String username = "root";
        //String password = "CrackerWindow654";
        String password = Globals.pass;

        System.out.println("Connecting database...");

        //Try to connect to the database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");

            //check to see if user is already in database
            Statement userCheck = connection.createStatement();
            String userQuery = "SELECT * FROM user WHERE user_id =" + this.user_id;
            PreparedStatement userPreparedStmt = connection.prepareStatement(userQuery);
            ResultSet rs = userPreparedStmt.executeQuery();

            if(rs.next()){
                Statement editStmt = connection.createStatement();
                String editQuery = "UPDATE user SET diet_type = ?, user_allergy = ?, gluten = ?, kosher = ?, lactose = ?, meats = ?, eating_environment = ? WHERE user_id = ?";
                PreparedStatement editPreparedStmt = connection.prepareStatement(editQuery);
                
                //set all values for prepared statement
                editPreparedStmt.setString(1, this.diet_type);
                editPreparedStmt.setString(2, this.user_allergy);
                editPreparedStmt.setString(3, this.gluten);
                editPreparedStmt.setString(4, this.kosher);
                editPreparedStmt.setString(5, this.lactose);
                editPreparedStmt.setString(6, this.meats);
                editPreparedStmt.setString(7, this.eating_environment);
                editPreparedStmt.setLong(8, this.user_id);

                //execute query to update user
                editPreparedStmt.execute();

                //close connection
                connection.close();

                return;
            }

            else{

                //create SQL statment to query to insert the user
                Statement insertStmt = connection.createStatement();
                String insertQuery = "INSERT INTO user (user_id, user_name, diet_type, user_allergy, gluten, kosher, lactose, meats, eating_environment)" + 
                                " VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement insertPreparedStmt = connection.prepareStatement(insertQuery);
                
                //set all values for prepared statment
                insertPreparedStmt.setLong(1, this.user_id); 
                insertPreparedStmt.setString(2, this.user_name);
                insertPreparedStmt.setString(3, this.diet_type);
                insertPreparedStmt.setString(4, this.user_allergy);
                insertPreparedStmt.setString(5, this.gluten);
                insertPreparedStmt.setString(6, this.kosher);
                insertPreparedStmt.setString(7, this.lactose);
                insertPreparedStmt.setString(8, this.meats);
                insertPreparedStmt.setString(9, this.eating_environment);

                //execute query to insert user
                insertPreparedStmt.execute();

                //close connection
                connection.close();

                return;

            }



            //Error case. Check if database 
        } catch (SQLException e) {
            System.out.println(e);
            throw new IllegalStateException("Cannot connect the database!", e);
        }


    }

    //Function to edit the given user's preferences. Edit the values of the object with the setters functions before calling this
    // public void editPreferences(){


    //     //Parameters to log into database
    //     String url = "jdbc:mysql://localhost:3306/preferate";
    //     String username = "root";
    //     String password = "CrackerWindow654";

    //     System.out.println("Connecting database...");

    //     //Try to connect to the database
    //     try (Connection connection = DriverManager.getConnection(url, username, password)) {
    //         System.out.println("Database connected!");

    //         //create SQL statement to query to edit preference information
    //         Statement stmt = connection.createStatement();
    //         String query = "UPDATE user SET diet_type = ?, user_allergy = ?, gluten = ?, kosher = ?, lactose = ?, meats = ?, eating_environment = ? WHERE user_id = ?";
    //         PreparedStatement preparedStmt = connection.prepareStatement(query);
            
    //         //set all values for prepared statement
    //         preparedStmt.setString(1, this.diet_type);
    //         preparedStmt.setString(2, this.user_allergy);
    //         preparedStmt.setString(3, this.gluten);
    //         preparedStmt.setString(4, this.kosher);
    //         preparedStmt.setString(5, this.lactose);
    //         preparedStmt.setString(6, this.meats);
    //         preparedStmt.setString(7, this.eating_environment);
    //         preparedStmt.setInt(8, this.user_id);

    //         //execute query to update user
    //         preparedStmt.execute();

    //         //close connection
    //         connection.close();



    //         //Error case. Check if database 
    //     } catch (SQLException e) {
    //         System.out.println(e);
    //         throw new IllegalStateException("Cannot connect the database!", e);
    //     }
    // }



    //getter functions for each attribute needed for Jackson to convert to json for front-end
    public Long getUser_id(){
        return user_id;
    }
    public void setUser_id(int user_id){
        this.user_id = user_id;
    }


    public String getUser_name(){
        return user_name;
    }
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }


    public String getDiet_type(){
        return diet_type;
    }
    public void setDiet_type(String diet_type){
        this.diet_type = diet_type;
    }


    public String getUser_allergy(){
        return user_allergy;
    }
    public String getGluten(){
        return gluten;
    }
    public String getKosher(){
        return kosher;
    }
    public String getLactose(){
        return lactose;
    }
    public String getMeats(){
        return meats;
    }
    public String getEating_environment(){
        return eating_environment;
    }


}