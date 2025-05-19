package backend.hiteen.message.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class MessageResponse {

    @Schema(description = "메시지 ID", example = "10")
    private Long messageId;

    @Schema(description = "쪽지방 ID", example = "5")
    private Long roomId;

    @Schema(description = "발신자 ID", example = "2")
    private Long senderId;

    @Schema(description = "쪽지 내용", example = "반갑습니다!")
    private String content;

    private LocalDateTime createdAt;

    @Schema(description = "작성자", example = "작성자")
    private String chatNickname;
}
