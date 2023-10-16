package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @author wxx
 * @version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendMailTset(){
        mailClient.sendMail("2563806166@qq.com","TEST","hello, testing!");
    }

    @Test
    public void sendMail_HTML_Test(){
        Context context=new Context();
        context.setVariable("username","wxxx");
        String process = templateEngine.process("/mail/demo", context);
        mailClient.sendMail("2563806166@qq.com","TEST",process);
    }
}
