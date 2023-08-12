import org.testng.Assert;
import org.testng.annotations.Test;

import Files.Payload;
import io.restassured.path.json.JsonPath;

//6. Verify if Sum of all Course prices matches with Purchase Amount

public class SumValidation {

	@Test
	public void sumOfCourses()
	{
		int sum = 0;
		JsonPath js = new JsonPath(Payload.CoursePrice());
		int count = js.getInt("courses.size()");
		for(int i=0;i<count;i++)
		{
			int coursePrice = js.getInt("courses["+i+"].price");
			int courseCopies = js.getInt("courses["+i+"].copies");
			int amount = coursePrice*courseCopies;
			System.out.println(amount);
			sum = sum+amount;
		}
		
		System.out.println(sum); 
		//2.Print Purchase Amount
				int purchaseAmount = js.getInt("dashboard.purchaseAmount");
				System.out.println(purchaseAmount);
				//compare purchase amount to sum of all courses amount
				Assert.assertEquals(purchaseAmount, sum);
	}
}
