package project.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mariosk89 on 22-Mar-16.
 */
@RestController
public class LoginController {

    @RequestMapping(value = "/logins", method = RequestMethod.POST)
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam String rememberMe, HttpServletRequest request, HttpServletResponse response) {
        Subject currentUser = SecurityUtils.getSubject();
        if(currentUser.isAuthenticated()){
            //redirect
        }
        else
        {
            AuthenticationToken token =  new UsernamePasswordToken(username, password);
            currentUser.login(token);
            try {
                WebUtils.redirectToSavedRequest(request, response, "index.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
