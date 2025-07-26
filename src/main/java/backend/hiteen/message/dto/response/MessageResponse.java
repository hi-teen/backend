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

    @Schema(description = "메시지 ID")
    private Long messageId;

    @Schema(description = "쪽지방 ID")
    private Long roomId;

    @Schema(description = "쪽지 내용")
    private String content;

    private LocalDateTime createdAt;

    @Schema(description = "작성자/익명N/익명")
    private String chatNickname;

    private Boolean isMine;
}
