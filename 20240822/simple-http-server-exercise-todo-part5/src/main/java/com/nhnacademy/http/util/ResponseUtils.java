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

package com.nhnacademy.http.util;

import java.io.*;
import java.net.URL;
import java.util.Objects;

public class ResponseUtils {
    private ResponseUtils(){}

    /**
     * /src/main/resourcs 하위에 filePath에 해당되는 파일이 존재하는 체크 합니다.
     * @param filePath, filePath -> requestURl -> ex) /index.html
     * @return true or false
     */
    public static boolean isExist(String filePath){
        /* TODO#7 isExist를 구현합니다,
           ex) filePat=/index.html 이면 /resources/index.html이 존재하면 true, 존재하지 않다면 false를 반환 합니다.
        */
        if(filePath == null || filePath.isEmpty()){
            return false;
        }
        URL url = ResponseUtils.class.getResource(filePath);
        return Objects.nonNull(url);
    }

    /**
     *
     * @param filePath , requestURi, ex) /index.html
     * @return String , index.html 파일을 읽고 String으로 반환
     * @throws IOException
     */
    public static String tryGetBodyFromFile(String filePath) throws IOException {
        /* TODO#9 tryGetBodyFromFile 구현 합니다.
         * ex) filePath = /index.html -> /resources/index.html 파일을 읽어서 반환 합니다.
         * */
        if(filePath == null || filePath.isEmpty()){
            throw new IOException("File path is null or empty");
        }

        StringBuilder responseBody = new StringBuilder();
        try(InputStream inputStream = ResponseUtils.class.getResourceAsStream(filePath);
            BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream,"UTF-8"))){
            while(true) {
                String line = reader.readLine();
                if(Objects.isNull(line)){
                    break;
                }
                responseBody.append(line).append(System.lineSeparator());
            }
        }
        return responseBody.toString();
    }

    /**
     *
     * @param httpStatusCode , 200 - OK
     * @param charset, default : UTF-8
     * @param contentLength, responseBody의 length
     * @return responseHeader를 String 반환
     */
    public static String createResponseHeader(int httpStatusCode, String charset, int contentLength ){
        /* TODO#11 responseHeader를 생성 합니다. 아래 header 예시를 참고

            HTTP/1.0 200 OK
            Server: HTTP server/0.1
            Content-type: text/html; charset=UTF-8
            Connection: Closed
            Content-Length:143

        */
        StringBuilder responseHeader = new StringBuilder();
        responseHeader.append(String.format("HTTP/1.0 %d OK%s",httpStatusCode,System.lineSeparator()));
        responseHeader.append(String.format("Server: HTTP server/0.1%s",System.lineSeparator()));
        responseHeader.append(String.format("Content-type: text/html; charset=%s%s",charset,System.lineSeparator()));
        responseHeader.append(String.format("Connection: Closed%s",System.lineSeparator()));
        responseHeader.append(String.format("Content-Length:%d %s%s",contentLength,System.lineSeparator(),System.lineSeparator()));
        responseHeader.append(System.lineSeparator());
        return responseHeader.toString();
    }

}
