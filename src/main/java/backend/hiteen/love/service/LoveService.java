package backend.hiteen.love.service;

import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.love.entity.Love;
import backend.hiteen.love.repository.LoveRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
