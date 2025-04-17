package backend.hiteen.scrap.dto;

import backend.hiteen.board.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapBoardResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final int loveCount;
    private final int scrapCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdDate;

    public ScrapBoardResponse(Board board){
        this.id= board.getId();
        this.title= board.getTitle();
        this.content= board.getContent();
        this.loveCount= board.getLoveCount();
        this.scrapCount= board.getScrapCount();
        this.createdDate=board.getCreatedDate();
    }

}
