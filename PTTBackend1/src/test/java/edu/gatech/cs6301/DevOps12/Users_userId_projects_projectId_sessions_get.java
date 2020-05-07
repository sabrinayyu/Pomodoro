package edu.gatech.cs6301.DevOps12;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class Users_userId_projects_projectId_sessions_get extends BaseTestCase {

	// *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***

	// Purpose: Test getting a session for a project with an invalid userId
	@Test
	public void pttTest1() throws Exception {
		try {
			String emptyUserId = "";
			String projectId = "11";

			// Get sessions with emptyUserId
			CloseableHttpResponse response = TestUtils.getSessionsForProject(httpclient, emptyUserId, projectId);
			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test getting sessions for a project with a non-existent user
	//CHANGES: Cleanup
	@Test
	public void pttTest2() throws Exception {
		try {
			// Create a user
			String id;
			CloseableHttpResponse createResponse = TestUtils.createUser(httpclient, "John", "Doe",
					TestUtils.generateEmailId("john@doe.org"));
			id = TestUtils.getIdFromResponse(createResponse);

			String nonExistentUserId = id + "123";
			String projectId = "11";

			// Get sessions with nonExistentId
			CloseableHttpResponse response = TestUtils.getSessionsForProject(httpclient, nonExistentUserId, projectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, id);
			cleanupResponse.close();

			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test getting sessions for a project with an invalid projectId
	//CHANGES:  Calls /ptt/users/{userID}/projects//sessions. Needs to return 400 (BAD REQUEST)
	@Test
	public void pttTest3() throws Exception {
		try {
			// Create a user
			String userId;
			CloseableHttpResponse createResponse = TestUtils.createUser(httpclient, "John", "Doe",
					TestUtils.generateEmailId("john@doe.org"));
			userId = TestUtils.getIdFromResponse(createResponse);

			String emptyProjectId = "";

			// Get sessions with emptyProjectId
			CloseableHttpResponse response = TestUtils.getSessionsForProject(httpclient, userId, emptyProjectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userId);
			cleanupResponse.close();

			Assert.assertEquals(400, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test getting a session for a project with a non-existent projectId
	@Test
	public void pttTest4() throws Exception {
		try {
			// Create a user
			String userId;
			CloseableHttpResponse createResponse = TestUtils.createUser(httpclient, "John", "Doe",
					TestUtils.generateEmailId("john@doe.org"));
			userId = TestUtils.getIdFromResponse(createResponse);

			// Create a project for the user created
			CloseableHttpResponse createResponseProject = TestUtils.createProjectForUserId(httpclient, userId,
					"testProject " + userId);
			String projectId = TestUtils.getIdFromResponse(createResponseProject);
			String invalidProjectId = projectId + "11";

			// Get sessions with non-existent ProjectId
			CloseableHttpResponse response = TestUtils.getSessionsForProject(httpclient, userId, invalidProjectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userId);
			cleanupResponse.close();

			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test getting sessions for a project with invalid user mapping for
	// existent userId
	// and existent projectId
	@Test
	public void pttTest5() throws Exception {
		try {
			// Create user 1
			String userOneId;
			CloseableHttpResponse createResponseOne = TestUtils.createUser(httpclient, "John", "Doe",
					TestUtils.generateEmailId("john@doe.org"));
			userOneId = TestUtils.getIdFromResponse(createResponseOne);

			// Create user 2
			String userTwoId;
			CloseableHttpResponse createResponseTwo = TestUtils.createUser(httpclient, "A", "B",
					TestUtils.generateEmailId("a@b.org"));
			userTwoId = TestUtils.getIdFromResponse(createResponseTwo);

			// Create a project1 for the user 1
			CloseableHttpResponse createResponseProjectOne = TestUtils.createProjectForUserId(httpclient, userOneId,
					"testProject " + userOneId);
			String projectIdOne = TestUtils.getIdFromResponse(createResponseProjectOne);

			// Get project1 from user2
			CloseableHttpResponse response = TestUtils.getSessionsForProject(httpclient, userTwoId, projectIdOne);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userOneId);
			cleanupResponse.close();
			cleanupResponse = TestUtils.deleteUserById(httpclient, userTwoId);
			cleanupResponse.close();

			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test getting sessions successfully.
	//CHANGES: Commented out valid assert. Cleanup
	@Test
	public void pttTest6() throws Exception {
		try {
			// Create a user
			String userId;
			CloseableHttpResponse createUserResponse = TestUtils.createUser(httpclient, "John", "Doe",
					TestUtils.generateEmailId("john@doe.org"));
			Assert.assertEquals(201, createUserResponse.getStatusLine().getStatusCode());
			userId = TestUtils.getIdFromResponse(createUserResponse);

			// Create a project for the user created
			CloseableHttpResponse createProjectResponse = TestUtils.createProjectForUserId(httpclient, userId,
					"testProject " + userId);
			Assert.assertEquals(201, createProjectResponse.getStatusLine().getStatusCode());
			String projectId = TestUtils.getIdFromResponse(createProjectResponse);

			// Create a session for the project that was just created.
			CloseableHttpResponse createSessionReponse = TestUtils.createSessionForProject(httpclient, userId,
					projectId, "2019-02-18T20:00Z", "2019-02-18T21:00Z", "2");
			String sessionId = TestUtils.getIdFromResponse(createSessionReponse);
			Assert.assertNotNull(sessionId);

			// Get sessions from a user for a project

//			  TODO: uncomment when backend starts supporting the
//
//			  GET /users/{user_id}/projects/{project_id}/sessions CloseableHttpResponse
//			  endpoint

			CloseableHttpResponse response = TestUtils.getSessionsForProject(httpclient, userId, projectId);
			Assert.assertEquals(200, response.getStatusLine().getStatusCode());

			// Verify if the right session is returned HttpEntity entity =
			HttpEntity entity = response.getEntity();
			String expectedJson =  TestUtils.getSessionArrayAsJsonString(sessionId, "2019-02-18T20:00Z", "2019-02-18T21:00Z", "2");
			//			  System.out.println(EntityUtils.toString(entity));

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userId);
			cleanupResponse.close();
			JSONAssert.assertEquals(expectedJson, EntityUtils.toString(entity), false);

			EntityUtils.consume(response.getEntity()); response.close();

		} finally {
			httpclient.close();
		}
	}
}
