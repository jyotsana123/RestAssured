import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import Files.Payload;
import Files.ReUsableMethods;

public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Validate if Add place API is working
		//Rest assured working on given, when, then principle
		//given - all input details
		//when - submit the API - resource and HTTP method comes inside when
		//then - validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(Payload.addPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		
		
		//Add Place -> Update place with new address -> Get place to validate if new address is updated or not
		//after add place we got the place id, By using place id we can update the address
		//we need to parse place id from response body and pass place id to update the record
		
		System.out.println(response);
		JsonPath js = new JsonPath(response);  //for parsing json
		//JsonPath class is the one which takes string as a input and convert that into Json
		//and it will help to parse the json
		//there is lots of methods exposed by the JsonPath which is help us to parse the json
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200);
		
		//Update place
		
		String address = "Saurabh vihar, Delhi";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+address+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200)
		.body("msg", equalTo("Address successfully updated"));
		
		//Get place
		String newAddress = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(newAddress);
//		JsonPath js1 = new JsonPath(newAddress); //we can use this as a reusable method
//		String actualAddress = js1.getString("address");
		JsonPath js1 = ReUsableMethods.rawToJson(newAddress);
		String actualAddress = js1.getString("address");
		
		System.out.println(actualAddress);
		
		Assert.assertEquals(actualAddress, address);
	}

	

}
