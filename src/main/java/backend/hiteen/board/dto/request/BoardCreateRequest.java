package backend.hiteen.board.dto.request;

import backend.hiteen.board.entity.Board;
import backend.hiteen.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    private Long memberId;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;


    public Board toEntity(Member member){
        return new Board(member,title, content);
    }

}




