package com.boundary.boundarybackend.domain.sst.model.dto.external;

import com.boundary.boundarybackend.domain.sst.model.dto.internal.BotMessageResponse;

public record BotChatResponse(
        String botMessage
) {
    public static BotChatResponse from(BotMessageResponse botMessageResponse) {
        return new BotChatResponse(botMessageResponse.botMessage());

    }
}
