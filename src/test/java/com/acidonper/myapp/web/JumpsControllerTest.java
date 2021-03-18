package com.acidonper.myapp.web;


import com.acidonper.myapp.dtos.JumpDto;
import com.acidonper.myapp.entities.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
class JumpsControllerTest {

    @Mock
    HttpURLConnection connection;

    @Mock
    HttpURLConnection connectionMulti;

    @Spy
    JumpsController app;

    @Spy
    JumpsController appMulti;

    @Test
    public void getJump() throws Exception {
        String url = "http://localhost:8443";
        URL urlTest = new URL(url + "/");

        // Generate Expected response mocking connection
        Response responseExpected = new Response("/jump - Greetings from Spring Boot!",200);
        InputStream input = new ByteArrayInputStream(new Gson().toJson(responseExpected).getBytes("UTF-8"));
        Mockito.doReturn(connection).when(app).create(urlTest);
        Mockito.doReturn(input).when(connection).getInputStream();

        // Make call
        Map<String, String> headers = new HashMap<>();
        headers.put("test", "test");
        ResponseEntity<String> status = app.jumpGet(headers);

        ObjectMapper mapper = new ObjectMapper();
        String responseExpectedjson = mapper.writeValueAsString(responseExpected);
        String statusResponsejson = mapper.writeValueAsString(status.getBody());

        // Expect value
        Assertions.assertEquals(responseExpectedjson, statusResponsejson);

    }
    @Test
    public void postJumpSingle() throws Exception {

        // Variables
        String url = "http://localhost:8443";
        URL urlTest = new URL(url + "/");

        // Generate Expected response mocking connection
        Response responseExpected = new Response("/jump - Greetings from Spring Boot!",200);
        InputStream input = new ByteArrayInputStream(new Gson().toJson(responseExpected).getBytes("UTF-8"));
        Mockito.doReturn(connection).when(app).create(urlTest);
        Mockito.doReturn(input).when(connection).getInputStream();

        // Make call
        JumpDto jumpDto = new JumpDto("test","/", "/jump", new String[] {url});
        Map<String, String> headers = new HashMap<>();
        headers.put("test", "test");
        ResponseEntity<String> status = app.jumpPost(jumpDto, headers);

        ObjectMapper mapper = new ObjectMapper();
        String responseExpectedjson = mapper.writeValueAsString(responseExpected);
        String statusResponsejson = mapper.writeValueAsString(status.getBody());

        // Expect value
        Assertions.assertEquals(responseExpectedjson, statusResponsejson);
    }

    @Test
    public void postJumpMulti() throws Exception {

        // Variables
        String url1 = "http://localhost:8444";
        String url2 = "http://localhost:8442";

        URL urlTest = new URL(url1 + "/jump");

        // Generate Expected response mocking connection
        Response responseExpected = new Response("/jump - Greetings from Spring Boot!",200);
        InputStream input = new ByteArrayInputStream(new Gson().toJson(responseExpected).getBytes("UTF-8"));
        JumpDto jumpDtoMultiExpected = new JumpDto("test","/", "/jump", new String[] {url2});
        OutputStream output = new ByteArrayOutputStream(new Gson().toJson(jumpDtoMultiExpected).length());
        Mockito.doReturn(connectionMulti).when(appMulti).create(urlTest);
        Mockito.doReturn(output).when(connectionMulti).getOutputStream();
        Mockito.doReturn(input).when(connectionMulti).getInputStream();

        // Make call
        JumpDto jumpDtoMulti = new JumpDto("test","/", "/jump", new String[] {url1, url2});
        Map<String, String> headers = new HashMap<>();
        headers.put("test", "test");
        ResponseEntity<String> status = appMulti.jumpPost(jumpDtoMulti, headers);

        ObjectMapper mapper = new ObjectMapper();
        String responseExpectedjson = mapper.writeValueAsString(responseExpected);
        String statusResponsejson = mapper.writeValueAsString(status.getBody());

        // Expect value
        Assertions.assertEquals(responseExpectedjson, statusResponsejson);
    }
}