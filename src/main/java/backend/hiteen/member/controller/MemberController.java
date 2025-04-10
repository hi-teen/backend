package backend.hiteen.member.controller;

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

    @PostMapping("/signUp")
    @Operation(summary = "회원가입",description = "사용자가 회원가입을 합니다.")
    public ResponseEntity<MemberResponse> signUp(@Valid @RequestBody MemberCreateRequest request){
        MemberResponse memberResponse= memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }
}
