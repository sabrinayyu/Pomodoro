package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;


public class SessionsTests {

    Library lib;

    // Purpose: Get Request has invalid userId returning 404
    @Test
    public void pttTest1() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.getSessions(-1, 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        finally {
            httpclient.close(); 
        }
    }
    

    // Purpose: Post Request has invalid userId returning 404
    @Test
    public void pttTest2() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.postSessions(-1, 1, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        finally {
            httpclient.close(); 
        }
    }

    // Purpose: Get Request has invalid projectId returning 404
    @Test
    public void pttTest3() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.getSessions(userid, -1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseTest.close();
            httpclient.close(); 
        }
    }

    // Purpose: Post Request has invalid projectId returning 404
    @Test
    public void pttTest4() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.postSessions(userid, -1, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        finally {
            lib.deleteUserId(userid);
            responseTest.close();
            httpclient.close(); 
        }
    }

    // Purpose: Post Request has empty session start time returning 400
    @Test
    public void pttTest5() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);
        
        try {
            CloseableHttpResponse response = lib.postSessions(userid, projectid, "", "2019-02-23T21:00Z", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        finally {
            lib.deleteUserId(userid);
            responseProj.close();
            responseTest.close();
            httpclient.close(); 
        }
    }

    // Purpose: Post Request has empty session end time returning 400
    @Test
    public void pttTest6() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);
        
        try {
            CloseableHttpResponse response = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseProj.close();
            responseTest.close();
            httpclient.close(); 
        }
    }

    // Purpose: Post Request has start time late than end time returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest7() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);
        
        try {
            CloseableHttpResponse response = lib.postSessions(userid, projectid, "2019-02-23T21:00Z", "2019-02-23T20:00Z", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("End time is before start time. Test case should not work. Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseProj.close();
            responseTest.close();
            httpclient.close(); 
        }
    }

    // Purpose: Post Request has invalid counter input returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest8() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);
        
        try {
            CloseableHttpResponse response = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", -1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Counter < 0. Test case should not be working. Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseProj.close();
            responseTest.close();
            httpclient.close(); 
        }
    }

    // Purpose: Correct Get Request returning 200
    // Not working on the server but should be an error after asking the Professor
    // Modified test case so that expected request body contains an array of sessions and not an individual session. Changed expected endTime to 21:00 hrs.
    @Test
    public void pttTest9() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);

        
        try {
            lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
            CloseableHttpResponse response = lib.getSessions(userid, projectid);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Get Request not working for sessions. Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String id = lib.getFieldFromStringResponseIndex(strResponse, "id", 0);
            String expectedJson = "[{\"id\":" + id + ",\"startTime\":\"2019-02-23T20:00Z\",\"endTime\":\"2019-02-23T21:00Z\",\"counter\":1}]";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseProj.close();
            responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Correct Post Request returning 201
    @Test
    public void pttTest10() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        CloseableHttpResponse responseTest =lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);

        
        try {
        	CloseableHttpResponse response = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status == 201) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String id = lib.getFieldFromStringResponse(strResponse, "id");
            String expectedJson = "{\"id\":" + id + ",\"startTime\":\"2019-02-23T20:00Z\",\"endTime\":\"2019-02-23T21:00Z\",\"counter\":1}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseProj.close();
            responseTest.close();
            httpclient.close();
        }
    }
}
