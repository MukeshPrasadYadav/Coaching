package com.projects.coaching_offline_support.user;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 50)
    private String name;


    private String contactNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private  String hashedPassword;

    @Builder.Default
    private List<UUID> coachingIds = new ArrayList<>();

    @Builder.Default
    private List<UUID> batchIds = new ArrayList<>();







    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        if(role != null) authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));

        return  authorities;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void setCoachingIds(UUID coachingId) {
        this.getCoachingIds().add(coachingId);
    }
}
