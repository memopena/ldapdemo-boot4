package com.woltersklower.demo.service;


import com.woltersklower.demo.validation.EmailExistsException;
import com.woltersklower.demo.web.model.User;

public interface IUserService {

    User registerNewUser(User user) throws EmailExistsException;

}
