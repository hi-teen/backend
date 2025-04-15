package backend.hiteen.auth.controller;

import backend.hiteen.auth.dto.LoginRequest;
import backend.hiteen.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "사용자 인증 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 로그인 합니다.")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request){
        String token= authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }
    @GetMapping("/me")
    public ResponseEntity<String> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("현재 로그인된 사용자: " + email);
    }

}
