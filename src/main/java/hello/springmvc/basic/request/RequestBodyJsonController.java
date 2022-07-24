package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // request 에서 InputStream 꺼냄.
        ServletInputStream inputStream = request.getInputStream();
        // StreamUtils 를 이용하여 inputStream 을 utf-8 로 String 으로 바꿈
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        // String 으로 바뀐 messageBody 출력
        log.info("messageBody = {}", messageBody);
        // messageBody 를 objectMapper 로 HelloData 객체에 매핑.
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    // request, response 를 받는게 아니라 @RequestBody 로 messageBody 를 직접 받음.
    public String requestBodyJsonV2(@RequestBody String messageBody)throws IOException {
        // request 에서 inputStream 을 꺼내 String 으로 변한하는 과정이 사라짐.
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);

        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    // HelloData 타입의 data 를 바로 받음
    public String requestBodyJsonV3(@RequestBody HelloData data) {
        // request 에서 inputStream 을 꺼내고 String 변환 과정 생략
        // objectMapper 로 HelloData 객체에 매핑하는 과정 생략.
        log.info("username = {} , age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    // HttpEntity 로 매개변수를 받아 getBody 메서드로 HelloData 타입의 data 꺼냄.
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData data = httpEntity.getBody();
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username = {} , age = {}", data.getUsername(), data.getAge());
        return data;
    }
}
