package com.fan.news.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class JSONUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static String objToJson(Object data) {
        try {
            String str = mapper.writeValueAsString(data);
            return str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static<T> T jsonToObj(String json, Class<T> beanType) {
        if (StringUtils.isEmpty(json) || beanType == null) {
            return null;
        }
        try {
            return beanType.equals(String.class)? (T) json : mapper.readValue(json, beanType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        String json = "{\"priority\":\"3\",\"courseId\":\"0\",\"resDesc\":\"666\"}";
//        Resource resource = JSONUtil.jsonToObj(json, Resource.class);
//        if (resource != null) {
//            System.out.println("--------");
//        }
//    }
}
