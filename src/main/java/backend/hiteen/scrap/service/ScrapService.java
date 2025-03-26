package backend.hiteen.scrap.service;

import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import backend.hiteen.scrap.entity.Scrap;
import backend.hiteen.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
