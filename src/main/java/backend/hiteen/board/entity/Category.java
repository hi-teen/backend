package backend.hiteen.board.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Category {
    @Schema(description = "자유게시판")
    FREE("자유게시판"),

    @Schema(description = "비밀게시판")
    SECRET("비밀게시판"),

    @Schema(description = "홍보게시판")
    PROMOTION("홍보게시판"),

    @Schema(description = "정보게시판")
    INFORMATION("정보게시판"),

    @Schema(description = "1학년게시판")
    GRADE1("1학년 게시판"),

    @Schema(description = "2학년게시판")
    GRADE2("2학년 게시판"),

    @Schema(description = "3학년게시판")
    GRADE3("3학년 게시판");

    private final String label;

    Category(String label)
    {
        this.label=label;
    }

    public String getLabel(){
        return label;
    }
}
