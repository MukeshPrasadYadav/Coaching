package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.auth.dtos.*;
import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.AdminUserDetails;
import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.StudentUserDetails;
import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.TeacherUserDetails;
import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.UserDetail;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.DuplicateException;
import com.projects.coaching_offline_support.common.Service.FileService;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.common.utils.CookieUtils;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.repository.StudentRepository;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
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
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private  final UserRepository userRepository;
    private final  JwtService jwtService;
    private  final PasswordEncoder passwordEncoder;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final FileService fileService;
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

        String tokenType = jwtService.getTokenType(refreshToken);

        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Invalid refresh token");
        }

        java.util.UUID id = jwtService.getUserIdFromToken(refreshToken);

        User  user = RepositoryUtils.findOrThrowById(userRepository,id,"User");
        System.out.println("Id of user"+id);

        String accessToken = jwtService.generateAccessToken(user);
         return new SignInResponse(accessToken,refreshToken);
    }

    @Override
    @Transactional
    public UserDetail getMe() {

        UUID userId = CurrentUser.get().getId();

        User user = RepositoryUtils.findOrThrowById(userRepository,userId, "User");



        String profilePic = null;
        if(user.getProfilePic() != null){
            profilePic = fileService.getProfilePicture(user.getProfilePic());;
        }

        if(!user.isProfileCompleted()){
            return AdminUserDetails.from(user,profilePic);
        }



        if(user.getRole().equals(Role.TEACHER)){
            Teacher teacher = RepositoryUtils.findOrThrowById(teacherRepository,userId,"Teacher");
            return TeacherUserDetails.from(teacher,profilePic);
        }
        else if(user.getRole().equals(Role.ADMIN)){
            return AdminUserDetails.from(user,profilePic);
        } else if (user.getRole().equals(Role.STUDENT)) {
            Student student = RepositoryUtils.findOrThrowById(studentRepository,userId,"Student");
            return StudentUserDetails.from(student,profilePic);

        }
        return  null;
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
