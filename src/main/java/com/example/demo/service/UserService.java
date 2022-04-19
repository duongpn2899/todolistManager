package com.example.demo.service;

import com.example.demo.common.JwtUtils;
import com.example.demo.dto.UserPostDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserTodoException;
import com.example.demo.exception.UserTodoNotFoundException;
import com.example.demo.mapping.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.exception.JwtRespones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserMapper userMapper;


//    public List<UserEntity> info(){
//        return userRepository.findAll();
//    }


    public JwtRespones login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                        ,loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new JwtRespones(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }


    public UserPostDto register(RegisterRequest registerRequest) {
        Optional<UserEntity> user = userRepository.findByUserName(registerRequest.getUsername());
        if (user.isPresent())
            throw new UserTodoException(HttpStatus.BAD_REQUEST.value(), "username existed!");
        else {
            UserEntity userEntity = new UserEntity();
            userEntity.setFullName(registerRequest.getFullname());
            userEntity.setUserName(registerRequest.getUsername());
            userEntity.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
            switch (registerRequest.getRole()) {
                case "user" :
                    List<RoleEntity> roleEntities = new ArrayList<>();
                    RoleEntity roleEntity = roleRepository.findByCode("USER");
                    roleEntities.add(roleEntity);
                    userEntity.setRoles(roleEntities);
                    break;
                case "admin":
                    List<RoleEntity> roleEntitie = new ArrayList<>();
                    RoleEntity role = roleRepository.findByCode("ADMIN");
                    roleEntitie.add(role);
                    userEntity.setRoles(roleEntitie);
                    break;
                default:
                    throw new UserTodoNotFoundException(HttpStatus.NOT_FOUND.value(),registerRequest.getRole() +" not found");
            }
            userRepository.save(userEntity);

            return userMapper.userEntityToUserPostDto(userEntity);
        }
    }
}
