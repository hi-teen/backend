package backend.hiteen.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiHeader {
    private final int status;
    private final String message;
}
