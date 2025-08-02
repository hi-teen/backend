package backend.hiteen.love.service;

import backend.hiteen.board.entity.Board;
import backend.hiteen.board.exception.BoardNotFoundException;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.love.dto.LoveBoardResponse;
import backend.hiteen.love.entity.Love;
import backend.hiteen.love.entity.LoveActionResult;
import backend.hiteen.love.exception.LoveNotFoundException;
import backend.hiteen.love.exception.LoveNotOwnerException;
import backend.hiteen.love.repository.LoveRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoveService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;

    // 좋아요 기능
    @Transactional
    public LoveActionResult updateLoveBoard(String email, Long boardId){

        Board board=boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        Member member=memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        validateSameSchool(member, board.getMember());

        if(!isBoardLoved(member,board)){
            board.increaseLoveCount();
            createLove(member,board);
            return LoveActionResult.CREATED;
        }
         board.decreaseLoveCount();
        deleteLove(member,board);
        return LoveActionResult.DELETED;
    }


    @Transactional(readOnly = true)
    public List<LoveBoardResponse> getMyLovedBoards(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        List<Board> boards=loveRepository.findLovedBoardsByMemberId(member.getId());

        return boards.stream().map(LoveBoardResponse::new).toList();
    }

    private boolean isBoardLoved(Member member, Board board){
        return loveRepository.findByMemberAndBoard(member,board).isPresent();
    }

    private void createLove(Member member, Board board){
        Love love=new Love(member,board);
        loveRepository.save(love);
    }

    private void deleteLove(Member member, Board board){
        Love love=loveRepository.findByMemberAndBoard(member,board)
                .orElseThrow(LoveNotFoundException::new);
        loveRepository.delete(love);
    }

    private void validateSameSchool(Member a, Member b) {
        if (!a.getSchool().getId().equals(b.getSchool().getId())) {
            throw new LoveNotOwnerException();
        }
    }


}
