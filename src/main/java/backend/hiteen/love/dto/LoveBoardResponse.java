package backend.hiteen.love.dto;

import backend.hiteen.board.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
//TODO: @Schema(description, example) 각 필드마다 추가

@Getter
public class LoveBoardResponse {

    private Long id;
    private String title;
    private String content;
    private int loveCount;
    private int scrapCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public LoveBoardResponse(Board board){
        this.id= board.getId();
        this.title= board.getTitle();
        this.content= board.getContent();
        this.loveCount=board.getLoveCount();
        this.scrapCount=board.getScrapCount();
        this.createdDate=board.getCreatedDate();
    }

}
