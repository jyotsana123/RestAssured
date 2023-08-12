import org.testng.Assert;

import Files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String args[])
	{
		
		JsonPath js = new JsonPath(Payload.CoursePrice());
		
		//1. Print No. of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		
		//2.Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//3. Print Title of the first course
		String title = js.getString("courses[0].title");
		System.out.println(title);
		
		//4. Print All course titles and their respective Prices
                    /*  String title1 = js.getString("courses[0].title");
	                    int price1 = js.getInt("courses[0].price");
		                System.out.println(title +" "+ price1); */
		for(int i=0;i<count;i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			System.out.println(courseTitle +" "+coursePrice);
		}
		
		
		//5. Print no of copies sold by RPA Course
		for(int i=0;i<count;i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			if(courseTitle.equals("RPA"))
			{
				int courseCopies = js.getInt("courses["+i+"].copies");
				System.out.println(courseCopies);
				break;
			}
			
		}
		
		//6. Verify if Sum of all Course prices matches with Purchase Amount
		int sum=0;
		for(int i=0;i<count;i++)
		{
			int coursePrice = js.getInt("courses["+i+"].price");
			int courseCopies = js.getInt("courses["+i+"].copies");
			int price = coursePrice*courseCopies;
			System.out.println(price);
			sum = sum+price;
		}
		System.out.println(sum);
		Assert.assertEquals(purchaseAmount, sum);
		
		
	}
}
