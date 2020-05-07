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

public class PTTUserUserIdProjects {

//Modified test cases to delete any created users/projects

    private String baseUrl = "http://localhost:8080/ptt";
    //private String baseUrl = System.getProperty("baseUrl");
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;
    private JSONObject project;

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
        project = new JSONObject()
                .put("id", 0)
                .put("projectname", "name");
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
    // Purpose: Tests GET call on /users/{userid}/projects with invalid userid
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(getUsersUserIdProjects(0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects with invalid userid
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(postUsersUserIdProjects(0, new StringEntity(project(0, "name"))), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects with missing id in body
    // Modified test case so that it passes. Given test case should create a project for the user and return status code 201. id field in request body of POST /users/{userId}/projects can be missing (since it is ignored).
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            project.remove("id");
            final StringEntity e = new StringEntity(project.toString());
            project.put("id",123);
            runTest(postUsersUserIdProjects(id, e), 201, project.toString());
            clearUsers(httpclient, baseUrl);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects with empty string projectname in body
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            project.put("projectname", "");
            final StringEntity e = new StringEntity(project.toString());
            runTest(postUsersUserIdProjects(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects with missing projectname in body
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            project.remove("projectname");
            final StringEntity e = new StringEntity(project.toString());
            runTest(postUsersUserIdProjects(id, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            final String project1 = project(0, "name1");
            final String project2 = project(0, "name2");
            JSONArray array = new JSONArray();
            array.put(new JSONObject(project1));
            array.put(new JSONObject(project2));
            final String expected = array.toString();

            postUsersUserIdProjects(id, new StringEntity(project1)).close();
            postUsersUserIdProjects(id, new StringEntity(project2)).close();

            runTestList(getUsersUserIdProjects(id), 200, expected, "projectname");
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects with valid in body
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            final StringEntity e = new StringEntity(project.toString());
            runTest(postUsersUserIdProjects(id, e), 201, project.toString());
            clearUsers(httpclient, baseUrl);
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse getUsersUserIdProjects(int userid) throws IOException {
        return getRequest(httpclient,baseUrl + "/users/" + userid + "/projects");
    }

    private CloseableHttpResponse postUsersUserIdProjects(int userid, StringEntity entity) throws IOException {
        return postRequest(httpclient,baseUrl + "/users/" + userid + "/projects", entity);
    }
}
