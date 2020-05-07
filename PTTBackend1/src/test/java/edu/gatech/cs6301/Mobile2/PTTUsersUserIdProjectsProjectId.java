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
import static edu.gatech.cs6301.Mobile2.Utils.deleteRequest;

public class PTTUsersUserIdProjectsProjectId {

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
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid} with invalid userid
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(getUsersUserIdProjectsProjectId(0, 0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid} with invalid userid
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(putUsersUserIdProjectsProjectId(0, 0, new StringEntity(project(0, "name"))), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests DELETE call on /users/{userid}/projects/{projectid} with invalid userid
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(deleteUsersUserIdProjectsProjectId(0, 0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid} with invalid projectid
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(getUsersUserIdProjectsProjectId(id, 0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid} with invalid projectid
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(putUsersUserIdProjectsProjectId(id, 0, new StringEntity(project(0, "name"))), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests DELETE call on /users/{userid}/projects/{projectid} with invalid projectid
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(deleteUsersUserIdProjectsProjectId(id, 0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid} with missing id in body
    // Modified test case so that it passes. Given test case should update the project and return status code 200. id field in request body of PUT /users/{userId}/projects/{projectId} can be missing (since it is ignored).
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            project.remove("id");
            final StringEntity e = new StringEntity(project.toString());
            project.put("id",projectid);
            runTest(putUsersUserIdProjectsProjectId(userid, projectid, e), 200, project.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid} with empty string projectname in body
    public void pttTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            project.put("projectname", "");
            runTest(putUsersUserIdProjectsProjectId(userid, projectid, new StringEntity(project.toString())), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid} with missing projectname in body
    public void pttTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            project.remove("projectname");
            runTest(putUsersUserIdProjectsProjectId(userid, projectid, new StringEntity(project.toString())), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid} with valid ids
    public void pttTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            runTest(getUsersUserIdProjectsProjectId(userid, projectid), 200, project.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid} with valid ids and body
    public void pttTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            runTest(putUsersUserIdProjectsProjectId(userid, projectid, new StringEntity(project.toString())), 200, project.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests DELETE call on /users/{userid}/projects/{projectid} with valid ids
    public void pttTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            runTest(deleteUsersUserIdProjectsProjectId(userid, projectid), 200, project.toString());
            clearUsers(httpclient, baseUrl);
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse getUsersUserIdProjectsProjectId(int userid, int projectid) throws IOException {
        return getRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid);
    }

    private CloseableHttpResponse putUsersUserIdProjectsProjectId(int userid, int projectid, StringEntity entity) throws IOException {
        return putRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid, entity);
    }

    private CloseableHttpResponse deleteUsersUserIdProjectsProjectId(int userid, int projectid) throws IOException {
        return deleteRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid);
    }
}
