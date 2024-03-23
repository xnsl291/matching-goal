package matchingGoal.matchingGoal.mail.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.mail.dto.MailDto;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import matchingGoal.matchingGoal.mail.model.entity.EmailContent;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final RedisService redisService;
    private final String VALID_PREFIX = "VAL_";

    @Value("${spring.mail.username}")
    private String sender;

    public void sendWelcomeMail(String email, String name) {
        final String TEMPLATE_NAME = "welcome.html";

        // EmailContent 객체 생성 및 변수 설정
        EmailContent emailContent = new EmailContent();
        emailContent.add("name", name);
        sendMail(email, TEMPLATE_NAME, emailContent);
    }

    /**
     * 이메일 인증 메일 발송
     * @param email - 이메일
     * @return 발송성공여부 (성공시 true)
     */
    public Boolean sendVerificationMail(String email) {
        final long redisDurationInMinutes = 3;
        final String TEMPLATE_NAME = "InitialEmailVerification.html";
        final String code = RandomStringUtils.random(7, true, true);
        final String expire = LocalDateTime.now().plusMinutes(redisDurationInMinutes).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // EmailContent 설정
        EmailContent emailContent = new EmailContent();
        emailContent.add("code", code);
        emailContent.add("expire", expire);

        if (!sendMail(email, TEMPLATE_NAME, emailContent))
            return false;

        redisService.setData(VALID_PREFIX + email, code, redisDurationInMinutes);
        return true;
    }

    /**
     * 메일 발송
     * @param email - 이메일
     * @param templateName - 템플릿 이름
     * @param emailContent - 템플릿에 넣을 값들
     * @return 발송성공여부(성공시 true)
     */
    private Boolean sendMail(String email, String templateName, EmailContent emailContent) {
        final String mailCharset = "utf-8";
        final String path =  (System.getProperty("user.dir") + "/backend/src/main/resources/templates/").replace("/",File.separator)  ;

        try {
            File input = new File(path + templateName);
            String subject = Jsoup.parse(input, mailCharset).getElementsByTag("title").text();

            MailDto mailDto = MailDto.builder()
                    .from(sender)
                    .to(email)
                    .subject(subject)
                    .template(templateName)
                    .build();

            // variable 매핑
            Context context = new Context();
            emailContent.getVariables().forEach(context::setVariable);

            // 메세지 생성
            MimeMessage message = javaMailSender.createMimeMessage();
                message.addRecipients(Message.RecipientType.TO, mailDto.getTo());
                message.setSubject(mailDto.getSubject());
                message.setContent(springTemplateEngine.process(mailDto.getTemplate(), context), "text/html; charset=" + mailCharset);
                message.setFrom(new InternetAddress(sender, "matching-goal"));
                javaMailSender.send(message);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 인증번호 검증
     * @param mailVerificationDto - email, code, name
     */
    public void verifyMail(MailVerificationDto mailVerificationDto) {
        // 코드 불일치
        if (! redisService.getData(VALID_PREFIX+mailVerificationDto.getEmail()).equals(mailVerificationDto.getCode()) )
            throw new CustomException(ErrorCode.INVALID_CODE);

        // 코드 일치
        redisService.deleteData(VALID_PREFIX+mailVerificationDto.getEmail());

        // 메일 발송
        sendWelcomeMail(mailVerificationDto.getEmail(), mailVerificationDto.getName());
    }
}

