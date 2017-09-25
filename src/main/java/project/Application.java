package project;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

//@Configuration
//@EnableAutoConfiguration
//@EnableWebMvc
//@ComponentScan({"core", "project.configuration", "project.controllers", "project.interceptors", "project.services", "project.exception", "project.listeners", "project.services", "project.models"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        LoggerFactory.getLogger("");

		System.out.println("project.Application Context succesfully loaded...!");
    }

}
