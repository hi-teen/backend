package backend.hiteen.auth.controller;

import backend.hiteen.auth.dto.LoginRequest;
import backend.hiteen.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
