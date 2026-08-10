package com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse;

import com.projects.coaching_offline_support.user.User;

public record AdminUserDetails(
        UserInfo user
) implements UserDetail {
    public static AdminUserDetails from(User user,String profilepic){
        return new AdminUserDetails(
                UserInfo.from(user,profilepic)
        );
    }
}
