package com.project.project.dawi.controller;

import com.project.project.dawi.dto.RegisterDTO;
import com.project.project.dawi.dto.UserDTO;
import com.project.project.dawi.entity.User;
import com.project.project.dawi.service.CustomUserDetailsService;
import com.project.project.dawi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacion")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/registrarse")
    ResponseEntity<String> registrarUsuario(@RequestBody RegisterDTO request)
    {
        //Pendiente: Validación de Empleado y Inserción de Empleado

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(passwordEncoder.encode(request.getPassword()));

        boolean userValResult = userService.validateUser(userDTO);

        if (!userValResult){
            return ResponseEntity.badRequest().body("¡Error! Los datos ingresados no son válidos.");
        }

        return ResponseEntity.ok().body(userService.saveUser(userDTO));
    }

    @GetMapping("/verificar/{email}")

    ResponseEntity<String> verificarEmail(@PathVariable String email)
    {
        User user = userService.findByEmail(email);

        if (user == null){
            return ResponseEntity.badRequest().body("¡Error! No existe un usuario asociado a ese correo.");
        }
        return ResponseEntity.ok().body("¡Éxito! El correo está asociado a un usuario.");

    }

    @PutMapping("/cambiarContrasenia")
    ResponseEntity<String> cambiarContrasenia(@RequestBody UserDTO userDTO)
    {
        User user = userService.findByEmailPassword(userDTO);

        if (user == null){
            return ResponseEntity.badRequest().body("¡Error! Los datos ingresados no son válidos.");
        }
        return ResponseEntity.ok().body(userService.updatePassword(user, userDTO.getNewPassword()));
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<String> iniciarSesion(@RequestBody UserDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("¡Éxito! Se inició la sesión.");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("¡Error! Los datos ingresados no son válidos.");
        }
    }

    @DeleteMapping("/cerrarSesion")
    ResponseEntity<String> cerrarSesion(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("¡Éxito! Sesión finalizada.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado o sin sesión activa.");
        }
    }
}
