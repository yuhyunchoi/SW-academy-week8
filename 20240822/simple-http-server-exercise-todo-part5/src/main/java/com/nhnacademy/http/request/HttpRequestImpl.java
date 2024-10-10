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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpRequestImpl implements HttpRequest {
    /* TODO#2 HttpRequest를 구현 합니다.
    *  test/java/com/nhnacademy/http/request/HttpRequestImplTest TestCode를 실행하고 검증 합니다.
    */

    private final Socket client;
    private String method;
    private String requestURI;
    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> attribute = new HashMap<>();

    public HttpRequestImpl(Socket socket) throws IOException {
        this.client = socket;

        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String requestLine = reader.readLine();

        if(requestLine != null && !requestLine.isEmpty()){
            String[] requestParts = requestLine.split(" ");
            if(requestParts.length < 2){
                throw new IOException("Invalid request line");
            }
            this.method = requestParts[0];
            String[] uriParts = requestParts[1].split("\\?");
            this.requestURI = uriParts[0];

            if(uriParts.length > 1){
                String[] paramPairs = uriParts[1].split("&");
                for(String pair : paramPairs){
                    String[] keyValue = pair.split("=");
                    if(keyValue.length == 2){
                        parameters.put(keyValue[0], keyValue[1]);
                    }
                }
            }
            String line;
            while((line = reader.readLine()) != null && !line.isEmpty()){
                String[] headerParts = line.split(": ");
                if(headerParts.length == 2){
                    headers.put(headerParts[0], headerParts[1]);
                }
            }
        }else{
            throw new IOException("Empty request line");
        }
    }


    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public Map<String, String> getParameterMap() {
        return parameters;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        attribute.put(name, o);
    }

    @Override
    public Object getAttribute(String name) {
        return attribute.get(name);
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }
}
