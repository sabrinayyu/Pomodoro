package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

public class UserIdTests {

    Library lib;

    // Purpose: Get Request has invalid userId returning 404
    @Test
    public void pttTest1() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;  
        try {
            CloseableHttpResponse response = lib.getUserId(-1);
            int status = response.getStatusLine().getStatusCode();

            if (status != 404){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        
            response.close();
        } 
        finally{
            httpclient.close();
        }
    }

    // Purpose: Put Request has invalid userId returning 404
    @Test
    public void pttTest2() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;  
        try {
            CloseableHttpResponse response = lib.putUserId(-1, 0, "firstname", "lastname", "email@email.com");
            int status = response.getStatusLine().getStatusCode();

            if (status != 404){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        
            response.close();
        } 
        finally{
            httpclient.close();
        }
    }

    // Purpose: Delete Request has invalid userId returning 404
    @Test
    public void pttTest3() throws Exception{
        lib = new Library();
        lib.runBefore();
        CloseableHttpClient httpclient = lib.httpclient;
        try {
            CloseableHttpResponse response = lib.deleteUserId(-1);
            int status = response.getStatusLine().getStatusCode();

            if (status != 404){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        
            response.close();
        } 
        finally{
            httpclient.close();
        }
    }


    /*
    the userId in the path is different from that in the request body (should return 400)
    , we are ignoring the id part in body for other cases

    // Purpose: Put Request has different id from userId in the body returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest4() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        try {
            response = lib.postUsers("first", "last", "e@e.com");
            int status = response.getStatusLine().getStatusCode();
            if (status != 201 && status != 409){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            int id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //User created, now deleting
            response = lib.putUserId(id, -1, "firstname", "lastname", "e@e.com");
            status = response.getStatusLine().getStatusCode();
            if(status != 400){
                response.close();
                throw new ClientProtocolException("userId != id in body. Test case should fail. Unexpected response status: " + status); 
            }

            response.close();
        } 
        finally{
            httpclient.close();
        }
    }

    */

    // Purpose: Put Request has empty firstName in the body returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest5() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        
        int id, status;

        response = lib.postUsers("first1", "last1", "e1@e.com");
        
        status = response.getStatusLine().getStatusCode();
        
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Insert a userid
            response = lib.putUserId(id, id, "", "lastname", "email@email.com");
            status = response.getStatusLine().getStatusCode();
            if (status != 400){
                throw new ClientProtocolException("First name empty, test case should fail. "
                + "Unexpected response status: " + status);
            }           
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }


    // Purpose: Put Request has empty lastName in the body returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest6() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        int status, id;
        
        //Insert a userid
        response = lib.postUsers("first1", "last1", "e1@e.com");
        status = response.getStatusLine().getStatusCode();
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            response = lib.putUserId(id, id, "first1", "", "email@email.com");
            status = response.getStatusLine().getStatusCode();
            if (status != 400){
                throw new ClientProtocolException("Last name empty, test case should fail. "
                + "Unexpected response status: " + status);
            }
            
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }

    
    // Purpose: Correct Get Request returning 200
    @Test
    public void pttTest7() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        
        //Insert a userid
        response = lib.postUsers("name1a", "name2a", "e1@e.com");
        int status = response.getStatusLine().getStatusCode();
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        int id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            response = lib.getUserId(id);
            status = response.getStatusLine().getStatusCode();
            
            HttpEntity entity;
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            String strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + id + ",\"firstName\":\"name1a\",\"lastName\":\"name2a\",\"email\":\"e1@e.com\"}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();

            
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }

    // Purpose: Correct Put Request returning 200
    @Test
    public void pttTest8() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        //Insert a userid
        response = lib.postUsers("name1a", "name2a", "e1@e.com");
        int status = response.getStatusLine().getStatusCode();
        
        
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        
        int id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            response = lib.putUserId(id, 0, "firstname", "lastname", "e1@e.com");
            status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            String strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + id + ",\"firstName\":\"firstname\",\"lastName\":\"lastname\",\"email\":\"e1@e.com\"}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }

    // Purpose: Correct Delete Request returning 200
    @Test
    public void pttTest9() throws Exception{
        lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        
        response = lib.postUsers("name1a", "name2a", "e1@e.com");
        int status = response.getStatusLine().getStatusCode();
        
        
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        
        int id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Delete the user
            response = lib.deleteUserId(id);
            status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            String strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + id + ",\"firstName\":\"name1a\",\"lastName\":\"name2a\",\"email\":\"e1@e.com\"}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            response.close();

            response = lib.getUserId(id);
            status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            EntityUtils.consume(response.getEntity());
            response.close();
        } 
        finally {
            //Delete the user
            response.close();      
            httpclient.close();
        }
    }
    


}
