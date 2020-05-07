package edu.gatech.cs6301.DevOps12;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class Users_userId_projects_projectId_delete extends BaseTestCase {

	// *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***

	// Purpose: Test deleting a project for an invalid userId
	@Test
	public void pttTest1() throws Exception {
		try {
			String emptyUserId = "";
			String projectId = "11";

			// Delete a project with emptyUserId
			CloseableHttpResponse response = TestUtils.deleteProjectByProjectId(httpclient, emptyUserId, projectId);
			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test deleting a project for a non-existent user
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

			// Get projects with nonExistentId
			CloseableHttpResponse response = TestUtils.deleteProjectByProjectId(httpclient, nonExistentUserId,
					projectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, id);
			cleanupResponse.close();

			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test deleting a project for an invalid projectId
	@Test
	public void pttTest3() throws Exception {
		try {
			// Create a user
			String userId;
			CloseableHttpResponse createResponse = TestUtils.createUser(httpclient, "John", "Doe",
					TestUtils.generateEmailId("john@doe.org"));
			userId = TestUtils.getIdFromResponse(createResponse);

			String emptyProjectId = "";

			// Get a project with emptyProjectId
			CloseableHttpResponse response = TestUtils.deleteProjectByProjectId(httpclient, userId, emptyProjectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userId);
			cleanupResponse.close();

			Assert.assertEquals(405, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test deleting a project for a non-existent projectId
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

			// Get a project with non-existent ProjectId
			CloseableHttpResponse response = TestUtils.deleteProjectByProjectId(httpclient, userId, invalidProjectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userId);
			cleanupResponse.close();

			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test invalid user mapping for existent userId and existence
	// projectId
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
					"testProject 1 " + userOneId);
			String projectIdOne = TestUtils.getIdFromResponse(createResponseProjectOne);

			// Try deleting project1 from user2
			CloseableHttpResponse response = TestUtils.deleteProjectByProjectId(httpclient, userTwoId, projectIdOne);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteProjectByProjectId(httpclient, userOneId, projectIdOne);
			cleanupResponse.close();
			cleanupResponse = TestUtils.deleteUserById(httpclient, userOneId);
			cleanupResponse.close();
			cleanupResponse = TestUtils.deleteUserById(httpclient, userTwoId);
			cleanupResponse.close();

			Assert.assertEquals(404, response.getStatusLine().getStatusCode());

			response.close();
		} finally {
			httpclient.close();
		}
	}

	// Purpose: Test deleting a project successfully
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

			// Delete a project from a user
			CloseableHttpResponse response = TestUtils.deleteProjectByProjectId(httpclient, userId, projectId);
			Assert.assertEquals(200, response.getStatusLine().getStatusCode());

			// Verify if the project has been deleted
			HttpEntity entity = response.getEntity();
			String expectedJson = TestUtils.getProjectObjectAsJsonString(projectId, "testProject " + userId);
			JSONAssert.assertEquals(expectedJson, EntityUtils.toString(entity), false);

			CloseableHttpResponse getResponse = TestUtils.getProjectByProjectId(httpclient, userId, projectId);

			CloseableHttpResponse cleanupResponse = TestUtils.deleteUserById(httpclient, userId);
			cleanupResponse.close();

			Assert.assertEquals(404, getResponse.getStatusLine().getStatusCode());

			EntityUtils.consume(response.getEntity());
			response.close();
		} finally {
			httpclient.close();
		}
	}

}
