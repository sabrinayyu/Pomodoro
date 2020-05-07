package edu.gatech.cs6301.Mobile2;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.gatech.cs6301.Mobile2.Utils.*;

public class PTTUsers {

//Modified test cases to delete any created users/projects

    private String baseUrl = "http://localhost:8080/ptt";
    //private String baseUrl = System.getProperty("baseUrl");
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;
    private JSONObject user;

    @Before
    public void runBefore() throws JSONException  {
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
    // Purpose: Tests GET call on /users
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            final String user1 = user(0, "f1", "l1", "e1");
            final String user2 = user(0, "f2", "l2", "e2");
            final String user3 = user(0, "f3", "l3", "e3");
            JSONArray array = new JSONArray();
            array.put(new JSONObject(user1));
            array.put(new JSONObject(user2));
            array.put(new JSONObject(user3));
            final String expected = array.toString();

            postUsers(new StringEntity(user1)).close();
            postUsers(new StringEntity(user2)).close();
            postUsers(new StringEntity(user3)).close();

            runTestList(getUsers(), 200, expected, "email");
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with missing id field
    // Modified test case so that it passes. Given test case should create a user and return status code 201. id field in request body of POST /users can be missing (since it is ignored).
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.remove("id");
            final StringEntity e = new StringEntity(user.toString());
            user.put("id",123);
            runTest(postUsers(e), 201, user.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with empty string firstname field
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.put("firstName", "");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with missing firstname field
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.remove("firstName");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with empty string lastname field
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.put("lastName", "");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with missing lastname field
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.remove("lastName");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with empty string email field
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.put("email", "");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with missing email field
    public void pttTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.remove("email");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with a valid email address
    public void pttTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            user.put("email", "example@email.com");
            final StringEntity e = new StringEntity(user.toString());
            runTest(postUsers(e), 201, user.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users with an invalid email address
    public void pttTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(postUsers(new StringEntity(user.toString())), 201, user.toString());
            clearUsers(httpclient, baseUrl);
        } finally {
            httpclient.close();
        }
        
    }

    private CloseableHttpResponse getUsers() throws IOException {
        return getRequest(httpclient,baseUrl + "/users");
    }

    private CloseableHttpResponse postUsers(StringEntity body) throws IOException {
        return postRequest(httpclient,baseUrl + "/users", body);
    }
}
