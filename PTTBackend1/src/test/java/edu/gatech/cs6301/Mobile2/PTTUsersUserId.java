package edu.gatech.cs6301.Mobile2;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.gatech.cs6301.Mobile2.Utils.*;

public class PTTUsersUserId {

//Modified test cases to delete any created users/projects

    private String baseUrl = "http://localhost:8080/ptt";
    //private String baseUrl = System.getProperty("baseUrl");
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;
    private JSONObject user;

    @Before
    public void runBefore() throws JSONException {
        if (!setupdone) {
            System.out.println("*** SETTING UP TESTS ***");
            // Increase max total connection to 100
            cm.setMaxTotal(100);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(10);
            // Increase max connections for localhost:80 to 50
            HttpHost localhost = new HttpHost("localhost", 8080);
            cm.setMaxPerRoute(new HttpRoute(localhost), 10);
            httpclient = HttpClients.custom().setConnectionManager(cm).build();
            setupdone = true;
        }
        user = new JSONObject()
                .put("id", 0)
                .put("firstName", "f1")
                .put("lastName", "l1")
                .put("email", "e1");
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        try {
            httpclient = HttpClients.createDefault();
            clearUsers(httpclient, baseUrl);
        } catch (Exception e) {

        }
        System.out.println("*** ENDING TEST ***");
    }

    @Test
    // Purpose: Tests GET call on /users/{userid} with invalid id
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(getUsersUserId(0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with invalid id
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(putUsersUserId(0, new StringEntity(user.toString())), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests DELETE call on /users/{userid} with invalid id
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(deleteUsersUserId(0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with missing id in body
    // Modified test case so that it passes. Given test case should update the user and return status code 200. id field in request body of PUT /users/{userId} can be missing (since it is ignored).
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.remove("id");
            final StringEntity e = new StringEntity(user.toString());
            user.put("id",id);
            runTest(putUsersUserId(id, e), 200, user.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with empty string firstName in body
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.put("firstName", "");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with missing firstName in body
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.remove("firstName");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with empty string lastName in body
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.put("lastName", "");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with missing lastName in body
    public void pttTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.remove("lastName");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with invalid email address in body
    public void pttTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.put("email", "-");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with empty string email in body
    public void pttTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.put("email", "");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with missing email address in body
    public void pttTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            user.remove("email");
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid} with valid userid
    public void pttTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(getUsersUserId(id), 200, user.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid} with valid userid and body
    public void pttTest13() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            final StringEntity e = new StringEntity(user.toString());
            runTest(putUsersUserId(id, e), 200, user.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests DELETE call on /users/{userid} with valid userid
    public void pttTest14() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(deleteUsersUserId(id), 200, user.toString());
            clearUsers(httpclient, baseUrl);
        } finally {
            httpclient.close();
        }
    }


    private CloseableHttpResponse getUsersUserId(int id) throws IOException {
        return getRequest(httpclient,baseUrl + "/users/" + id);
    }

    private CloseableHttpResponse putUsersUserId(int id, StringEntity entity) throws IOException {
        return putRequest(httpclient,baseUrl + "/users/" + id, entity);
    }

    private CloseableHttpResponse deleteUsersUserId(int id) throws IOException {
        return deleteRequest(httpclient,baseUrl + "/users/" + id);
    }
}
