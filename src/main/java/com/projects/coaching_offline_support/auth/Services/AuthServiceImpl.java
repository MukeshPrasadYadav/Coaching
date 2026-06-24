package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.auth.dtos.SignInResponse;
import com.projects.coaching_offline_support.auth.dtos.SignInReuest;
import com.projects.coaching_offline_support.auth.dtos.SignupRequest;
import com.projects.coaching_offline_support.auth.dtos.SignupResponse;
import com.projects.coaching_offline_support.common.Exceptions.UserAlreadyExistsException;
import com.projects.coaching_offline_support.common.Exceptions.UserNotFoundException;
import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private  final UserRepository userRepository;
    private final  JwtService jwtService;
    private  final PasswordEncoder passwordEncoder;
    @Override
    public SignupResponse signUp(SignupRequest request) {

        Optional<User> user = userRepository.findByContactNumber(request.contactNumber());

        if(user.isPresent()) throw  new UserAlreadyExistsException("User already exists.");

        User toBeSaved = User.builder()
                .name(request.name())
                .contactNumber(request.contactNumber())
                .hashedPassword(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ADMIN))
                .permissions(Set.of(Permission.ADD_COACHING))
                .build();

        userRepository.save(toBeSaved);

        return new SignupResponse(toBeSaved.getId(),toBeSaved.getName(),toBeSaved.getRoles().toString());
    }

    @Override
    public SignInResponse signin(SignInReuest request) {
        User user = userRepository.findByContactNumber(request.contactNumber()).orElseThrow(() -> new UserNotFoundException("No user found"));

        if(!passwordEncoder.matches(request.password(),user.getPassword())) throw  new RuntimeException("Bad credentials");

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new SignInResponse(accessToken,refreshToken);
    }

    @Override
    public SignInResponse refreshToken(String refreshToken) {

        java.util.UUID id = jwtService.getUserIdFromToken(refreshToken);
        if(id == null) throw  new RuntimeException("bad credentials");
        User  user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("No user found"));
        System.out.println("Id of user"+id);

        String accessToken = jwtService.generateAccessToken(user);
        return  new SignInResponse(accessToken,refreshToken);
    }

}
