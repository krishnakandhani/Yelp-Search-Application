package populate;

import java.io.BufferedReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class Pop_review 
{
 
    public void run_review() throws SQLException
    {
        // TODO Auto-generated method stub
        Connection dbConnection = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO REVIEWS"
                + "(funny_vote, useful_vote, cool_vote, user_id, review_id, stars, r_date, r_text, r_type, bid) VALUES"
                        + "(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),?,?,?)";
         
        JSONParser parser = new JSONParser();
        try
        {
            dbConnection = getDBConnection();
            ps = dbConnection.prepareStatement(sql);
             
            FileReader filereader = new FileReader("C:/Users/Mahadev$/eclipse-workspace/Yelp/src/populate/yelp_review.json");
            BufferedReader bufferedReader = new BufferedReader(filereader);
            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                //System.out.println(line);
                
                JSONObject votes = (JSONObject) jsonObject.get("votes");
                int funny_votes;
                int useful_votes;
                int cool_votes;
                funny_votes = ((Long) votes.get("funny")).intValue();
                useful_votes = ((Long) votes.get("useful")).intValue();
                cool_votes = ((Long) votes.get("cool")).intValue();
                ps.setInt(1, funny_votes);
                ps.setInt(2, useful_votes);
                ps.setInt(3, cool_votes);
                 
                String user_id = (String) jsonObject.get("user_id");
                ps.setString(4, user_id);
                 
                String review_id = (String) jsonObject.get("review_id");
                ps.setString(5, review_id);
                 
                int stars = ((Long) jsonObject.get("stars")).intValue();
                ps.setInt(6, stars);
                 
                String date_string = (String) jsonObject.get("date");
    			Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.MONTH, Integer.parseInt(date_string.split("-")[1])-1);
                calendar.set(Calendar.YEAR,Integer.parseInt(date_string.split("-")[0]));
                Date date = (Date) calendar.getTime();
                
                java.sql.Date d = new java.sql.Date(date.getTime());
                ps.setString(7, d.toString());
                
                String text = (String) jsonObject.get("text");
                ps.setString(8, text);
                 
                String type = (String) jsonObject.get("type");
                ps.setString(9, type);
                 
                String business_id = (String) jsonObject.get("business_id");
                ps.setString(10, business_id);
                 
                ps.executeUpdate();
                 
            }
            filereader.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e) 
        {
 
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ps != null) 
            {
                ps.close();
            }
            if (dbConnection != null) 
            {
                dbConnection.close();
            }
        }
 
    }
    public static Connection getDBConnection() 
    {
 
        Connection dbConnection = null;
 
        try {
 
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        try {
 
            dbConnection = DriverManager.getConnection(
            		"jdbc:oracle:thin:@localhost:1521:globalDB", "scott", "tiger");
            return dbConnection;
 
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        return dbConnection;
 
    }       
 
}
