package backend.hiteen.scrap.service;

import backend.hiteen.board.entity.Board;
import backend.hiteen.board.exception.BoardNotFoundException;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import backend.hiteen.scrap.dto.ScrapBoardResponse;
import backend.hiteen.scrap.entity.Scrap;
import backend.hiteen.scrap.entity.ScrapActionResult;
import backend.hiteen.scrap.exception.ScrapNotFoundException;
import backend.hiteen.scrap.exception.ScrapNotOwnerException;
import backend.hiteen.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ScrapRepository scrapRepository;

    //스크랩 추가/취소
    @Transactional
    public ScrapActionResult updateScrapBoard(String email, Long boardId){
        Member member=memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Board board=boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        validateSameSchool(member, board.getMember());

        if (!isBoardScrapped(member, board)){
            board.increaseScrapCount();
            createScrap(member,board);
            return ScrapActionResult.CREATED;
        }
        board.decreaseScrapCount();
        deleteScrap(member,board);
        return ScrapActionResult.DELETED;
    }

    //내가 스크랩 한 게시글 조회
    @Transactional(readOnly = true)
    public List<ScrapBoardResponse> getMyScrapedBoards(String email){

        Member member=memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        List<Board> boards=scrapRepository.findScrapedBoardsByMemberId(member.getId());

        return boards.stream().map(ScrapBoardResponse::new).toList();
    }

    private boolean isBoardScrapped(Member member, Board board){
        return scrapRepository.findByMemberAndBoard(member,board).isPresent();
    }

    private void createScrap(Member member, Board board){
        Scrap scrap=Scrap.create(member,board);
        scrapRepository.save(scrap);
    }

    private void deleteScrap(Member member, Board board){
        Scrap scrap=scrapRepository.findByMemberAndBoard(member, board)
                .orElseThrow(ScrapNotFoundException::new);
        scrapRepository.delete(scrap);
    }


    private void validateSameSchool(Member a, Member b) {
        if (!a.getSchool().getId().equals(b.getSchool().getId())) {
            throw new ScrapNotOwnerException();
        }
    }

}
