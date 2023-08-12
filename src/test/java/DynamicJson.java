import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.Payload;
import Files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//Section 7: Handling dynamic json Payloads with Parameterization
//Video 33

public class DynamicJson {
	
	//@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json").body(Payload.addBook(isbn, aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).body("Msg", equalTo("successfully added"))
		.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	//@Test(dataProvider="BooksId")
	public void deleteBook(String id)
	{
		RestAssured.baseURI = "http://216.10.245.166";
		given().header("Content-Type", "application/json").body("{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+id+"\"\r\n"
				+ " \r\n"
				+ "} ")
		.when().delete("Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200);
	}
	
	@Test(dataProvider="BooksData")
	public void delete(String isbn, String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json").body(Payload.addBook(isbn, aisle))
		.when().post("Library/Addbook.php")
		.then().assertThat().statusCode(200).body("Msg", equalTo("successfully added"))
		.extract().response().asString();
		
		given().header("Content-Type", "application/json").body("{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+isbn+aisle+"\"\r\n"
				+ " \r\n"
				+ "} ")
		.when().delete("Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200);
	}
	
	//Video: 35- Understanding testng data provider for parameterization
	//array: collection of elements
	//Multidimensional array: collection of arrays
	
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{
		return new Object[][] {{"aab","1010"},{"aac","1011"},{"aad","1012"}};
	}

	//@DataProvider(name="BooksId")
	public Object[] getId()
	{
		return new Object[] {"aab1010","aac1011","aad1012"};
	}

}

/* Practical problems:
* Dynamically build json payload with external data input
* Parameterize the API test with multiple data sets
* How to send static json file(payloads) directly into post method of Rest Assured */ 