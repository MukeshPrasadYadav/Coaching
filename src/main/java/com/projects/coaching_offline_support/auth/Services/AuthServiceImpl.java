package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.auth.dtos.*;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.DuplicateException;
import com.projects.coaching_offline_support.common.utils.CookieUtils;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private  final UserRepository userRepository;
    private final  JwtService jwtService;
    private  final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;



    @Auditable(
            logType = LogType.USER,
            actionType = ActionType.CREATED,
            description = "New #{#request.role} with email : #{#request.email} user signed up"
    )
    @Transactional
    @Override
    public SignupResponse signUp(SignupRequest request) {

        Optional<User> user = userRepository.findByEmail(request.email());

        if(user.isPresent()) throw  new DuplicateException("User already exists.");

        User toBeSaved = User.builder()
                .email(request.email())
                .name(request.name())
                .hashedPassword(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        userRepository.save(toBeSaved);

//        publisher.publishEvent(
//                 UserSignedUpEvent.fromEntity(toBeSaved)
//        );

        return new SignupResponse(toBeSaved.getId(),toBeSaved.getName(),toBeSaved.getRole().toString());
    }

    @Override
    @Transactional
    @Auditable(
            logType = LogType.USER,
            actionType = ActionType.LOGGED_IN,
            description = "user #{#request.email} logged in."
    )
    public SignInResponse signin(SignInReuest request) throws BadRequestException {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("No user found"));

        if(!passwordEncoder.matches(request.password(),user.getPassword())) throw new BadRequestException("Wrong password");

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


        User user = userRepository.findById(authenticatedUser.getId()).orElseThrow(() -> new ResourceNotFoundException("No user found"));

       return new UserDetail(user.getId(),user.getName(),user.getEmail(),user.getContactNumber(),user.getRole(),user.isProfileCompleted(),user.getAddress());
    }

    @Override
    @Transactional
    @Auditable(
            logType = LogType.USER,
            actionType = ActionType.LOGGED_OUT,
            description = "user #{CurrentUser.get().getId()} logged out."
    )
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(response,"access_token");
        CookieUtils.deleteCookie(response,"refresh_token");
        SecurityContextHolder.clearContext();
        return;

    }

}
