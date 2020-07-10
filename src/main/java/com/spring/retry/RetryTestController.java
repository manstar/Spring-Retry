package com.spring.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryTestController {

    @Autowired
    private MyService myService;
    @Autowired
    private RetryTemplate retryTemplate;

    @GetMapping("/retry/success")
    public int count() throws Exception {
        return myService.countContents();
    }

    @GetMapping("/retry/exception")
    public int insert()  throws Exception{
        return myService.insertContents();
    }

    @GetMapping("/retry/recover")
    public int delete() throws Exception {
        try {
            return myService.deleteContents("delete from contents");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @GetMapping("/retry/template")
    public int countRetryTemplate() {
        return retryTemplate.execute(new RetryCallback<Integer, RuntimeException>()  {
            @Override
            public Integer doWithRetry(RetryContext context) {
                return myService.countContentsForRetryTemplate();
            }
        });
    }

    @GetMapping("/retry/template/lambda")
    public int countRetryTemplateLambda() {
        return retryTemplate.execute(context -> myService.countContentsForRetryTemplate());
    }
}
