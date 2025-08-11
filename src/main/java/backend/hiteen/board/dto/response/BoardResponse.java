package backend.hiteen.board.dto.response;

import backend.hiteen.board.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String writer;
    private final String category;
    private final String categoryLabel;
    private final int loveCount;
    private final int scrapCount;
    private final int commentCount;
    private final Long viewCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public BoardResponse(Board board){
        this.id=board.getId();
        this.writer=board.getDisplayWriterName();
        this.category=board.getCategory().name();
        this.categoryLabel=board.getCategoryLabel();
        this.title=board.getTitle();
        this.content=board.getContent();
        this.loveCount=board.getLoveCount();
        this.scrapCount= board.getScrapCount();
        this.commentCount=board.getCommentCount();
        this.createdAt = board.getCreatedAt();
        this.viewCount=board.getViewCount();
    }
}
