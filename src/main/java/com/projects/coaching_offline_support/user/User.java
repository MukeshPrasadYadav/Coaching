package com.projects.coaching_offline_support.user;

import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(nullable = false,length = 15)
    private String contactNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = Set.of(Role.PENDING);

    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    private String email;

    @Column(nullable = false)
    private  String hashedPassword;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).toList());

        if(permissions != null)
            authorities.addAll(
                    permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.name())).toList()
            );

        return  authorities;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
