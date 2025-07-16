package backend.hiteen.member.controller;

import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.member.dto.request.MemberCreateRequest;
import backend.hiteen.member.dto.response.MemberResponse;
import backend.hiteen.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name="Member", description = "사용자 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입",description = "사용자가 회원가입을 합니다. 비밀번호는 6자 이상이며, 영문자와 숫자를 최소 1자 이상 포함해야 합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(@Valid @RequestBody MemberCreateRequest request){
        MemberResponse memberResponse= memberService.signUp(request);
        return ResponseEntity.status(SuccessCode.MEMBER_REGISTERED.getStatus()).body(ApiResponse.success(SuccessCode.MEMBER_REGISTERED, memberResponse));
    }
}
