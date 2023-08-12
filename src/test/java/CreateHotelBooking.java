import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class CreateHotelBooking {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		given().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"firstname\" : \"Jyotsana\",\r\n"
				+ "    \"lastname\" : \"Pandey\",\r\n"
				+ "    \"totalprice\" : 100,\r\n"
				+ "    \"depositpaid\" : true,\r\n"
				+ "    \"bookingdates\" : {\r\n"
				+ "        \"checkin\" : \"2023-05-12\",\r\n"
				+ "        \"checkout\" : \"2023-06-12\"\r\n"
				+ "    },\r\n"
				+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
				+ "}")
		.when().log().all().post("booking").then().statusCode(200);
}
}