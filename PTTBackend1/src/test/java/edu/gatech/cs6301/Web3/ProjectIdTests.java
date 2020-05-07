package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;


public class ProjectIdTests {

    Library lib;

    // Purpose: Get Request has invalid userId returning 404
    @Test
    public void pttTest1() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.getProjectId(-1, 7);

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
    
    // Purpose: Put Request has invalid userId returning 404
    @Test
    public void pttTest2() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.putProjectId(-1, 7, 7, "TestProject");

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

    // Purpose: Delete Request has invalid userId returning 404
    @Test
    public void pttTest3() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;
        
        try {
            CloseableHttpResponse response = lib.deleteProjectId(-1, 0);

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
    public void pttTest4() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();
        
        try {
            CloseableHttpResponse response = lib.getProjectId(userid, -1);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
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

    // Purpose: Put Request has invalid projectId returning 404
    @Test
    public void pttTest5() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();
        
        try {
            CloseableHttpResponse response = lib.putProjectId(userid, -1, -1, "TestProject");

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

    // Purpose: Delete Request has invalid projectId returning 404
    @Test
    public void pttTest6() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();
        
        try {
            CloseableHttpResponse response = lib.deleteProjectId(userid, -1);

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

    /*
    the userId in the path is different from that in the request body (should return 400), 
    we are ignoring the id part in body for other cases

    // Purpose: Put Request has different id from projectId in the body returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest7() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();

        responseTest = lib.postProjects(userid, "TestProject");;
        entity = responseTest.getEntity();
        strResponse = EntityUtils.toString(entity);
        id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.putProjectId(userid, projectid, -1, "TestProject2");

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                response.close();
                throw new ClientProtocolException("projectId != id in body. Test case should give error. Unexpected response status: " + status);
            }

            response.close();
        } 
        finally {
            lib.deleteUserId(userid);
            responseTest.close();
            httpclient.close();
        }
    }
    */

    // Purpose: Put Request has empty projectname returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest8() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();

        responseTest = lib.postProjects(userid, "TestProject");;
        entity = responseTest.getEntity();
        strResponse = EntityUtils.toString(entity);
        id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.putProjectId(userid, projectid, projectid, "");

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Projectname can never be empty. Test case should give error. Unexpected response status: " + status);
            }

            response.close();
        }
        finally {
            lib.deleteUserId(userid);
            responseTest.close();
            httpclient.close();
        }
    }

    // Purpose: Correct Put Request returning 200
    @Test
    public void pttTest9() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();

        responseTest = lib.postProjects(userid, "TestProject");;
        entity = responseTest.getEntity();
        strResponse = EntityUtils.toString(entity);
        id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.putProjectId(userid, projectid, projectid, "TestProject2");

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + projectid + ",\"projectname\":\"TestProject2\"}";
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

    // Purpose: Correct Get Request returning 200
    @Test
    public void pttTest10() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();

        responseTest = lib.postProjects(userid, "TestProject");;
        entity = responseTest.getEntity();
        strResponse = EntityUtils.toString(entity);
        id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.getProjectId(userid, projectid);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + projectid + ",\"projectname\":\"TestProject\"}";
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

    // Purpose: Correct Delete Request returning 200
    @Test
    public void pttTest11() throws Exception {
        lib = new Library();
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;

        CloseableHttpResponse responseTest = lib.postUsers("Sharjeel", "Khan", "smkhan@gatech.edu");
        HttpEntity entity = responseTest.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer userid = Integer.parseInt(id_str);
        responseTest.close();

        responseTest = lib.postProjects(userid, "TestProject");;
        entity = responseTest.getEntity();
        strResponse = EntityUtils.toString(entity);
        id_str = lib.getFieldFromStringResponse(strResponse, "id");
        Integer projectid = Integer.parseInt(id_str);
        
        try {
            CloseableHttpResponse response = lib.deleteProjectId(userid, projectid);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            }
            else {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            strResponse = EntityUtils.toString(entity);
            String expectedJson = "{\"id\":" + projectid + ",\"projectname\":\"TestProject\"}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            response.close();

            response = lib.getProjectId(userid, projectid);
            status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                response.close();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

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
