package com.boundary.boundarybackend.domain.sst.service;

import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.sst.model.dto.external.GetFeedBackResponse;
import com.boundary.boundarybackend.domain.sst.model.entity.Sst;
import com.boundary.boundarybackend.domain.sst.repository.SstRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuerySstService {
    private final SstRepository sstRepository;

    public ResponseEntity<GetFeedBackResponse> getFeedBack(String threadId) {
        Sst s = sstRepository.findByThreadId(threadId).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return ResponseEntity.ok(GetFeedBackResponse.from(s));
    }
}
