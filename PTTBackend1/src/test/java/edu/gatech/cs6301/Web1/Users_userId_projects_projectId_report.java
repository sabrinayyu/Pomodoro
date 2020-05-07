package edu.gatech.cs6301.Web1;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

public class Users_userId_projects_projectId_report extends PTTBackendTests {
    @After
    public void runAfter() {
        try {
            usersConstruct.deleteAllUsers();
        } catch (Exception e) {

        }

        System.out.println("*** ENDING TEST ***");
    }
    
    public Users_userId_projects_projectId_report() {
        super();
        runBefore();
    }

    private Users usersConstruct = new Users();
    private Users_userId_projects projectsConstruct = new Users_userId_projects();
    private Users_userId_projects_projectId_sessions session = new Users_userId_projects_projectId_sessions();

    @Test
    public void pttTest1() throws Exception {
    // Purpose: test the GET request with empty time range
    // Expected: 400

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");

    //Generate start and end times


    //Get Report with non-existing user, valid project, valid start, valid end
    CloseableHttpResponse res = getReportHTTPResponse(userName, proId.getString("id"), "", "");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be a bad request
    Assert.assertEquals(400, statusCode);
    }

    @Test
    public void pttTest2() throws Exception {
    // Purpose: test the GET request with non-existing user
    // Expected: 404

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user.getString("id");

    //Create the invalid user
    int nonexistingId = usersConstruct.getBadUserId();

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");

    //Generate start and end times


    //Get Report with non-existing user, valid project, valid start, valid end
    CloseableHttpResponse res = getReportHTTPResponse(String.valueOf(nonexistingId), proId.getString("id"), "2019-02-18", "2019-02-18");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be not found
    Assert.assertEquals(404, statusCode);
    }

    @Test
    public void pttTest3() throws Exception {
    // Purpose: Test a GET request with a userId of maxint
    // Expected: 404

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");

    //Generate start and end times


    //Get Report with non-existing user, valid project, valid start, valid end
    CloseableHttpResponse res = getReportHTTPResponse(String.valueOf(Integer.MAX_VALUE), proId.getString("id"), "2019-02-18", "2019-02-18");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be not found
    Assert.assertEquals(404, statusCode);

    }

    @Test
    public void pttTest4() throws Exception {
    // Purpose: Test a GET request with a valid userId and invalid projectId
    // Expected: 404

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user3 = usersConstruct.createUser("trudy", "monk", "trudyellison@gatech.edu");
    String userName = user3.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");

    //Create the invalid user
    Users_userId_projects_projectId project = new Users_userId_projects_projectId();
    int nonexistingId = project.getBadProjectId(userName);

    //Generate start and end times


    //Get Report with non-existing user, valid project, valid start, valid end
    CloseableHttpResponse res = getReportHTTPResponse(userName, String.valueOf(nonexistingId), "2019-02-18", "2019-02-18");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be not found
    Assert.assertEquals(404, statusCode);

    }

    @Test
    public void pttTest5() throws Exception {
    // Purpose: Test a GET request with a projectId of maxint
    // Expected: 404

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user3 = usersConstruct.createUser("trudy", "monk", "trudyellison@gatech.edu");
    String userName = user3.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");

    //Generate start and end times


    //Get Report with non-existing user, valid project, valid start, valid end
    CloseableHttpResponse res = getReportHTTPResponse(userName, String.valueOf(Integer.MAX_VALUE), "2019-02-18", "2019-02-18");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be not found
    Assert.assertEquals(404, statusCode);
    }

    @Test
    public void pttTest6() throws Exception {
    // Purpose: Test a GET request with an invalid start time
    // Expected: 400

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");


    //Create a session with valid start and end times

    //Generate start and end times


    //Get empty session
    CloseableHttpResponse res = getReportHTTPResponse(userName, proId.getString("id"), "", "2019-02-18");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be a bad request
    Assert.assertEquals(400, statusCode);
    }

    @Test
    public void pttTest7() throws Exception {
    // Purpose: Test a GET request with an invalid end time
    // Expected: 400

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");


    //Create a session with valid start and end times

    //Generate start and end times


    //Get empty session
    CloseableHttpResponse res = getReportHTTPResponse(userName, proId.getString("id"), "2019-02-18", "");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be a bad request
    Assert.assertEquals(400, statusCode);

    }

    @Test
    public void pttTest8() throws Exception {
    // Purpose: Test a GET request with a valid userId and invalid projectId, no session in the range
    // Expected: 200

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user3 = usersConstruct.createUser("trudy", "monk", "trudyellison@gatech.edu");
    String userName = user3.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");

    //Generate start and end times


    //Get Report with non-existing user, valid project, valid start, valid end
    CloseableHttpResponse res = getReportHTTPResponse(userName, proId.getString("id"), "2019-02-18T20:00Z", "2019-02-19T20:00Z");
    int statusCode = res.getStatusLine().getStatusCode();

    // should be successful
    Assert.assertEquals(200, statusCode);

    }

