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

public class PTTUsersUserIdProjectsProjectIdReport {

    private String baseUrl = "http://localhost:8080/ptt";
    //private String baseUrl = System.getProperty("baseUrl");
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;

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
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with invalid userid
    public void pttTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            runTest(getUsersUserIdProjectsProjectIdReport(0, 0, "", "", false, false), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with invalid projectid
    public void pttTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int id = makeUser(httpclient, baseUrl);
            runTest(getUsersUserIdProjectsProjectIdReport(id, 0, "", "", false, false), 404);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with invalid format from param
    public void pttTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            runTest(getUsersUserIdProjectsProjectIdReport(userid, projectid, "--------", "2019-02-18T20:00Z", false, false), 400);
        } finally {
            httpclient.close();
        }
    }

  @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to < from
    public void pttTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            runTest(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T22:30Z",
                    "2019-02-18T19:30Z",
                    true,
                    true),
                    400);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with invalid format to param
    public void pttTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = makeProject(httpclient, baseUrl);
            int userid = ids[0];
            int projectid = ids[1];
            runTest(getUsersUserIdProjectsProjectIdReport(userid, projectid, "2019-02-18T20:00Z", "---------", false, false), 400);
        } finally {
            httpclient.close();
        }
    }


    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to > from, include pomodoros count and hours
    public void pttTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);
            String session3 = session("2019-02-18T22:00Z", "2019-02-18T23:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
            sessionsArray.put(new JSONObject(session2));
            sessionsArray.put(new JSONObject(session3));
            String report = new JSONObject()
                    .put("completedPomodoros", 4)
                    .put("totalHoursWorkedOnProject", 3)
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T19:30Z",
                    "2019-02-18T22:30Z",
                    true,
                    true),
                    200, report);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to < from, include pomodoros count
    public void pttTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);
            String session3 = session("2019-02-18T22:00Z", "2019-02-18T23:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
            sessionsArray.put(new JSONObject(session2));
            sessionsArray.put(new JSONObject(session3));
            String report = new JSONObject()
                    .put("completedPomodoros", 4)
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T19:30Z",
                    "2019-02-18T22:30Z",
                    true,
                    false),
                    200, report);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to < from, include pomodoros hours
    public void pttTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);
            String session3 = session("2019-02-18T22:00Z", "2019-02-18T23:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
            sessionsArray.put(new JSONObject(session2));
            sessionsArray.put(new JSONObject(session3));
            String report = new JSONObject()
                    .put("totalHoursWorkedOnProject", 3)
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T19:30Z",
                    "2019-02-18T22:30Z",
                    false,
                    true),
                    200, report);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to < from and no additional into
    public void pttTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);
            String session3 = session("2019-02-18T22:00Z", "2019-02-18T23:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
            sessionsArray.put(new JSONObject(session2));
            sessionsArray.put(new JSONObject(session3));
            String report = new JSONObject()
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T19:30Z",
                    "2019-02-18T22:30Z",
                    false,
                    false),
                    200, report);
        } finally {
            httpclient.close();
        }
    }
    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to == from, include pomodoros count and hours
    // Modified test case such that report has empty array of sessions. This is because there are no sessions within a timeframe where startingTime = endingTime. Since no sessions are included, pomodoro count is 0.
    public void pttTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
           // sessionsArray.put(new JSONObject(session2));
            String report = new JSONObject()
                    .put("completedPomodoros", 0)
                    .put("totalHoursWorkedOnProject", 3)
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T20:00Z",
                    "2019-02-18T20:00Z",
                    true,
                    true),
                    200, report);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to == from, include pomodoros count
    // Modified test case such that report has empty array of sessions. This is because there are no sessions within a timeframe where startingTime = endingTime. Since no sessions are included, pomodoro count is 0.
    public void pttTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
          //  sessionsArray.put(new JSONObject(session2));
            String report = new JSONObject()
                    .put("completedPomodoros", 0)
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T20:00Z",
                    "2019-02-18T20:00Z",
                    true,
                    false),
                    200, report);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to == from, include pomodoros hours
    // Modified test case such that report has empty array of sessions. This is because there are no sessions within a timeframe where startingTime = endingTime.
    public void pttTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
        //    sessionsArray.put(new JSONObject(session2));
            String report = new JSONObject()
                    .put("totalHoursWorkedOnProject", 3) 
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T20:00Z",
                    "2019-02-18T20:00Z",
                    false,
                    true),
                    200, report);
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Tests GET call on /users/{userid}/projects/{projectid}/report with to == from and no additional into
    // Modified test case such that report has empty array of sessions. This is because there are no sessions within a timeframe where startingTime = endingTime.
    public void pttTest13() throws Exception {
        httpclient = HttpClients.createDefault();
        clearUsers(httpclient, baseUrl);
        try {
            int[] ids = setupForReport();
            int userid = ids[0];
            int projectid = ids[1];

            String session2 = session("2019-02-18T20:00Z", "2019-02-18T21:00Z", 1);

            final JSONArray sessionsArray = new JSONArray();
           // sessionsArray.put(new JSONObject(session2));
            String report = new JSONObject()
                    .put("sessions", sessionsArray)
                    .toString();

            runTestNoId(getUsersUserIdProjectsProjectIdReport(userid, projectid,
                    "2019-02-18T20:00Z",
                    "2019-02-18T20:00Z",
                    false,
                    false),
                    200, report);
            clearUsers(httpclient, baseUrl);
        } finally {
            clearUsers(httpclient, baseUrl);
         httpclient.close();
        }
    }

    private CloseableHttpResponse getUsersUserIdProjectsProjectIdReport(
            int userid, int projectid,
            String from, String to,
            boolean includeCompletedPomodoros,
            boolean includeTotalHoursWorkedOnProject) throws IOException {
        return getRequest(httpclient,
                baseUrl + "/users/" + userid + "/projects/" + projectid
                        + "/report?"
                        + "from=" + from
                        + "&to=" + to
                        + "&includeCompletedPomodoros=" + includeCompletedPomodoros
                        + "&includeTotalHoursWorkedOnProject=" + includeTotalHoursWorkedOnProject
        );
    }

    private int[] setupForReport() throws Exception {
        int[] ids = makeProject(httpclient, baseUrl);
        int userid = ids[0];
        int projectid = ids[1];
        String session1 = session(0, "2019-02-18T18:00Z", "2019-02-18T19:00Z", 2);
        String session2 = session(0, "2019-02-18T20:00Z", "2019-02-18T21:00Z", 2);
        String session3 = session(0, "2019-02-18T22:00Z", "2019-02-18T23:00Z", 2);
        postUsersUserIdProjectsProjectIdSessions(userid, projectid, new StringEntity(session1)).close();
        postUsersUserIdProjectsProjectIdSessions(userid, projectid, new StringEntity(session2)).close();
        postUsersUserIdProjectsProjectIdSessions(userid, projectid, new StringEntity(session3)).close();
        return ids;
    }

    private CloseableHttpResponse postUsersUserIdProjectsProjectIdSessions(int userid, int projectid, StringEntity entity) throws IOException {
        return postRequest(httpclient,baseUrl + "/users/" + userid + "/projects/" + projectid + "/sessions", entity);
    }
}
