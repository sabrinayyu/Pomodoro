package edu.gatech.cs6301.Web3;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Library {

    private String baseUrl = "http://localhost:8080/ptt";
    //private String baseUrl = System.getProperty("baseUrl");
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    public CloseableHttpClient httpclient;
    private boolean setupdone;

    @Before
    public void runBefore() {
        if (!setupdone) {
            System.out.println("*** SETTING UP TESTS ***");
            // Increase max total connection to 100
            cm.setMaxTotal(100);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(10);
            // Increase max connections for localhost:80 to 50
            HttpHost localhost = new HttpHost("locahost", 8080);
            cm.setMaxPerRoute(new HttpRoute(localhost), 10);
            httpclient = HttpClients.custom().setConnectionManager(cm).build();
            setupdone = true;
        }
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }

    // *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***

    /* Call Get "/users" */
    public CloseableHttpResponse getUsers() throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/users");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Post "/users" */
    public CloseableHttpResponse postUsers(String firstname, String lastname, String email) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/users");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":\"\"," + "\"firstName\":\"" + firstname + "\","
                + "\"lastName\":\"" + lastname + "\"," + "\"email\":\"" + email + "\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getAllHeaders() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Get "/users/{userId}" */
    public CloseableHttpResponse getUserId(Integer userId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/users/" + Integer.toString(userId));
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Put "/users/{userId}" */
    public CloseableHttpResponse putUserId(Integer userId, Integer id, String firstname, String lastname, String email)
            throws IOException {
        HttpPut httpRequest = new HttpPut(baseUrl + "/users/" + Integer.toString(userId));
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":\"" + Integer.toString(id) + "\"," + "\"firstName\":\""
                + firstname + "\"," + "\"lastName\":\"" + lastname + "\"," + "\"email\":\"" + email + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Delete "/users/{userId}" */
    public CloseableHttpResponse deleteUserId(Integer userId) throws IOException {
        HttpDelete httpRequest = new HttpDelete(baseUrl + "/users/" + Integer.toString(userId));
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Get "/users/{userId}/projects" */
    public CloseableHttpResponse getProjects(Integer userId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/users/" + Integer.toString(userId) + "/projects");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Post "/users/{userId}/projects" */
    public CloseableHttpResponse postProjects(Integer userId, String projectname) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/users/" + Integer.toString(userId) + "/projects/");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":\"\"," + "\"projectname\":\"" + projectname + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Get "/users/{userId}/projects/{projectid}" */
    public CloseableHttpResponse getProjectId(Integer userId, Integer projectId) throws IOException {
        HttpGet httpRequest = new HttpGet(
                baseUrl + "/users/" + Integer.toString(userId) + "/projects/" + Integer.toString(projectId));
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Put "/users/{userId}/projects/{projectid}" */
    public CloseableHttpResponse putProjectId(Integer userId, Integer projectId, Integer id, String projectname)
            throws IOException {
        HttpPut httpRequest = new HttpPut(
                baseUrl + "/users/" + Integer.toString(userId) + "/projects/" + Integer.toString(projectId));
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(
                "{\"id\":\"" + Integer.toString(id) + "\"," + "\"projectname\":\"" + projectname + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Delete "/users/{userId}/projects/{projectid}" */
    public CloseableHttpResponse deleteProjectId(Integer userId, Integer projectId) throws IOException {
        HttpDelete httpRequest = new HttpDelete(
                baseUrl + "/users/" + Integer.toString(userId) + "/projects/" + Integer.toString(projectId));
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Get "/users/{userId}/projects/{projectid}/report" */
    public CloseableHttpResponse getReport(Integer userId, Integer projectId, String from, String to,
            boolean includeCompletedPomodoros, boolean includeTotalHoursWorkedOnProject) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/users/" + Integer.toString(userId) + "/projects/"
                + Integer.toString(projectId) + "/report?from=" + from + "&to=" + to + "&includeCompletedPomodoros="
                + Boolean.toString(includeCompletedPomodoros) + "&includeTotalHoursWorkedOnProject="
                + Boolean.toString(includeTotalHoursWorkedOnProject));
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Get "/users/{userId}/projects/{projectid}/sessions" */
    public CloseableHttpResponse getSessions(Integer userId, Integer projectId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/users/" + Integer.toString(userId) + "/projects/"
                + Integer.toString(projectId) + "/sessions");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Post "/users/{userId}/projects/{projectid}/sessions" */
    public CloseableHttpResponse postSessions(Integer userId, Integer projectId, String startTime, String endTime,
            Integer counter) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/users/" + Integer.toString(userId) + "/projects/"
                + Integer.toString(projectId) + "/sessions");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":\"\"," + "\"startTime\":\"" + startTime + "\","
                + "\"endTime\":\"" + endTime + "\"," + "\"counter\":\"" + Integer.toString(counter) + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    /* Call Put "/users/{userId}/projects/{projectid}/sessions/{sessionId}" */
    public CloseableHttpResponse putSessionId(Integer userId, Integer projectId, Integer sessionId, Integer id,
            String startTime, String endTime, Integer counter) throws IOException {
        HttpPut httpRequest = new HttpPut(baseUrl + "/users/" + Integer.toString(userId) + "/projects/"
                + Integer.toString(projectId) + "/sessions/" + Integer.toString(sessionId));
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(
                "{\"id\":\"" + Integer.toString(id) + "\"," + "\"startTime\":\"" + startTime + "\"," + "\"endTime\":\""
                        + endTime + "\"," + "\"counter\":\"" + Integer.toString(counter) + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    @SuppressWarnings("unchecked")
    public String getFieldFromStringResponse(String strResponse, String field) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String fieldValue = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()) {
            String key = keyList.next();
            if (key.equals(field)) {
                fieldValue = object.get(key).toString();
            }
        }
        return fieldValue;
    }

    @SuppressWarnings("unchecked")
    public String getFieldFromStringResponseIndex(String strResponse, String field, Integer index)
            throws JSONException {
        JSONArray array = new JSONArray(strResponse);
        JSONObject object = array.getJSONObject(index);

        String fieldValue = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()) {
            String key = keyList.next();
            if (key.equals(field)) {
                fieldValue = object.get(key).toString();
            }
        }
        return fieldValue;
    }

    public String getFieldFromStringResponse(CloseableHttpResponse response, String field)
            throws ParseException, JSONException, IOException {
		return getFieldFromStringResponse(EntityUtils.toString(response.getEntity()), field);
    }
    
    public void deleteAllUsers() throws IOException, JSONException {
        CloseableHttpResponse response = getUsers();

        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        JSONArray array = new JSONArray(strResponse);

        for(int i = 0; i < array.length(); i++)
        {
            String userid = getFieldFromStringResponseIndex(strResponse, "id", i);
            deleteUserId(Integer.parseInt(userid));
        }
    }


}
