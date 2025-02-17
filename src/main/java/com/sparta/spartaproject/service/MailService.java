package com.sparta.spartaproject.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMimeMessage(String toEmail, String authCode) {
        log.info("메일 발송 시작 -> 수신자: {}, 인증번호: {}", toEmail,authCode);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 메일을 받을 수신자 설정
            mimeMessageHelper.setTo(toEmail);
            // 메일의 제목 설정
            mimeMessageHelper.setSubject("안녕하세요 인증번호 안내입니다.");

            // html 문법 적용한 메일의 내용
            String content = String.format("""
                    <!DOCTYPE html>
                    <html xmlns:th="http://www.thymeleaf.org">
                    
                    <body>
                    <div style="margin:100px;">
                        <h1> 인증번호 안내입니다. </h1>
                        <br>
                    
                    
                        <div align="center" style="border:1px solid black;">
                            <h3> 인증번호는 <b>%s</b> 입니다. </h3>
                        </div>
                        <br/>
                    </div>
                    
                    </body>
                    </html>
                    """,authCode); // 인증번호를 %s 자리에 삽입

            // 메일의 내용 설정
            mimeMessageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);

            log.info("메일 발송 성공!");
        } catch (Exception e) {
            log.error("메일 발송 실패! 에러 메시지: {}", e.getMessage(), e);
        }

    }

}