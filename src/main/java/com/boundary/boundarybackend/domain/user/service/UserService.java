package com.boundary.boundarybackend.domain.user.service;

import com.boundary.boundarybackend.domain.user.model.dto.request.ParentSignUpRequest;
import com.boundary.boundarybackend.domain.user.model.dto.response.UserResponse;
import com.boundary.boundarybackend.domain.user.model.entity.Attendance;
import com.boundary.boundarybackend.domain.user.repository.AttendanceRepository;
import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.user.model.dto.request.ChildSignUpRequest;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public void createChild(ChildSignUpRequest req) {
        try {
            if (userRepository.existsByUserId(req.getUserId())) {
                log.error("사용자 아이디가 이미 존재합니다: {}", req.getUserId());
                throw new BusinessException(ErrorCode.DUPLICATED);
            }

                userRepository.save(User.builder()
                    .name(req.getName()) // 이름
                    .age(req.getAge()) // 나이
                    .phoneNum(req.getPhoneNum()) // 전화번호
                    .gender(req.getGender()) // 성별
                    .userId(req.getUserId()) // 사용자 아이디
                    .password(new BCryptPasswordEncoder().encode(req.getPassword())) // 비밀번호
                    .role(req.getRole()) // 부모or아이 권한
                    .point(req.getPoint()) // 포인트
                    .build()
            );
        } catch (Exception e) {
            log.error("회원 생성 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public void createParent(ParentSignUpRequest req) {
        if (userRepository.existsByUserId(req.getUserId())) {
            log.error("사용자 아이디가 이미 존재합니다: {}", req.getUserId());
            throw new BusinessException(ErrorCode.DUPLICATED);
        }

        if (userRepository.existsByChildId(req.getChildId())) {
            log.error("아이 아이디가 이미 존재합니다: {}", req.getChildId());
            throw new BusinessException(ErrorCode.DUPLICATED);
        }

        try {
            userRepository.save(User.builder()
                    .phoneNum(req.getPhoneNum()) // 전화번호
                    .userId(req.getUserId()) // 사용자 아이디
                    .password(new BCryptPasswordEncoder().encode(req.getPassword())) // 비밀번호
                    .role(req.getRole()) // 부모 or 아이 권한
                    .childId(req.getChildId()) // 아이 아이디
                    .build()
            );
        } catch (Exception e) {
            log.error("회원 생성 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }


    @Transactional
    public boolean recordAttendance(Long userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.now().format(formatter);

        Optional<Attendance> todayAttendance = attendanceRepository.findByUserIdAndAttendanceDate(userId, formattedDate);

        if (todayAttendance.isPresent()) {
            return false;
        }

        // 새로운 출석 기록 생성
        Attendance attendance = Attendance.builder()
                .userId(userId)
                .attendanceDate(formattedDate)
                .build();

        attendanceRepository.save(attendance);
        return true;
    }


    @Transactional
    public UserResponse getUserById(Long userId) {
        // userId로 해당 User 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // Return the relevant fields in a UserResponse
        return new UserResponse(
                user.getName(),
                user.getUserId(),
                user.getPoint(),
                user.getChildId()
        );
    }

}
