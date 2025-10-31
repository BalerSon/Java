package src.people.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static String toJson(Object object) throws JsonProcessingException{
        objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException{
        objectMapper.readValue(json, clazz);
    }

    public static String toPrettyJson(Object object) throws JsonProcessingException{
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}