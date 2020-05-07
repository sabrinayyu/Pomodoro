package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;


public class ProjectsTests {
    Library lib;

    // Purpose: Get Request has invalid userId returning 404
    @Test
    public void pttTest1() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.getProjects(-1);

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
            CloseableHttpResponse response = lib.postProjects(-1, "TestProject");

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

    // Purpose: Post Request has empty projectname returning 400
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
            CloseableHttpResponse response = lib.postProjects(userid, "");

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
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

    // Purpose: Correct Get Request returning 200
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
            lib.postProjects(userid, "TestProject");
            CloseableHttpResponse response = lib.getProjects(userid);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String id = lib.getFieldFromStringResponseIndex(strResponse, "id", 0);
            String expectedJson = "[{\"id\":" + id + ",\"projectname\":\"TestProject\"}]";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Correct Post Request returning 200
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
        
        try {
            CloseableHttpResponse response = lib.postProjects(userid, "TestProject");

            int status = response.getStatusLine().getStatusCode();
            if (status == 201) {
                entity = response.getEntity();
            } else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String id = lib.getFieldFromStringResponse(strResponse, "id");
            String expectedJson = "{\"id\":" + id + ",\"projectname\":\"TestProject\"}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        }
        finally {
            lib.deleteUserId(userid);
            responseTest.close();
            httpclient.close();
        }
    }
}
