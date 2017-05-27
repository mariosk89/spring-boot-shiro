package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@Configuration
//@EnableAutoConfiguration
//@EnableWebMvc
//@ComponentScan({"core", "project/configuration", "project/controllers", "project/interceptors", "project/services", "project/exception", "project/listeners", "project/services", "project/models"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("Application Context succesfully loaded...!");

    }

}
