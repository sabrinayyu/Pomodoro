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

public class PTTUsersUserIdProjectsProjectIdSessionsSessionId {


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
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with invalid userid
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(0, 0, 0, new StringEntity(session.toString())), 404);
        } finally {
            httpclient.close();
        }
    }


    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with invalid projectid
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(id, 0, 0, new StringEntity(session.toString())), 404);
        } finally {
            httpclient.close();
        }
    }


    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with invalid sessionid
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, 0, new StringEntity(session.toString())), 404);
        } finally {
            httpclient.close();
        }
    }


    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with missing id field in body
    // Modified test because missing id in body for PUT sessions should modify the session object, Expected Status Code: 200
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.remove("id");
            final StringEntity e = new StringEntity(session.toString());
            session.put("id",123);
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 200, session.toString());
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with invalid format startTime in body
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.put("startTime", "yUYB67UYVty");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with missing startTime in body
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.remove("startTime");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with empty string startTime in body
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.put("startTime", "");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with invalid format endTime in body
    public void pttTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.put("endTime", "hjb6&VVse76");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with missing endTime in body
    // Modified test case because missing endTime in PUT {sessionId} should return status code 400
    public void pttTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.remove("endTime");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with empty string endTime in body
    public void pttTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.put("endTime", "");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with counter as negative in body
    public void pttTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.put("counter", -1);
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with counter as MAX_INT in body
    public void pttTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.put("counter", Integer.MAX_VALUE);
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 200);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with missing counter in body
    public void pttTest13() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            session.remove("counter");
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests PUT call on /users/{userid}/projects/{projectid}/sessions/{sessionid} with valid ids and body
    public void pttTest14() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeSession(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            int sessionid = ids[2];
            final StringEntity e = new StringEntity(session.toString());
            runTest(putUsersUserIdProjectsProjectIdSessionsSessionId(userid, projectid, sessionid, e), 200, session.toString());
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse putUsersUserIdProjectsProjectIdSessionsSessionId(int userid, int projectid, int sessionid, StringEntity entity) throws IOException {
        return putRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid + "/sessions/" + sessionid, entity);
    }
}
