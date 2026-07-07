package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.auth.dtos.*;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.UserAlreadyExistsException;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.common.utils.CookieUtils;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private  final UserRepository userRepository;
    private final  JwtService jwtService;
    private  final PasswordEncoder passwordEncoder;



    @Override
    public SignupResponse signUp(SignupRequest request) {

        Optional<User> user = userRepository.findByEmail(request.email());

        if(user.isPresent()) throw  new UserAlreadyExistsException("User already exists.");

        User toBeSaved = User.builder()
                .email(request.email())
                .name(request.name())
                .hashedPassword(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        userRepository.save(toBeSaved);

        return new SignupResponse(toBeSaved.getId(),toBeSaved.getName(),toBeSaved.getRole().toString());
    }

    @Override
    @Transactional
    public SignInResponse signin(SignInReuest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("No user found"));

        if(!passwordEncoder.matches(request.password(),user.getPassword())) throw  new RuntimeException("Bad credentials");

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new SignInResponse(accessToken,refreshToken) ;
    }

    @Override
    @Transactional
    public SignInResponse refreshToken(String refreshToken) {

        java.util.UUID id = jwtService.getUserIdFromToken(refreshToken);
        if(id == null) throw  new RuntimeException("bad credentials");
        User  user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No user found"));
        System.out.println("Id of user"+id);

        String accessToken = jwtService.generateAccessToken(user);
         return new SignInResponse(accessToken,refreshToken);
    }

    @Override
    @Transactional
    public UserDetail getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        System.out.println(authentication);
        System.out.println(authentication.getClass());
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getPrincipal().getClass());

        User user = userRepository.findById(authenticatedUser.getId()).orElseThrow(() -> new ResourceNotFoundException("No user found"));


       return new UserDetail(user.getId(),user.getName(),user.getEmail(),user.getContactNumber(),user.getRole());
    }

    @Override
    @Transactional
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(response,"access_token");
        CookieUtils.deleteCookie(response,"refresh_token");
        SecurityContextHolder.clearContext();
        return;

    }

}
