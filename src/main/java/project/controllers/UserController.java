package project.controllers;

/**
 * Created by mariosk89 on 12-Mar-16.
 */
import org.springframework.web.bind.annotation.*;
import project.models.User;
import project.models.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getUserNameById(@PathVariable("id") String id) {
        String username;
        try {
            User user = userDao.findOne(Long.valueOf(id));
            username = String.valueOf(user.getUsername());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + username;
    }

    //TODO make it available for admin only
    @RequestMapping(method = RequestMethod.GET)
    public String getUserList() {
        StringBuilder userNames = new StringBuilder();
            try {
                List<User> users = (List<User>) userDao.findAll();
                if(!users.isEmpty()) {
                    for(User user : users){
                        userNames.append(user.getUsername());
                    }
                }
                else
                {
                    return "No users";
                }
        }
        catch (Exception ex) {
            return "No users";
        }
        return userNames.toString();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") String id) {
        try {
            User user = new User(Long.valueOf(id));
            userDao.delete(user);
        }
        catch (Exception ex) {
            return "Error deleting the user:" + ex.toString();
        }
        return "User succesfully deleted!";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") String id, @RequestParam String username) {
        try {
            User user = userDao.findOne(Long.valueOf(id));
            user.setUsername(username);
            userDao.save(user);
        }
        catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }
}
