package com.boundary.boundarybackend.domain.sst.model.dto.external;

import com.boundary.boundarybackend.domain.sst.model.entity.Sst;

public record GetFeedBackResponse(
        String feedBack
) {
    public static GetFeedBackResponse from(Sst sst) {
        return new GetFeedBackResponse(
                sst.getFeedBack()
        );
    }
}
