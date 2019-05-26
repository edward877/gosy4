package com.ulstu.gosy1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping(value = {"/"})
    public String registration() {
        return "registration";
    }

    @PostMapping("/")
    public ModelAndView saveUser(@ModelAttribute User user) {
        Map<String, Object> model = new HashMap();
        String viewName = "registration";

        if (userDao.findById(user.getLogin()).isPresent()) {
            model.put("error", "Логин уже занят");
        } else {
            try{
                userDao.save(user);
                model.put("login", user.getLogin());
                viewName = "redirect:/user";
            } catch (Exception ex) {
                model.put("error", "Что-то пошло не так");
            }
        }
        return new ModelAndView(viewName, model);
    }

    @GetMapping(value = {"user"})
    public ModelAndView getUser(String login) {
        Map<String, Object> model = new HashMap();
        Optional<User> user = userDao.findById(login);
        if (user.isPresent()) {
            model.put("user", userDao.findById(login).get());
        } else {
            model.put("error", "Пользователь не найден");
        }
        return new ModelAndView("user", model);

    }
}
