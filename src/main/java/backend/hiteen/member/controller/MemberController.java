package backend.hiteen.member.controller;

import backend.hiteen.auth.security.CustomUserPrincipal;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.member.dto.request.MemberCreateRequest;
import backend.hiteen.member.dto.request.MemberUpdateRequest;
import backend.hiteen.member.dto.response.MemberResponse;
import backend.hiteen.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "Member", description = "사용자 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입",description = "사용자가 회원가입을 합니다. 비밀번호는 6자 이상이며, 영문자와 숫자를 최소 1자 이상 포함해야 합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(@Valid @RequestBody MemberCreateRequest request){
        MemberResponse memberResponse= memberService.signUp(request);
        return ResponseEntity.status(SuccessCode.MEMBER_REGISTERED.getStatus()).body(ApiResponse.success(SuccessCode.MEMBER_REGISTERED, memberResponse));
    }

    // 내 정보 조회
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인된 내 회원 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Long memberId = principal.getId();
        MemberResponse res = memberService.getMember(memberId);
        return ResponseEntity
                .status(SuccessCode.MEMBER_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.MEMBER_FETCHED, res));
    }

    // 내 정보 수정
    @PutMapping("/me")
    @Operation(summary = "내 정보 수정", description = "로그인된 내 회원 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMyProfile(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody MemberUpdateRequest req
    ) {
        Long memberId = principal.getId();
        MemberResponse res = memberService.updateMember(memberId, req);
        return ResponseEntity
                .status(SuccessCode.MEMBER_UPDATED.getStatus())
                .body(ApiResponse.success(SuccessCode.MEMBER_UPDATED, res));
    }
}
