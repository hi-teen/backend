package backend.hiteen.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    //member
    MEMBER_REGISTERED(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    MEMBER_LOGGED_IN(HttpStatus.OK, "로그인이 완료되었습니다."),
    MEMBER_REISSUED(HttpStatus.OK, "토큰이 재발급되었습니다."),
    MEMBER_INFO_FETCHED(HttpStatus.OK, "회원 정보를 조회했습니다."),
    //board

    //comment
    COMMENT_CREATED(HttpStatus.CREATED, "댓글이 등록되었습니다."),
    REPLY_CREATED(HttpStatus.CREATED,"대댓글이 등록되었습니다."),
    COMMENT_FETCHED(HttpStatus.OK, "댓글 목록을 불러왔습니다."),
    //love

    //scrap

    //message
    MESSAGE_ROOM_CREATED(HttpStatus.CREATED, "쪽지방이 생성되었습니다."),
    MESSAGE_SENT(HttpStatus.CREATED, "쪽지를 보냈습니다."),
    MESSAGE_SENT_IN_ROOM(HttpStatus.CREATED, "쪽지방에 메시지를 보냈습니다."),
    MESSAGES_FETCHED(HttpStatus.OK, "쪽지 목록을 조회했습니다."),
    MESSAGE_POLLED(HttpStatus.OK, "롱폴링으로 새 메시지를 조회했습니다()"),

    //Meal
    MEAL_FETCHED(HttpStatus.OK, "급식 조회했습니다."),

    //Timetable
    TIMETABLE_FETCHED(HttpStatus.OK, "시간표를 조회했습니다.");


    private final HttpStatus status;
    private final String message;

    SuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
