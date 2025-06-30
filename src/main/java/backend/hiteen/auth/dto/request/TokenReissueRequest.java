package backend.hiteen.auth.dto.request;

import lombok.Getter;

@Getter
public class TokenReissueRequest {
    private String refreshToken;
}
