package backend.hiteen.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    //Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    MEMBER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    MEMBER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 정보가 유효하지 않습니다."),
    MEMBER_REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 Refresh Token이 없습니다."),
    MEMBER_REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "Refresh Token이 일치하지 않습니다."),

    //Board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    BOARD_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 작성에 실패했습니다."),
    KEYWORD_REQUIRED(HttpStatus.BAD_REQUEST, "검색어를 입력해주세요."),
    NO_PERMISSION_TO_DELETE_BOARD(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),

    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    COMMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN,"댓글에 대한 권한이 없습니다."),

    //Love
    LOVE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다."),
    LOVE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요가 존재하지 않습니다."),

    //Scrap
    SCRAP_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 스크랩한 게시글입니다."),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "스크랩 정보가 존재하지 않습니다."),

    //Message
    MESSAGE_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "쪽지방을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "쪽지를 찾을 수 없습니다."),
    CANNOT_SEND_MESSAGE_TO_SELF(HttpStatus.BAD_REQUEST, "자기 자신에게 쪽지를 보낼 수 없습니다."),
    MESSAGE_TARGET_NOT_SPECIFIED(HttpStatus.BAD_REQUEST, "쪽지 받을 대상을 지정해 주세요."),
    COMMENT_ANONYMOUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 익명번호의 댓글이 존재하지 않습니다."),
    INVALID_MESSAGE_TARGET(HttpStatus.BAD_REQUEST, "쪽지 대상은 게시글 작성자 또는 특정 댓글러 중 하나만 지정할 수 있습니다."),
    MESSAGE_ROOM_PERMISSION_DENIED(HttpStatus.FORBIDDEN,"이 쪽지방에 메시지를 보낼 권한이 없습니다."),


    //Meal
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "급식 정보를 찾을 수 없습니다."),
    MEAL_FETCH_FAILED(HttpStatus.BAD_GATEWAY, "급식 정보를 가져오는 데 실패했습니다."),

    //Timetable
    TIMETABLE_NOT_FOUND(HttpStatus.NOT_FOUND,"시간표를 찾을 수 없습니다."),
    TIMETABLE_FETCH_FAILED(HttpStatus.BAD_GATEWAY,"시간표를 가져오는 데 실패했습니다."),

    //School
    SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND,    "학교가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
