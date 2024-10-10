/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class HttpRequestImplTest {

    static HttpRequest request;

    static Socket client = Mockito.mock(Socket.class);

    @BeforeAll
    static void setUp() throws IOException {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("GET /index.html?id=marco&age=40&name=마르코 HTTP/1.1%s",System.lineSeparator()));
        sb.append(String.format("Host: localhost:8080%s",System.lineSeparator()));
        sb.append(String.format("Connection: keep-alive%s",System.lineSeparator()));
        sb.append(String.format("Cache-Control: max-age=0%s",System.lineSeparator()));
        sb.append(String.format("sec-ch-ua-platform: macOS%s",System.lineSeparator()));
        sb.append(String.format("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36%s",System.lineSeparator()));
        sb.append(String.format("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"));

        InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
        Mockito.when(client.getInputStream()).thenReturn(inputStream);
        request = new HttpRequestImpl(client);
    }

    @Test
    void constructor(){
        assertInstanceOf(HttpRequest.class, request );
    }

    @Test
    void getMethod() {
        String method = request.getMethod();
        assertEquals("GET",method);
    }

    @Test
    void getParameter() {
        String id = request.getParameter("id");
        assertEquals("marco",id);
    }

    @Test
    void getParameterMap() {

        Map<String, Object> expected = new HashMap<>();
        expected.put("id","marco");
        expected.put("age","40");
        expected.put("name","마르코");

        Map actual = request.getParameterMap();
        assertEquals(expected,actual);
    }

    @Test
    void getHeader() {
        Assertions.assertAll(()->{
                assertEquals(request.getHeader("sec-ch-ua-platform"),"macOS");
                assertTrue(request.getHeader("User-Agent").contains("Mozilla/5.0"));
                assertTrue(request.getHeader("Host").contains("localhost"));
        });
    }

    @Test
    void attributeTest() {
        request.setAttribute("numberList", List.of(1,2,3,4,5) );
        request.setAttribute("count",1l);
        request.setAttribute("name","엔에이치엔아카데미");
        long actual = (long) request.getAttribute("count");
        String nhnacademy = (String) request.getAttribute("name");
        List<Integer> numberList = (List<Integer>) request.getAttribute("numberList");
        Assertions.assertAll(()->{
            assertEquals(1l,actual);
            assertEquals("엔에이치엔아카데미",nhnacademy);
            assertEquals(List.of(1,2,3,4,5),numberList);
        });
    }

    @Test
    void getRequestURI(){
        assertEquals("/index.html",request.getRequestURI());
    }

}