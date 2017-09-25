package project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.models.User;
import project.models.UserDao;

/**
 * Created by mariosk89 on 20-Mar-16.
 */
@RestController
public class UserRegistrationController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String create(@RequestParam String username, @RequestParam String password) {
        User user = null;
        try {
            user = new User(username, password);
            userDao.save(user);
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created! (id = " + user.getId() + ")";
    }
}
