package backend.hiteen.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    //member

    //board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),

    //comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),

    //love

    //scrap

    //message
    MESSAGE_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "쪽지방을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "쪽지를 찾을 수 없습니다."),

    //Meal
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "급식 정보를 찾을 수 없습니다."),
    MEAL_FETCH_FAILED(HttpStatus.BAD_GATEWAY, "급식 정보를 가져오는 데 실패했습니다."),

    //Timetable
    TIMETABLE_NOT_FOUND      (HttpStatus.NOT_FOUND,     "시간표를 찾을 수 없습니다."),
    TIMETABLE_FETCH_FAILED   (HttpStatus.BAD_GATEWAY,    "시간표를 가져오는 데 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
