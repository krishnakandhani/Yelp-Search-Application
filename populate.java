package populate;
import java.sql.SQLException;


public class populate 
{

	public static void main(String[] args) throws SQLException 
	{

		System.out.println("Populating Starts");
		Pop_user user = new Pop_user();
		user.run_user();
		System.out.println("Populated User");
		
		Pop_business business = new Pop_business();
		business.run_business();
		System.out.println("Populated Business");
         
		Pop_review review = new Pop_review();
		review.run_review();
		System.out.println("Populated Review");
	}

}