    @Test
    public void pttTest9() throws Exception {
    // Purpose: Test a GET request with a valid
    // start and end time with valid user and project
    // that also returns the number of completed pomodoros and total hours

    // Expected: The fields for both completedPomodoros and totalHoursWorkedOnProject are present
        // the field for completedPomodoros exists, holding value of 0
        // the field for totalHoursWorkedOnProject is present and holds a string
        // with value equal to the number of hours beteween the start time and end time

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");
    String projId = proId.getString("id");

    //Generate start and end times


    //Get empty session
    JSONObject sessToReplace = session.createSession(userName, projId, "2019-02-18T20:00Z", "2019-02-21T20:00Z");

    //Get report
    JSONObject report = getReport(userName, projId, "2019-02-18T20:00Z", "2019-02-25T20:00Z&includeCompletedPomodoros=true&includeTotalHoursWorkedOnProject=true");

    int completed = Integer.parseInt(report.getString("completedPomodoros"));
    double hours = Double.parseDouble(report.getString("totalHoursWorkedOnProject"));
    Assert.assertEquals(0, completed);
    Assert.assertEquals(72.0, hours, 0.001);

    }

    @Test
    public void pttTest10() throws Exception {
    // Purpose: Test a GET request with a valid
    // start and end time with valid user and project
    // that also returns the number of completed pomodoros without total hours

    // Expected: The returned JSON object has no field for totalHoursWorkedOnProject
        // and the field for completedPomodoros exists, holding value of 0


    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");
    String projId = proId.getString("id");

    //Generate start and end times


    //Get empty session
    JSONObject sessToReplace = session.createSession(userName, projId, "2019-02-18T20:00Z", "2019-02-21T20:00Z");

    //Get report
    JSONObject report = getReport(userName, projId, "2019-02-18T20:00Z", "2019-02-25T20:00Z&includeCompletedPomodoros=true&includeTotalHoursWorkedOnProject=false");

    int completed = Integer.parseInt(report.getString("completedPomodoros"));
    Boolean noVal = report.has("totalHoursWorkedOnProject");
    Assert.assertEquals(0, completed);
    Assert.assertEquals(Boolean.FALSE,  Boolean.FALSE);

    }


    @Test
    public void pttTest11() throws Exception {
    // Purpose: Test a GET request with a valid
    // start and end time with valid user and project
    // that also returns the total hours number without completed pomodoros

    // Expected: The returned JSON object has no field for completedPomodoros
        // the field for totalHoursWorkedOnProject is present and holds a string
        // with value equal to the number of hours beteween the start time and end time


    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");
    String projId = proId.getString("id");

    //Generate start and end times


    //Get empty session
    JSONObject sessToReplace = session.createSession(userName, projId, "2019-02-18T20:00Z", "2019-02-21T20:00Z");

    //Get report
    JSONObject report = getReport(userName, projId, "2019-02-18T20:00Z", "2019-02-25T20:00Z&includeCompletedPomodoros=false&includeTotalHoursWorkedOnProject=true");

    Boolean noVal = report.has("completedPomodoros");
    double hours = Double.parseDouble(report.getString("totalHoursWorkedOnProject"));
    Assert.assertEquals(Boolean.FALSE, noVal);
    Assert.assertEquals(72.0, hours, 0.001);

    }

    @Test
    public void pttTest12() throws Exception {
    // Purpose: Test a GET request with a valid
    // start and end time with valid user and project
    // that returns neither the number of completed pomodoros nor total hours

    // Expected: The returned JSON object has no fields for completedPomodoros or totalHoursWorkedOnProject

    //Start fresh
    usersConstruct.deleteAllUsers();

    //Create a valid user (to have a valid projectId)
    JSONObject user2 = usersConstruct.createUser("adrian", "monk", "mrmonk@gatech.edu");
    String userName = user2.getString("id");

    //Create a projectId element
    JSONObject proId = projectsConstruct.createProject(userName, "projectX");
    String projId = proId.getString("id");

    //Generate start and end times


    //Get empty session
    JSONObject sessToReplace = session.createSession(userName, projId, "2019-02-18T20:00Z", "2019-02-21T20:00Z");

    //Get report
    JSONObject report = getReport(userName, projId, "2019-02-18T20:00Z", "2019-02-25T20:00Z&includeCompletedPomodoros=false&includeTotalHoursWorkedOnProject=false");


    Boolean noVal = report.has("completedPomodoros");
    Boolean noHours = report.has("totalHoursWorkedOnProject");
    Assert.assertEquals(Boolean.FALSE, noVal);
    Assert.assertEquals(Boolean.FALSE, noHours);

    }


    public CloseableHttpResponse getReportHTTPResponse (String userId, String projectId, String from, String to) throws IOException, JSONException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/users/" + userId + "/projects/" + projectId + "/report?from=" + from + "&to=" + to);
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public JSONObject getReport(String userId, String projectId, String from, String to) throws IOException, JSONException {
        CloseableHttpResponse response = getReportHTTPResponse(userId, projectId, from, to);
        System.out.println("*** Raw response " + response + "***");
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        return new JSONObject(strResponse);
    }

}
