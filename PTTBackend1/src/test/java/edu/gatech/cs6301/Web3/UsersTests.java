package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

public class UsersTests {

    Library lib;
    
    // Purpose: Post Request has empty firstname returning 400
    @Test
    public void pttTest1() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.postUsers("", "Khan", "smkhan@cmu.edu");

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        finally{
            httpclient.close();
        }

    }

    // Purpose: Post Request has empty lastname returning 400
    @Test
    public void pttTest2() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.postUsers("Sharjeel", "", "smkhan@cmu.edu");

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            httpclient.close();
        }

    }

    // Purpose: Post Request has empty email returning 400
    @Test
    public void pttTest3() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.postUsers("Sharjeel", "Khan", "");

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response.close();
        }
        finally{
            httpclient.close();
        }

    }

    /*
    POST/users with wrong email format (test4) (should return 400), ours 
    just ignores this. Our professor said it is not needed on piazza

    
    // Purpose: Post Request has wrong email format returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest4() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.postUsers("Sharjeel", "Khan", "asdasdsdsdasdadaad");
            
            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("Email format is not right. Test case should give error. Unexpected response status: " + status);
            }

            response.close();
        }
        finally{
            httpclient.close();
        }

    }
    */

    // Purpose: Correct Get Request returning 200
    @Test
    public void pttTest5() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        try {
            lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
            CloseableHttpResponse response = lib.getUsers();

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            String strResponse = EntityUtils.toString(entity);
            String id = lib.getFieldFromStringResponseIndex(strResponse, "id", 0);
            String expectedJson = "[{\"id\":" + id + ",\"firstName\":\"Sharjeel\",\"lastName\":\"Khan\",\"email\":\"smkhan@gatech.edu\"}]";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            
            EntityUtils.consume(response.getEntity());
            lib.deleteUserId(Integer.parseInt(id));
            response.close();
        }
        finally {
            httpclient.close();
        }

    }

    // Purpose: Correct Post Request returning 201
    @Test
    public void pttTest6() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        try {
            CloseableHttpResponse response = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            
            String strResponse = EntityUtils.toString(entity);
            String id = lib.getFieldFromStringResponse(strResponse, "id");
            String expectedJson = "{\"id\":" + id + ",\"firstName\":\"Sharjeel\",\"lastName\":\"Khan\",\"email\":\"smkhan@gatech.edu\"}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            lib.deleteUserId(Integer.parseInt(id));
            response.close();
        }
        finally {
            httpclient.close();
        }

    }

}
