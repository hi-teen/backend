package backend.hiteen.message.mapper;

import backend.hiteen.message.dto.response.MessageResponse;
import backend.hiteen.message.entity.Message;
import backend.hiteen.message.entity.MessageRoom;


public class MessageMapper {
    private MessageMapper() {
    }

    // 메시지 단건 DTO 변환 시
    public static String computeDisplayName(Message message) {
        MessageRoom room = message.getMessageRoom();
        Long boardOwnerId = room.getBoard().getMember().getId();

        // 1) 작성자 본인이 보낸 메시지면 “작성자”
        if (message.getSenderId().equals(boardOwnerId)) {
            return "작성자";
        }
        // 2) 익명번호가 있으면 “익명 {번호}”, 없으면 “익명”
        Integer anonNum = room.getAnonymousNumber();
        return anonNum != null
                ? "익명 " + anonNum
                : "익명";
    }

    // 방 목록 DTO 변환 시
    public static String computeDisplayName(MessageRoom room, Long memberId) {
        Long boardOwnerId = room.getBoard().getMember().getId();

        if (memberId.equals(boardOwnerId)) {
            return "작성자";
        }
        Integer anonNum = room.getAnonymousNumber();
        return anonNum != null
                ? "익명 " + anonNum
                : "익명";
    }

    public static MessageResponse toDto(Message message, Long memberId) {
        return new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getContent(),
                message.getCreatedAt(),
                computeDisplayName(message),
                message.getSenderId().equals(memberId)
        );
    }
}