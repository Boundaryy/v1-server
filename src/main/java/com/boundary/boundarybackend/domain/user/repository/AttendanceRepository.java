package com.boundary.boundarybackend.domain.user.repository;

import com.boundary.boundarybackend.domain.user.model.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserIdAndAttendanceDate(Long userId, String attendanceDate);
}
