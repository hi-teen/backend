package backend.hiteen.board.service;

import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 게시글 작성
    @Transactional
    public BoardResponse createBoard(String email, final BoardCreateRequest request) {

        Member member=memberRepository.findByEmail(email).orElseThrow(()->new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Board board = Board.create(member,
                                   request.getTitle(),
                                   request.getContent(),
                                   request.getDisclosureStatus(),
                                   request.getCategory());
        boardRepository.save(board);

        return new BoardResponse(board);
    }

    //게시글 전체 조회 - 모든 사용자에 대한
    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(BoardResponse::new).toList();

    }

    //게시글 단일 조회 - 모든 사용자에 대한
    @Transactional
    public BoardResponse getBoardById(Long boardId){
        Board board=boardRepository.findById(boardId)
                .orElseThrow(()->new BusinessException(ErrorCode.BOARD_NOT_FOUND));
        board.increaseViewCount();
        return new BoardResponse(board);
    }

    //게시글 전체 조회 - 내가 작성한
    @Transactional(readOnly = true)
    public List<BoardResponse> getAllMyBoards(String email) {

        Member member=memberRepository.findByEmail(email).orElseThrow(()->new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<Board> boards = boardRepository.findAllByMember(member);

        return boards.stream().map(BoardResponse::new).toList();
    }

    //인기게시글 조회
    @Transactional
    public List<BoardResponse> getPopularBoards(){
        List<Board> popularBoards=boardRepository.findPopularBoards();
        return popularBoards.stream().map(BoardResponse::new).toList();
    }


    //게시글 검색
    @Transactional
    public List<BoardResponse> searchBoards(String keyword){
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.KEYWORD_REQUIRED);
        }

        List<Board> boards=boardRepository.searchByKeyword(keyword);
        return boards.stream().map(BoardResponse::new).toList();
    }

    //게시글 삭제
    public void deleteBoard(String email, Long boardId){
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Board board=boardRepository.findById(boardId).orElseThrow(()->new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        if(!board.getMember().equals(member)){
            throw new BusinessException(ErrorCode.NO_PERMISSION_TO_DELETE_BOARD);
        }

        boardRepository.delete(board);
    }

}
