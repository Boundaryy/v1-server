package com.boundary.boundarybackend.domain.facade;

import com.boundary.boundarybackend.domain.user.model.dto.response.UserResponse;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import com.boundary.boundarybackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    @Transactional
    public UserResponse getUser(Long userId) {
        userService.recordAttendance(userId);
        UserResponse user = userService.getUserById(userId);

        return user;
    }
}