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

public class PTTUsersUserIdProjectsProjectIdSessions {

    private String baseUrl = "http://localhost:8080/ptt";
    //private String baseUrl = System.getProperty("baseUrl");
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;
    private JSONObject session;

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
        session = new JSONObject()
                .put("id", 0)
                .put("startTime", "2019-02-18T20:00Z")
                .put("endTime", "2019-02-18T20:10Z")
                .put("counter", 1);
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
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/sessions with invalid userid
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(getUsersUserIdProjectsProjectIdSessions(0, 0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with invalid userid
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(postUsersUserIdProjectsProjectIdSessions(0, 0, new StringEntity(session.toString())), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/sessions with invalid projectid
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(getUsersUserIdProjectsProjectIdSessions(id, 0), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with invalid projectid
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(postUsersUserIdProjectsProjectIdSessions(id, 0, new StringEntity(session.toString())), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with missing id in body
    // Modified test because missing id in body for POST sessions should create a session object, Expected Status Code: 201
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.remove("id");
            final StringEntity e = new StringEntity(session.toString());
            session.put("id",123);
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 201, session.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with missing startTime in body
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.remove("startTime");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with empty string startTime in body
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("startTime", "");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with invalid format startTime in body
    public void pttTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("startTime", "AjshAJHba878");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with missing endTime in body
    public void pttTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.remove("endTime");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with empty string endTime in body
    public void pttTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("endTime", "");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with endTime before start time
    public void pttTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("endTime", "2019-02-17T20:10Z");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with invalid format endTime in body
    public void pttTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("endTime", "Y7hAbuwca7wcu");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with counter as MAX_INT in body
    public void pttTest13() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("counter", Integer.MAX_VALUE);
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 201, session.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with counter as negative in body
    public void pttTest14() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.put("counter", -1);
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with counter missing in body
    public void pttTest15() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            session.remove("counter");
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/sessions
    public void pttTest16() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            final String session1 = session(0, "2019-02-18T20:00Z", "2019-02-18T20:10Z", 1);
            final String session2 = session(0, "2019-02-18T20:20Z", "2019-02-18T20:40Z", 1);
            JSONArray array = new JSONArray();
            array.put(new JSONObject(session1));
            array.put(new JSONObject(session2));
            final String expected = array.toString();

            postUsersUserIdProjectsProjectIdSessions(userid, projectid, new StringEntity(session1)).close();
            postUsersUserIdProjectsProjectIdSessions(userid, projectid, new StringEntity(session2)).close();

            runTestList(getUsersUserIdProjectsProjectIdSessions(userid, projectid), 200, expected, "startTime");
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests POST call on /users/{userid}/projects/{projectid}/sessions with valid ids and body
    public void pttTest17() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            final StringEntity e = new StringEntity(session.toString());
            runTest(postUsersUserIdProjectsProjectIdSessions(userid, projectid, e), 201, session.toString());
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse getUsersUserIdProjectsProjectIdSessions(int userid, int projectid) throws IOException {
        return getRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid + "/sessions");
    }

    private CloseableHttpResponse postUsersUserIdProjectsProjectIdSessions(int userid, int projectid, StringEntity entity) throws IOException {
        return postRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid + "/sessions", entity);
    }
}
