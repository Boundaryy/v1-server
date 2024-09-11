package com.boundary.boundarybackend.domain.sst.model.dto.external;

import java.time.LocalDateTime;

public record GetChildEducationResponse(
        String threadId,
        String feedBack,
        LocalDateTime finishTime
) {
}
