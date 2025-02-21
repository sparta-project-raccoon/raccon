package com.sparta.spartaproject.domain.slack;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {
    Slack slackClient = Slack.getInstance();

    @Value("${logging.slack.webhook-uri}")
    private String webhookUrl;

    /**
     * 슬랙 메시지 전송
     **/
    public void sendMessage(Exception e, HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        String xffHeader = request.getHeader("X-FORWARDED-FOR");

        try {
            slackClient.send(webhookUrl, payload(p -> p
                .attachments(List.of(
                    Attachment.builder()
                        .color("#ff0000")
                        .title(requestTime + " 발생 에러 로그")
                        .fields(List.of(
                            generateSlackField("Request IP", xffHeader == null ? request.getRemoteAddr() : xffHeader),
                            generateSlackField("Request URL", request.getRequestURL() + " " + request.getMethod()),
                            generateSlackField("Error Message", e.getMessage())
                        ))
                        .build()
                )))
            );
        } catch (IOException slackError) {
            log.error("Slack 통신 예외 발생");
        }
    }

    /**
     * Slack Field 생성
     **/
    private Field generateSlackField(String title, String value) {
        return Field.builder()
            .title(title)
            .value(value)
            .valueShortEnough(false)
            .build();
    }
}