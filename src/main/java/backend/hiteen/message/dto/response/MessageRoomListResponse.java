package backend.hiteen.message.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageRoomListResponse {
    private Long roomId;
    private Long boardId;
    private String boardTitle;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private String lastMessageNickname;
    private Integer unreadCount;
    private String chatNickname; //(익명n,작성자,익명)
}
