package matchingGoal.matchingGoal.mail.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.mail.exception.InvalidValidationCodeException;
import org.apache.commons.lang3.RandomStringUtils;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.mail.dto.MailDto;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final RedisService redisService;
    private final String MAIL_CHARSET = "utf-8";
    private final String VALID_PREFIX = "VAL_";
    private final String PATH = "src/main/resources/templates/";

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 이메일 인자들을 템플릿에 매핑
     *
     * @param emailValue  이메일인자
     * @param template    템플릿명
     * @return springTemplateEngine.process
     */
    private String setContext(HashMap<String,String> emailValue, String template){
        Context context = new Context();
        emailValue.forEach(context::setVariable);
        return springTemplateEngine.process(template, context);
    }

    /**
     * 메일 생성
     * @param dto - to, from, subject, templateName
     * @param emailValue  이메일인자
     * @return MimeMessage
     */
    private MimeMessage createMessage(MailDto dto, HashMap<String,String> emailValue){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, dto.getTo());
            message.setSubject(dto.getSubject());
            message.setContent(setContext(emailValue, dto.getTemplate()), "text/html; charset="+MAIL_CHARSET);
            message.setFrom(new InternetAddress(sender,"matching-goal"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  message;
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

        log.info("\n--- CODE : "+code);

        HashMap<String,String> emailValues = new HashMap<>();
        emailValues.put("code",code);
        emailValues.put("expire", expire);

        log.info("\n--- emailValues : "+emailValues);

        if (sendMail(TEMPLATE_NAME, email, emailValues)) {
            redisService.setData(VALID_PREFIX+email, code, redisDurationInMinutes);
            return true;
        }
        else
            return false;
    }

    /**
     * 메일 발송
     * @param templateName - 템플릿 이름
     * @param email - 이메일
     * @param emailValue - 템플릿에 넣을 값들
     * @return 발송성공여부(성공시 true)
     */
    private Boolean sendMail(String templateName, String email, HashMap<String, String> emailValue){
        String subject;

        try{
            File input = new File(PATH + templateName);
            subject = Jsoup.parse(input,MAIL_CHARSET).getElementsByTag("title").text();
            log.info("\n subject : "+subject);

            MailDto mailDto = MailDto.builder()
                    .from(sender)
                    .to(email)
                    .subject(subject)
                    .template(templateName)
                    .build();

            MimeMessage mimeMessage = createMessage(mailDto,emailValue);
            javaMailSender.send(mimeMessage);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * 인증번호 검증
     * @param mailVerificationDto - email, code, name
     */
    public void verifyMail(MailVerificationDto mailVerificationDto) {
        if (! redisService.getData(VALID_PREFIX+mailVerificationDto.getEmail()).equals(mailVerificationDto.getCode()) )
            throw new InvalidValidationCodeException(ErrorCode.INVALID_CODE);
        redisService.deleteData(VALID_PREFIX+mailVerificationDto.getEmail());

        // 가입 환영 메일 발송
        sendWelcomeMail(mailVerificationDto.getEmail(), mailVerificationDto.getName());
    }

    public void sendWelcomeMail(String email, String name){
        final String TEMPLATE_NAME = "welcome.html";
        HashMap<String,String> emailValues = new HashMap<>();
        emailValues.put("name",name);
        sendMail(TEMPLATE_NAME, email, emailValues);
    }
}
