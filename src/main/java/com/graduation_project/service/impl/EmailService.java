package com.graduation_project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String author;

    @Autowired
    TemplateEngine templateEngine;

    public boolean sendSimpleMail(String account,String code){

        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(code.split("")));
        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("mail.html", context);
        MimeMessage message=javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(author);
            helper.setTo(account);
            helper.setSubject("注册验证码");
            helper.setText(emailContent,true);
            javaMailSender.send(message);
            return true;
        }catch (MessagingException e) {
            return false;
        }

    }
}
