package backend.hiteen.board.dto.response;

import backend.hiteen.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponse {

    private final Long id;
    private final String title;
    private final String content;

    public BoardResponse(Board board){
        this.id=board.getId();
        this.title=board.getTitle();
        this.content=board.getContent();
    }
}
