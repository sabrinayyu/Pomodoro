package edu.gatech.cs6301.Web3;

import org.junit.Test;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;



public class ReportTests {

    // Purpose: Get Request has invalid userId returning 404
    @Test
    public void pttTest1() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;        

        try {
            //Make a project
            CloseableHttpResponse response2 = lib.getReport(-1, 2, "2018-02-18T20:00Z", "2019-02-18T20:00Z", 
            true, false);
            int status = response2.getStatusLine().getStatusCode();

            if (status != 404){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response2.close();
        } 
        finally {
            httpclient.close();
        }
    }

    // Purpose: Get Request has invalid projectId returning 404
    @Test
    public void pttTest2() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        int id;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();

        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Make a project
            response = lib.getReport(-1, 2, "2018-02-18T20:00Z", "2019-02-18T20:00Z", 
            true, false);

            status = response.getStatusLine().getStatusCode();

            if (status != 404){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }

    // Purpose: Get Request has empty from returning 400
    @Test
    public void pttTest3() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();
            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();
            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "", "2019-02-18T20:00Z", 
            true, false);
            status = response.getStatusLine().getStatusCode();
            if (status != 400){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }

    // Purpose: Get Request has empty to returning 400
    @Test
    public void pttTest4() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;     
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();
        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();
            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();
            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "2018-04-18T20:00Z", "", 
            true, false);
            status = response.getStatusLine().getStatusCode();
            if (status != 400){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }
    }

    // Purpose: Get Request has To is before From returning 400
    // Not working on the server but should be an error after asking the Professor
    @Test
    public void pttTest5() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;    
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();

        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();
            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "2018-04-18T20:00Z", "2018-02-18T20:00Z", 
            true, false);
            status = response.getStatusLine().getStatusCode();
            if (status != 400){
                throw new ClientProtocolException("To < From. Test case should fail. "
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

    // Purpose: Correct Get Request with both options true returning 200
    @Test
    public void pttTest6() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();

        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "2018-02-18T20:00Z", "2019-02-18T20:00Z", 
                        true, true);

            if(status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }

    }

    // Purpose: Correct Get Request with first option true returning 200
    @Test
    public void pttTest7() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();

        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
    
        try {
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "2018-02-18T20:00Z", "2019-02-18T20:00Z", 
                        true, false);

            if(status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }

    }

    // Purpose: Correct Get Request with second option true returning 200
    @Test
    public void pttTest8() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;         
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();

        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {    
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "2018-02-18T20:00Z", "2019-02-18T20:00Z", 
                        false, true);

            if(status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }

    }

    // Purpose: Correct Get Request with both options false returning 200
    @Test
    public void pttTest9() throws Exception{
        Library lib = new Library(); 
        lib.runBefore();
        lib.deleteAllUsers();
        CloseableHttpClient httpclient = lib.httpclient;       
        CloseableHttpResponse response;
        int id, pid;
        
        response = lib.postUsers("name1", "name2", "e@e.com");
        int status = response.getStatusLine().getStatusCode();

        if (status != 201 && status != 409){
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    
        id = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));
        
        try {
            //Make a project
            response = lib.postProjects(id, "pname");
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            pid = Integer.parseInt(lib.getFieldFromStringResponse(response, "id"));

            //Make a session
            response = lib.postSessions(id, pid, "2018-04-18T20:00Z", "2018-06-18T20:00Z", 1);
            status = response.getStatusLine().getStatusCode();

            if (status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            response = lib.getReport(id, pid, "2018-02-18T20:00Z", "2019-02-18T20:00Z", 
                        false, false);

            if(status != 201){
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } 
        finally {
            //Delete the user
            lib.deleteUserId(id);
            response.close();      
            httpclient.close();
        }

    }
}    


