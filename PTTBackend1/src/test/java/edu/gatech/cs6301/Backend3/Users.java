package edu.gatech.cs6301.Backend3;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gatech.cs6301.Backend3.Model.User;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class Users {
    Util util;
    private ObjectMapper objectMapper = new ObjectMapper();
    private boolean setupdone;

    @Before
    public void runBefore() throws IOException {
        if (!setupdone) {
            System.out.println("*** SETTING UP TESTS ***");
            util = new Util();
            setupdone = true;
        }
        util.deleteAllUsers();

        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        try {
            util.deleteAllUsers();
            util.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    /**
     * Test create a correct new user with valid first name, last name and email.
     * The response should have a 201 status.
     * The response body should contain same user except ID.
     */
    public void testAddUserSuccess() throws IOException {
        User user = new User("Qifan", "Zhang", util.genEmail());
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.CREATED);
        String responseBody = EntityUtils.toString(response.getEntity());
        User responseObject = objectMapper.readValue(responseBody, User.class);
        assert (user.equalExceptId(responseObject));
        response.close();
    }

    @Test
    /**
     * Test create a user with empty first name
     * Should return 400 status
     */
    public void testAddUserEmptyFirstName() throws IOException {
        User user = new User("", "Zhang", util.genEmail());
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.BAD_REQUEST);
        response.close();
    }


    /**
     * Test create a user with Invalid first name
     * Should return 400 status
     */

    /**
     * Skipping this test, due to the fact that we want to allow project names, first names, & last names
     * to have non-alphanumeric characters. For example, many applications (like the iPhone contacts app)
     * will support emojis in the first name and last name field. If our app ever imports contacts
     * from those services, we might have problems unless we support that as well.
     *
     * Since we already have tests for valid input, we have just ignored this test as opposed to modified it.
     @Test
    public void testAddUserInvalidFirstName() throws IOException {
        User user = new User("***@{", "Zhang", util.genEmail());
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.BAD_REQUEST);
        response.close();
    }
     */

    @Test
    /**
     * Test create a user with empty last name
     * Should return 400 status
     */
    public void testAddUserEmptyLastName() throws IOException {
        User user = new User("Qifan", "", util.genEmail());
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.BAD_REQUEST);
        response.close();
    }


    /**
     * Test create a user with invalid last name
     * Should return 400 status
     */
    /**
     * Skipping this test, due to the fact that we want to allow project names, first names, & last names
     * to have non-alphanumeric characters. For example, many applications (like the iPhone contacts app)
     * will support emojis in the first name and last name field. If our app ever imports contacts
     * from those services, we might have problems unless we support that as well.
     *
     * Since we already have tests for valid input, we have just ignored this test as opposed to modified it.
     @Test
    public void testAddUserInvalidLastName() throws IOException {
        User user = new User("Qifan", "#@$%", util.genEmail());
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.BAD_REQUEST);
        response.close();
    }
     */

    @Test
    /**
     * Test create a user with empty email
     * Should return 400 status
     */
    public void testAddUserEmptyEmail() throws IOException {
        User user = new User("Qifan", "Zhang", "");
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.BAD_REQUEST);
        response.close();
    }

    @Test
    /**
     * Test create a user with invalid email
     * Should return 400 status
     */
    /**
     * We do not have an oracle to determine that an email is valid. This is not feasible
     * unless we somehow have an email server to test whether the email and domain actually exist.
     *
     * Since we already have tests for valid input, we have just ignored this test as opposed to modified it.
     @Test
    public void testAddUserInvalidEmail() throws IOException {
        User user = new User("Qifan", "zhang", "@$#)(");
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.BAD_REQUEST);
        response.close();
    }

    @Test
    /**
     * Test create user with duplicate email
     * Should return 409 status
     */
    public void testAddUserDuplciateEmail() throws IOException {
        String duplicateEmail = util.genEmail();
        User user = new User("Qifan", "Zhang", duplicateEmail);
        CloseableHttpResponse response = util.addUserWithResponse(user);
        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.CREATED);
        String responseBody = EntityUtils.toString(response.getEntity());
        User responseObject = objectMapper.readValue(responseBody, User.class);
        assert (user.equalExceptId(responseObject));

        User user2 = new User("La", "Damian", duplicateEmail);
        CloseableHttpResponse response2 = util.addUserWithResponse(user2);
        assert (response2.getStatusLine().getStatusCode() == HttpStatusCode.CONFLICT);
    }

    @Test
    /**
     * Test get users method
     * should return exactly the same users as we created
     */
    public void testGetAllUsers() throws IOException {
        util.deleteAllUsers();
        User user1 = util.addUserSuccess();
        User user2 = util.addUserSuccess();
        CloseableHttpResponse response = util.getAllUsers();
        String responseBody = EntityUtils.toString(response.getEntity());

        assert (response.getStatusLine().getStatusCode() == HttpStatusCode.SUCCESS);
        User[] users = objectMapper.readValue(responseBody, User[].class);
        assert(users.length == 2);
        assert(users[0].equals(user1));
        assert(users[1].equals(user2));
    }

}
