package com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse;

public sealed interface UserDetail permits StudentUserDetails,TeacherUserDetails,AdminUserDetails {
}
