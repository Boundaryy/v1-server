package com.boundary.boundarybackend.domain.sst.model.dto.internal;

import com.boundary.boundarybackend.domain.situation.model.entity.Situation;

public record CreateSttThreadRequest(
        String situation
) { public static CreateSttThreadRequest from(Situation situation) {
    return new CreateSttThreadRequest(situation.getContent());
}
}
