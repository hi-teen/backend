package backend.hiteen.love.service;

import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.love.dto.LoveBoardResponse;
import backend.hiteen.love.entity.Love;
import backend.hiteen.love.repository.LoveRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoveService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;

    // 좋아요 기능
    @Transactional
    public String updateLoveBoard(Long memberId, Long boardId){
        Board board=boardRepository.findById(boardId)
                .orElseThrow(()->new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Member member=memberRepository.findById(memberId).orElse(null);

        if(!hasLoveBoard(member,board)){
            board.increaseLoveCount();
            return createLove(member,board);
        }
         board.decreaseLoveCount();
        return deleteLove(member,board);
    }


    //FIXME: 자바17은 .toList() 사용 가능하니 가독성을 위해 수정.
    @Transactional(readOnly = true)
    public List<LoveBoardResponse> getMyLovedBoards(Long memberId){
        List<Board> boards=loveRepository.findLovedBoardsByMemberId(memberId);
        return boards.stream().map(LoveBoardResponse::new).collect(Collectors.toList());
    }

    //FIXME: boolean 메서드는 의문형 스타일로 많이 작성하는편임. 좋아요 여부 확인인데 has를 쓰니깐 가지고있다? 라고 읽힘
    private boolean hasLoveBoard(Member member, Board board){
        return loveRepository.findByMemberAndBoard(member,board).isPresent();
    }

    private String createLove(Member member, Board board){
        Love love=new Love(member,board);
        loveRepository.save(love);
        return "좋아요를 눌렀습니다.";
    }

    private String deleteLove(Member member, Board board){
        Love love=loveRepository.findByMemberAndBoard(member,board)
                .orElseThrow(()-> new IllegalArgumentException("좋아요가 없습니다."));
        loveRepository.delete(love);
        return "좋아요를 취소했습니다.";
    }


}
