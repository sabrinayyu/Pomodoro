package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;


public class SessionIdTests {

    Library lib;

    // Purpose: Put Request has invalid userId returning 404
    @Test
    public void pttTest1() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.putSessionId(-1, 1, 1, 1, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);

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

    // Purpose: Put Request has invalid projectId returning 404
    @Test
    public void pttTest2() throws Exception {
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
            CloseableHttpResponse response = lib.putSessionId(userid, -1, 1, 1, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);

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

    // Purpose: Put Request has invalid sessionId returning 404
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

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, -1, 1, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
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

    /*
    **This test case could be ignored because the ID field in response body is irrelevant 
    // Purpose: Put Request has different id from sessionId returning 400
    // Not working on the server but should be an error after asking the Professor
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

        CloseableHttpResponse responseProj = lib.postProjects(userid, "TestProject");
        entity = responseProj.getEntity();
        strResponse = EntityUtils.toString(entity);
        String projectid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(projectid_str);

        CloseableHttpResponse responseSess = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
        entity = responseSess.getEntity();
        strResponse = EntityUtils.toString(entity);
        String sessionid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer sessionid = Integer.parseInt(sessionid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, sessionid, -1, "2019-02-23T21:00Z", "2019-02-23T22:00Z", 2);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("SessionId != id in body. Test case should give error. Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
        	responseSess.close();
        	responseProj.close();
        	responseTest.close();
            httpclient.close();
        }
    }
    */

    // Purpose: Put Request has empty start time returning 400
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

        CloseableHttpResponse responseSess = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
        entity = responseSess.getEntity();
        strResponse = EntityUtils.toString(entity);
        String sessionid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer sessionid = Integer.parseInt(sessionid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, sessionid, sessionid, "2019-02-23T21:00Z", "", 2);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
        	responseSess.close();
        	responseProj.close();
        	responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Put Request has empty end time returning 400
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

        CloseableHttpResponse responseSess = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
        entity = responseSess.getEntity();
        strResponse = EntityUtils.toString(entity);
        String sessionid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer sessionid = Integer.parseInt(sessionid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, sessionid, sessionid, "", "2019-02-23T22:00Z", 2);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
        	responseSess.close();
        	responseProj.close();
        	responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Put Request has start time late than end time returning 400
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

        CloseableHttpResponse responseSess = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
        entity = responseSess.getEntity();
        strResponse = EntityUtils.toString(entity);
        String sessionid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer sessionid = Integer.parseInt(sessionid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, sessionid, sessionid, "2019-02-23T22:00Z", "2019-02-23T21:00Z", 2);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Start time is after endtime. Test case should give error. Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
        	responseSess.close();
        	responseProj.close();
        	responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Put Request has invalid counter (< 0) returning 400
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

        CloseableHttpResponse responseSess = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
        entity = responseSess.getEntity();
        strResponse = EntityUtils.toString(entity);
        String sessionid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer sessionid = Integer.parseInt(sessionid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, sessionid, sessionid, "2019-02-23T22:00Z", "2019-02-23T21:00Z", -1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Counter < 0. Test case should give error. Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
        	responseSess.close();
        	responseProj.close();
        	responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Put Request returning 200
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

        CloseableHttpResponse responseSess = lib.postSessions(userid, projectid, "2019-02-23T20:00Z", "2019-02-23T21:00Z", 1);
        entity = responseSess.getEntity();
        strResponse = EntityUtils.toString(entity);
        String sessionid_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer sessionid = Integer.parseInt(sessionid_str);
        
        try {
            CloseableHttpResponse response = lib.putSessionId(userid, projectid, sessionid, sessionid, "2019-02-23T21:00Z", "2019-02-23T22:00Z", 2);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + sessionid + ",\"startTime\":\"2019-02-23T21:00Z\",\"endTime\":\"2019-02-23T22:00Z\",\"counter\":2}";
            JSONAssert.assertEquals(expectedJson, strResponse, false);
            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
        	responseSess.close();
        	responseProj.close();
        	responseTest.close();
            httpclient.close();
        }
    }
}
