package com.woltersklower.demo.web.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woltersklower.demo.persistence.UserRepository;
import com.woltersklower.demo.service.IUserService;
import com.woltersklower.demo.validation.EmailExistsException;
import com.woltersklower.demo.web.model.User;

@Controller
@RequestMapping("/user")
class UserController {

    private final UserRepository userRepository;
    private final IUserService userService;

    UserController(UserRepository userRepository, IUserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("tl/list", "users", userRepository.findAll());
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute final User user) {
        return "tl/form";
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") User user) {
        return new ModelAndView("tl/view", "user", user);
    }

    @PostMapping
    public ModelAndView create(@Valid final User user, final BindingResult result, final RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
        final User saved;
        try {
            saved = userService.registerNewUser(user);
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("tl/form", "user", user);
        }
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/user/" + saved.getId());
    }

    @PostMapping("delete/{id}")
    public ModelAndView delete(@PathVariable("id") final Long id) {
        userRepository.deleteById(id);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") final User user) {
        return new ModelAndView("tl/form", "user", user);
    }

}
