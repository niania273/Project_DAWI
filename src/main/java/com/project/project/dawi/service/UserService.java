package com.project.project.dawi.service;

import com.project.project.dawi.dto.UserDTO;
import com.project.project.dawi.entity.Role;
import com.project.project.dawi.entity.User;
import com.project.project.dawi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email){
        return repository.findByEmail(email);
    }
    
    public User findByEmailPassword(UserDTO userDTO){

        User foundUser = findByEmail(userDTO.getEmail());

        if (foundUser != null){
            boolean match = passwordEncoder.matches(userDTO.getPassword(), foundUser.getPassword());
            if (!match) {
                return null;
            }
        }
        return foundUser;
    }

    public boolean validateUser(UserDTO userDTO) {
        return userDTO.getEmail() != null && userDTO.getPassword() != null;
    }

    public String saveUser(UserDTO userDTO){

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        Role role = new Role();
        role.setId(1);
        user.setRole(role);

        repository.save(user);

        return "¡Éxito! Usuario registrado.";
    }

    public String updatePassword(User user, String newPassword)
    {
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        return "¡Éxito! Se actualizó la contraseña";
    }
}
