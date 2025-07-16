package backend.hiteen.auth.controller;

import backend.hiteen.auth.dto.request.LoginRequest;
import backend.hiteen.auth.dto.request.TokenReissueRequest;
import backend.hiteen.auth.dto.response.TokenResponse;
import backend.hiteen.auth.security.CustomUserPrincipal;
import backend.hiteen.auth.service.AuthService;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "사용자 인증 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 로그인 합니다.")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request){
        TokenResponse tokenResponse= authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(SuccessCode.MEMBER_LOGGED_IN.getStatus()).body(ApiResponse.success(SuccessCode.MEMBER_LOGGED_IN, tokenResponse));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 Access Token을 재발급합니다.")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(@RequestBody TokenReissueRequest request) {
        TokenResponse tokenResponse = authService.reissue(request.getRefreshToken());
        return ResponseEntity.status(SuccessCode.MEMBER_REISSUED.getStatus()).body(ApiResponse.success(SuccessCode.MEMBER_REISSUED, tokenResponse));
    }

    @GetMapping("/me")
    @Operation(summary = "로그인 된 사용자 정보 확인", description = "현재 인증된 사용자의 이메일 정보를 확인합니다.")
    public ResponseEntity<ApiResponse<String>> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(SuccessCode.MEMBER_INFO_FETCHED.getStatus()).body(ApiResponse.success(SuccessCode.MEMBER_INFO_FETCHED,email));
    }

}
