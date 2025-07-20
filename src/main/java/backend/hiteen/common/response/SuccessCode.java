package backend.hiteen.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    //member
    MEMBER_REGISTERED(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    MEMBER_FETCHED(HttpStatus.OK, "회원 정보를 조회합니다."),
    MEMBER_UPDATED(HttpStatus.OK, "회원 정보를 수정합니다."),
    MEMBER_LOGGED_IN(HttpStatus.OK, "로그인이 완료되었습니다."),
    MEMBER_REISSUED(HttpStatus.OK, "토큰이 재발급되었습니다."),
    MEMBER_CURRENT_INFO_FETCHED(HttpStatus.OK, "현재 로그인 된 사용자입니다."),
    MEMBER_LOGOUT(HttpStatus.OK, "로그아웃되었습니다."),


    //board
    BOARD_CREATED(HttpStatus.CREATED, "게시글이 등록되었습니다."),
    BOARD_DETAIL_FETCHED(HttpStatus.OK, "게시글을 단일 조회했습니다."),
    BOARD_ALL_FETCHED(HttpStatus.OK, "모든 게시글 목록을 조회했습니다."),
    MY_BOARD_LIST_FETCHED(HttpStatus.OK, "내가 작성한 게시글 목록을 조회했습니다."),
    POPULAR_BOARD_ALL_FETCHED(HttpStatus.OK, "모든 인기 게시글을 조회했습니다."),
    SEARCHED_BOARD_LIST_FETCHED(HttpStatus.OK, "검색된 게시글 목록을 조회했습니다."),

    //comment
    COMMENT_CREATED(HttpStatus.CREATED, "댓글이 등록되었습니다."),
    REPLY_CREATED(HttpStatus.CREATED,"대댓글이 등록되었습니다."),
    COMMENT_FETCHED(HttpStatus.OK, "댓글 목록을 불러왔습니다."),
    COMMENT_DELETED(HttpStatus.OK, "댓글이 삭제되었습니다."),
    COMMENT_LIKED_CREATED(HttpStatus.CREATED, "댓글 좋아요 되었습니다."),
    COMMENT_LIKED_DELETED(HttpStatus.OK, "댓글 좋아요가 취소되었습니다."),


    //love
    LOVE_CREATED(HttpStatus.CREATED, "게시글에 좋아요를 눌렀습니다."),
    LOVE_DELETED(HttpStatus.OK, "게시글 좋아요를 취소했습니다."),
    LOVED_BOARDS_FETCHED(HttpStatus.OK, "좋아요한 게시글 목록을 조회했습니다."),

    //scrap
    SCRAP_CREATED(HttpStatus.CREATED, "게시글이 스크랩되었습니다."),
    SCRAP_DELETED(HttpStatus.OK, "게시글 스크랩이 취소되었습니다."),
    SCRAPED_BOARDS_FETCHED(HttpStatus.OK, "스크랩한 게시글 목록을 조회했습니다."),

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
