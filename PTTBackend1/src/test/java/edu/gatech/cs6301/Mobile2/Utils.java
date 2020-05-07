package edu.gatech.cs6301.Mobile2;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Utils {

    static int makeUser(CloseableHttpClient httpclient, String baseUrl) throws Exception {
        CloseableHttpResponse response = postRequest(httpclient,
                baseUrl + "/users",
                new StringEntity(user(0, "f1", "l1", "e1")));
        int id = getId(response);
        response.close();
        return id;
    }

    static int[] makeProject(CloseableHttpClient httpclient, String baseUrl) throws Exception {
        int userid = makeUser(httpclient, baseUrl);
        CloseableHttpResponse response = postRequest(httpclient,
                baseUrl + "/users/" + userid + "/projects",
                new StringEntity(project(0, "name")));
        int id = getId(response);
        response.close();
        int[] pair = {userid, id};
        return pair;
    }

    static int[] makeSession(CloseableHttpClient httpclient, String baseUrl) throws Exception {
        int[] ids = makeProject(httpclient, baseUrl);
        int userid = ids[0];
        int projectid = ids[1];
        CloseableHttpResponse response = postRequest(httpclient,
                baseUrl + "/users/" + userid + "/projects/" + projectid + "/sessions",
                new StringEntity(session(0, "2019-02-18T20:00Z", "2019-02-18T20:10Z", 1)));
        int id = getId(response);
        response.close();
        int[] tuple = {userid, projectid, id};
        return tuple;
    }

    static void JSONAssertIgnoreId(String expected, String actual) throws Exception {
        JSONObject exp = new JSONObject(expected);
        if (exp.has("id")) {
            exp.remove("id");
        } else {
            throw new AssertionError("No id field");
        }

        System.out.print(actual);
        JSONObject act = new JSONObject(actual);
        if (act.has("id")) {
            act.remove("id");
        } else {
            throw new AssertionError("No id field");
        }

        JSONAssert.assertEquals(exp.toString(), act.toString(), false);
    }

    static void clearUsers(CloseableHttpClient httpclient, String baseUrl) throws Exception {
        CloseableHttpResponse response = getRequest(httpclient,baseUrl + "/users");
        String strResponse = EntityUtils.toString(response.getEntity());
        JSONArray users = new JSONArray(strResponse);
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            int id = user.getInt("id");
            deleteRequest(httpclient, baseUrl + "/users/" + id).close();
        }
        response.close();
    }

    static void runTest(CloseableHttpResponse response, int expectedStatus) throws Exception {
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity;
        if (status == expectedStatus) {
            entity = response.getEntity();
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        String strResponse = EntityUtils.toString(entity);
        System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
        EntityUtils.consume(response.getEntity());
        response.close();
    }

    static void runTest(CloseableHttpResponse response, int expectedStatus, String expectedJson) throws Exception {
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity;
        if (status == expectedStatus) {
            entity = response.getEntity();
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        String strResponse = EntityUtils.toString(entity);

        System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
        JSONAssertIgnoreId(expectedJson, strResponse);
        EntityUtils.consume(response.getEntity());
        response.close();
    }

    static void runTestNoId(CloseableHttpResponse response, int expectedStatus, String expectedJson) throws Exception {
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity;
        if (status == expectedStatus) {
            entity = response.getEntity();
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        String strResponse = EntityUtils.toString(entity);

        System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
        EntityUtils.consume(response.getEntity());
        JSONAssert.assertEquals(expectedJson, strResponse, false);
        response.close();
    }

    static void runTestList(CloseableHttpResponse response, int expectedStatus, String expectedJson, String idKey) throws Exception {
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity;
        if (status == expectedStatus) {
            entity = response.getEntity();
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        String strResponse = EntityUtils.toString(entity);

        System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
        EntityUtils.consume(response.getEntity());

        Map<String, JSONObject> expectedMap = new HashMap<>();
        JSONArray expectedArray = new JSONArray(expectedJson);
        for (int i = 0; i < expectedArray.length(); i++) {
            JSONObject obj = expectedArray.getJSONObject(i);
            String id = obj.getString(idKey);
            expectedMap.put(id, obj);
        }
        Map<String, JSONObject> actualMap = new HashMap<>();
        JSONArray actualArray = new JSONArray(strResponse);
        for (int i = 0; i < actualArray.length(); i++) {
            JSONObject obj = actualArray.getJSONObject(i);
            String id = obj.getString(idKey);
            actualMap.put(id, obj);
        }
        Assert.assertEquals(expectedMap.size(), actualMap.size());
        for (String id : expectedMap.keySet()) {
            JSONAssertIgnoreId(expectedMap.get(id).toString(), actualMap.get(id).toString());
        }
        response.close();
    }

    static CloseableHttpResponse getRequest(CloseableHttpClient httpclient, String endpoint) throws IOException {
        HttpGet httpRequest = new HttpGet(endpoint);
        httpRequest.addHeader("accept", "application/json");
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    static CloseableHttpResponse deleteRequest(CloseableHttpClient httpclient, String endpoint) throws IOException {
        HttpDelete httpRequest = new HttpDelete(endpoint);
        httpRequest.addHeader("accept", "application/json");
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    static CloseableHttpResponse postRequest(CloseableHttpClient httpclient, String endpoint, StringEntity body) throws IOException {
        HttpPost httpRequest = new HttpPost(endpoint);
        httpRequest.addHeader("accept", "application/json");
        body.setContentType("application/json");
        httpRequest.setEntity(body);
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    static CloseableHttpResponse putRequest(CloseableHttpClient httpclient, String endpoint, StringEntity body) throws IOException {
        HttpPut httpRequest = new HttpPut(endpoint);
        httpRequest.addHeader("accept", "application/json");
        body.setContentType("application/json");
        httpRequest.setEntity(body);
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    static int getId(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        int id = -1;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()){
            String key = keyList.next();
            if (key.equals("id")) {
                id = object.getInt(key);
            }
        }
        if (id == -1) {
            throw new AssertionError("No id field");
        }
        return id;
    }

    static int getId(CloseableHttpResponse response) throws JSONException, IOException {
        return getId(EntityUtils.toString(response.getEntity()));
    }

    static String user(int id, String firstname, String lastname, String email) throws JSONException {
        return new JSONObject()
                .put("id", id)
                .put("firstName", firstname)
                .put("lastName", lastname)
                .put("email", email)
                .toString();
    }

    static String project(int id, String projectname) throws JSONException {
        return new JSONObject()
                .put("id", id)
                .put("projectname", projectname)
                .toString();
    }

    static String session(int id, String startTime, String endTime, int counter) throws JSONException {
        return new JSONObject()
                .put("id", id)
                .put("startTime", startTime)
                .put("endTime", endTime)
                .put("counter", counter)
                .toString();
    }

    static String session(String startingTime, String endingTime, double hoursWorked) throws JSONException {
        return new JSONObject()
                .put("startingTime", startingTime)
                .put("endingTime", endingTime)
                .put("hoursWorked", hoursWorked)
                .toString();
    }
}
