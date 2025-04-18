package backend.hiteen.scrap.service;

import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import backend.hiteen.scrap.dto.ScrapBoardResponse;
import backend.hiteen.scrap.entity.Scrap;
import backend.hiteen.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ScrapRepository scrapRepository;

    //스크랩 추가/취소
    @Transactional
    public String updateScrapBoard(Long memberId, Long boardId){
        Member member=memberRepository.findById(memberId).orElse(null);

        Board board=boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!hasScrapBoard(member, board)){
            board.increaseScrapCount();
            return createScrap(member,board);
        }
        board.decreaseScrapCount();
        return deleteScrap(member,board);
    }

    //내가 스크랩 한 게시글 조회
    @Transactional(readOnly = true)
    public List<ScrapBoardResponse> getMyScrapedBoards(Long memebrId){
        List<Board> boards=scrapRepository.findScrapedBoardsByMemberId(memebrId);
        return boards.stream().map(ScrapBoardResponse::new).collect(Collectors.toList());
    }

    //FIXME: boolean 메서드는 의문형 스타일로 많이 작성하는편임. 스크랩 여부 확인인데 has를 쓰니깐 가지고있다? 라고 읽힘
    private boolean hasScrapBoard(Member member, Board board){
        return scrapRepository.findByMemberAndBoard(member,board).isPresent();
    }

    private String createScrap(Member member, Board board){
        Scrap scrap=Scrap.create(member,board);
        scrapRepository.save(scrap);
        return "스크랩이 되었습니다.";
    }

    private String deleteScrap(Member member, Board board){
        Scrap scrap=scrapRepository.findByMemberAndBoard(member, board)
                .orElseThrow(()-> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        scrapRepository.delete(scrap);
        return "스크랩이 취소되었습니다.";
    }
}
