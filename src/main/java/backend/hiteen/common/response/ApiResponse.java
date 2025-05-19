package backend.hiteen.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final ApiHeader header;
    private final T data;
    private final boolean success;

    private ApiResponse(boolean success, ApiHeader header, T data) {
        this.success = success;
        this.header = header;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(SuccessCode code, T data) {
        return new ApiResponse<>(true, new ApiHeader(code.getStatus().value(), code.getMessage()), data);
    }

    public static <T> ApiResponse<T> success(SuccessCode code) {
        return new ApiResponse<>(true, new ApiHeader(code.getStatus().value(), code.getMessage()), null);
    }

    public static <T> ApiResponse<T> failure(ErrorCode code) {
        return new ApiResponse<>(false, new ApiHeader(code.getStatus().value(), code.getMessage()), null);
    }

    public static <T> ApiResponse<T> failure(ErrorCode code, String customMessage) {
        return new ApiResponse<>(false, new ApiHeader(code.getStatus().value(), customMessage), null);
    }

}
