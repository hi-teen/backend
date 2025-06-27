package backend.hiteen.board.entity;

import backend.hiteen.global.entity.BaseTimeEntity;
import backend.hiteen.love.entity.Love;
import backend.hiteen.member.entity.Member;
import backend.hiteen.scrap.entity.Scrap;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisclosureStatus disclosureStatus;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private int loveCount;

    @Column
    private int scrapCount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Love> loves;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps;


    private Board(Member member, String title, String content, DisclosureStatus disclosureStatus){
        this.member=member;
        this.title=title;
        this.content=content;
        this.loveCount=0;
        this.scrapCount=0;
        this.disclosureStatus=disclosureStatus;
    }

    public static Board create(Member member, String title, String content, DisclosureStatus disclosureStatus){
        return new Board(member,title,content,disclosureStatus);
    }

    public String getDisplayWriterName(){
        return this.disclosureStatus==DisclosureStatus.PUBLIC
                ? this.member.getName()
                :"익명";
    }

    public void increaseLoveCount(){
        this.loveCount+=1;
    }

    public void decreaseLoveCount(){
        this.loveCount-=1;
    }

    public void increaseScrapCount(){
        this.scrapCount+=1;
    }

    public void decreaseScrapCount(){
        this.scrapCount-=1;
    }

}