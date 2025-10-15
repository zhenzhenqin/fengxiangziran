package com.fengxiang;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class FengXiangApplication {
    public static void main(String[] args) {
        SpringApplication.run(FengXiangApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  fengxiang激活成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  __               _                           _   \n" +
                " / _| ___ _ __ ___(_) __ _ _ __   __ _ ___  __| |  \n" +
                "| |_ / _ \\ '__/ __| |/ _` | '_ \\ / _` / __|/ _` |  \n" +
                "|  _|  __/ |  \\__ \\ | (_| | | | | (_| \\__ \\ (_| |  \n" +
                "|_|  \\___|_|  |___/_|\\__, |_| |_|\\__,_|___/\\__,_|  \n" +
                "                     |___/                          \n");
    }
}
