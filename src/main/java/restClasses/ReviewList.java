package restClasses;

import java.sql.*;
import java.util.*;


import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;



//Class to query the database for reviews and to hold a list of these reviews 
public class ReviewList {

    //member variable to store reviews
    private final ArrayList<Review> review_list;

    //Constructer that logs into database, gets all restaurant reviews, creates a Review object for each, and adds them to the review_list variable    
    public ReviewList(){


        //Parameters to log into database
        String url = "jdbc:mysql://localhost:3306/preferate";
        String username = Globals.dbuser;
        String password = Globals.pass;

        System.out.println("Connecting database...");

        //Try to connect to the database
        try (Connection connection = DriverManager.getConnection(url, username,password)) {
            System.out.println("Database connected!");

            //create SQL statment to query for reviews
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM `restaurant_reviews`";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            //prepare review_list to add reviews
            review_list = new ArrayList<Review>();

            //Loop over all reviews returned from query 
            while (rs.next()) {

                //create a Review object for each
                Review r = new Review(rs.getLong("user_id"), rs.getInt("restaurant_id"), rs.getInt("restaurant_review"), 
                    rs.getFloat("food_rating"), rs.getFloat("menu_rating"), rs.getFloat("service_rating"), rs.getString("comments"),rs.getString("restaurant_name") );

                //Add to the list
                this.review_list.add(r);             

            }

            //close connection
            connection.close();



            //Error case. Check if database 
        } catch (SQLException e) {
            System.out.println(e);
            throw new IllegalStateException("Cannot connect the database!", e);
        }


    }


    //Contructor that takes in a userID and gets all restatuant reviews for that particular user by retrieving from database
    public ReviewList(long userID){

        //Parameters to log into database
        String url = "jdbc:mysql://localhost:3306/preferate";
        String username = Globals.dbuser;
        String password = Globals.pass;

        System.out.println("Connecting database...");

        //Try to connect to the database
        try (Connection connection = DriverManager.getConnection(url, username,password)) {
            System.out.println("Database connected!");

            //create SQL statment to query for reviews
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM `restaurant_reviews` WHERE user_id =" + userID;
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            //prepare review_list to add reviews
            review_list = new ArrayList<Review>();

            //Loop over all reviews returned from query 
            while (rs.next()) {

                //create a Review object for each
                Review r = new Review(rs.getInt("user_id"), rs.getInt("restaurant_id"), rs.getInt("restaurant_review"), 
                    rs.getFloat("food_rating"), rs.getFloat("menu_rating"), rs.getFloat("service_rating"), rs.getString("comments"),rs.getString("restaurant_name") );

                //Add to the list
                this.review_list.add(r);             

            }

            //close connection
            connection.close();



            //Error case. Check if database 
        } catch (SQLException e) {
            System.out.println(e);
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }



    //getter function to return the list. Needed so that Jackson can convert this to json when sending to front-end
    public ArrayList<Review> getreview_list(){
        return review_list;
    }

    //adds a review to the review_list privit variable
    public void addReview(Review r){
        this.review_list.add(r);
    }

    //returns a datamodel of one column of the reviews data for use in the recommender

    public static JDBCDataModel getReviewsRecommender(String param){

     MysqlDataSource dataSource = new MysqlDataSource();

     dataSource.setServerName("jdbc:mysql://localhost:3306/");
     dataSource.setUser("root");
     dataSource.setPassword(Globals.pass);
     dataSource.setDatabaseName("preferate");

     JDBCDataModel dataModel;

     if(param != null){
    	 dataModel = new MySQLJDBCDataModel(
         dataSource, "restaurant_reviews", "user_id",
         "restaurant_id", param+"_rating", null);
     }
     else{
         dataModel = new MySQLJDBCDataModel(
         dataSource, "restaurant_reviews", "user_id",
         "restaurant_id", "menu_rating", null);   
     }

     return dataModel;

    }



}