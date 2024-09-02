package com.boundary.boundarybackend.domain.user.model.dto.request;

import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildSignUpRequest
{
    @Schema(description = "이름")
    private String name; // 이름

    @Schema(description = "나이")
    private Integer age; // 나이

    @Schema(description = "전화번호")
    private String phoneNum; // 전화번호

    @Schema(description = "성별")
    private String gender; // 성별

    @Schema(description = "사용자 아이디")
    private String userId; // 아이디

    @Schema(description = "비밀번호")
    private String password; // 비밀번호

    @Schema(description = "부모or아이 권한")
    private MemberRole role; // 부모or아이

    @Schema(description = "아이 레벨 포인트")
    private Integer point; // 포인트
}
