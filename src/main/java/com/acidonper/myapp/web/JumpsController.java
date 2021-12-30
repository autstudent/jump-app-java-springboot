package com.acidonper.myapp.web;

import com.acidonper.myapp.dtos.JumpDto;
import com.acidonper.myapp.entities.Jump;
import com.acidonper.myapp.entities.Response;
import com.acidonper.myapp.mappers.JumpMapper;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

@RestController
public class JumpsController {

    @GetMapping("/jump")
    ResponseEntity<String> jumpGet(@RequestHeader Map<String, String> headers){
        System.out.println("Received GET /jump");
        // Print Headers
        headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });
        Response response = new Response("/jump - Greetings from Spring Boot!",200 );
        System.out.println("Sending GET Response /jump - " + response.toString());

        // Build response
        System.out.println("Responding GET Response /jump - " + response.toString());
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json");
        resHeaders.add("React-Modifier", headers.get("React-Modifier"));
        resHeaders.add("X-Request-Id", headers.get("X-Request-Id"));
        resHeaders.add("X-B3-Spanid", headers.get("X-B3-Spanid"));
        resHeaders.add("X-B3-Parentspanid", headers.get("X-B3-Parentspanid"));
        resHeaders.add("X-B3-Sampled", headers.get("X-B3-Sampled"));
        resHeaders.add("X-B3-Flags", headers.get("X-B3-Flags"));
        resHeaders.add("X-Ot-Span-Context", headers.get("X-Ot-Span-Context"));
        return new ResponseEntity(response, resHeaders, HttpStatus.OK);
    }

    @PostMapping("/jump")
    ResponseEntity<String> jumpPost(@Valid
                      @RequestBody JumpDto newJump,
                      @RequestHeader Map<String, String> headers) throws IOException {
        System.out.println("Received POST /jump");
        // Print Headers
        headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });

        Jump jump = JumpMapper.INSTANCE.jumpDTOtoJump(newJump);
        Response response = new Response("/jump - Farewell from Spring Boot! Error by default",400 );

        if (jump.jumps.length == 0) {
            response.message = "/jump - Farewell from Spring Boot! Bad Request!";
            response.code = 400;
        } else if (jump.jumps.length == 1) {
            System.out.println("Received POST /jump with 1 JUMP.jumps -" + newJump.toStringCustom());

            // Perform GET connection to the last jump
            URL url = new URL(jump.jumps[0] + jump.last_path);

            // Make connection
            HttpURLConnection con = create(url);

            // Perform GET
            System.out.println("Sending GET Response /jump to " + url);
            con.setRequestProperty("React-Modifier", headers.get("react-modifier"));
            con.setRequestProperty("X-Request-Id", headers.get("x-request-id"));
            con.setRequestProperty("X-B3-Spanid", headers.get("x-b3-spanid"));
            con.setRequestProperty("X-B3-Parentspanid", headers.get("x-b3-parentspanid"));
            con.setRequestProperty("X-B3-Sampled", headers.get("x-b3-sampled"));
            con.setRequestProperty("X-B3-Flags", headers.get("x-b3-Flags"));
            con.setRequestProperty("X-Ot-Span-Context", headers.get("x-ot-span-context"));
            con.setRequestMethod("GET");

            // Handle Response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {

                StringBuilder getResponse = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    getResponse.append(responseLine.trim());
                }
                // Generate Response
                response = new Gson().fromJson(getResponse.toString(), Response.class);
                System.out.println(con.getHeaderFields());
            } catch (Exception error) {
                response.message = "/jump - Farewell from Spring Boot! Error jumping";
                response.code = 400;
            }
        } else if (jump.jumps.length > 1) {
            System.out.println("Received POST /jump with multi JUMP.jumps -" + newJump.toStringCustom());

            // Perform GET connection to the last jump
            URL url = new URL(jump.jumps[0] + jump.jump_path);

            // Make connection
            HttpURLConnection con = create(url);

            // Generate the new Jump Object first position of jumps array
            String[] jumpsPost = Arrays.copyOfRange(jump.jumps, 1, jump.jumps.length);
            Jump jumpPost = jump;
            jumpPost.jumps = jumpsPost;

            // Perform POST
            System.out.println("Sending POST Response /jump to " + url);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("React-Modifier", headers.get("react-modifier"));
            con.setRequestProperty("X-Request-Id", headers.get("x-request-id"));
            con.setRequestProperty("X-B3-Spanid", headers.get("x-b3-spanid"));
            con.setRequestProperty("X-B3-Parentspanid", headers.get("x-b3-parentspanid"));
            con.setRequestProperty("X-B3-Sampled", headers.get("x-b3-sampled"));
            con.setRequestProperty("X-B3-Flags", headers.get("x-b3-Flags"));
            con.setRequestProperty("X-Ot-Span-Context", headers.get("x-ot-span-context"));
            con.setDoOutput(true);
            String jsonInputString = new Gson().toJson(jumpPost);
            try (OutputStream os = con.getOutputStream()) {
                System.out.println(con.getOutputStream());
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Handle Response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder getResponse = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    getResponse.append(responseLine.trim());
                }

                // Generate Response
                response = new Gson().fromJson(getResponse.toString(), Response.class);
            } catch (Exception error) {
                response.message = "/jump - Farewell from Spring Boot! Error jumping";
                response.code = 400;
            }
        }

        // Build Response
        System.out.println("Responding POST Response /jump - " + response.toString());
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json");
        resHeaders.add("React-Modifier", headers.get("React-Modifier"));
        resHeaders.add("X-Request-Id", headers.get("X-Request-Id"));
        resHeaders.add("X-B3-Spanid", headers.get("X-B3-Spanid"));
        resHeaders.add("X-B3-Parentspanid", headers.get("X-B3-Parentspanid"));
        resHeaders.add("X-B3-Sampled", headers.get("X-B3-Sampled"));
        resHeaders.add("X-B3-Flags", headers.get("X-B3-Flags"));
        resHeaders.add("X-Ot-Span-Context", headers.get("X-Ot-Span-Context"));
        return new ResponseEntity(response, resHeaders, HttpStatus.OK);
    }

    HttpURLConnection create(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

}