package com.woltersklower.demo.service;

import com.woltersklower.demo.persistence.UserRepository;
import com.woltersklower.demo.validation.EmailExistsException;
import com.woltersklower.demo.web.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class UserService implements IUserService {

    private final UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User registerNewUser(final User user) throws EmailExistsException {
        if (emailExists(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }

}
