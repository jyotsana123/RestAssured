package Files;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

/* Step to automate Jira application
1. login to jira to create session using login API
2. Add a comment to existing issue using add comment API
3. Add an attachment to existing issue using add attachment API
4. Get Issue details and verify if added comment and attachment exist using Get issue API

New topics which are covered from above examples:
	how to create session filter for authentication in Rest Assured Automation
	in introducing Path parameter and Query parameters together Single test 
	Sending Files as attachments using rest assured with multipart method
	Parsing complex json  and limiting json response through query parameter
	Handling HTTPS certification validation through automated code. */

//Video-44: Importance of session filter cookie in rest assured
//46. Sending Attachments to Rest API using MultiPart method in Rest Assured
//47. Integrating Query Params and Path Params in single test to restrict results

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI = "http://localhost:8080";
		
		//Login scenario
		//With the help of session filter we can extract the session id from the response
		//Whenever you have any scenario which is being treated as a session cookie, for this in rest assured there is one sortcut sessionfilter class
		//you need to only apply the filter method and that will be applied to all your requests.
		SessionFilter session = new SessionFilter();
		String response = given().log().all().filter(session).header("Content-Type","application/json").body("{ \"username\": \"jyotsana\", \r\n"
				+ "  \"password\": \"Annunishu\" \r\n"
				+ "}").when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
		//Add comment
		String addCommentResponse = given().pathParam("id", "10000").log().all().header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \"This is my first comment\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.get("id");
		
		//Add attachment
		//In jira if you want to send any file or attachment then there is one method multiPart() and file argument we need to pass inside the multiPart method
		//file path you can not pass directly, you need to pass file path in file class object
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", "10000").multiPart("file", new File("jira.txt"))
		.header("Content-Type", "multipart/form-data")
		.when().post("/rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(200);
		
		//Get issue details with selected field in query parameter
		//you can check response in json edit online
		String issueDetail = given().log().all().pathParam("id", "10000").queryParam("fields", "comment").filter(session)
		.when().get("/rest/api/2/issue/{id}").then().log().all().extract().response().asString();
		System.out.println(issueDetail);
		
		JsonPath js1 = new JsonPath(issueDetail);
		int commentCount = js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentCount;i++)
		{
			System.out.println(js1.getInt("fields.comment.comments["+i+"].id"));
		}
	}

}
