package backend.hiteen.board.service;

import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 게시글 작성
    @Transactional
    public BoardResponse createBoard(final BoardCreateRequest request){
        Member member=memberRepository.findById(request.getMemberId())
                .orElse(null);
//        Throw(()->new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board=Board.create(member,request.getTitle(),request.getContent());
        boardRepository.save(board);
        return new BoardResponse(board);
    }

    //게시글 전체 조회 - 모든 사용자에 대한
    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoards(){
        List<Board> boards =boardRepository.findAll();
        return boards.stream().map(BoardResponse::new)
                .collect(Collectors.toList());

    }

    //게시글 단일 조회 - 모든 사용자에 대한
    @Transactional(readOnly=true)
    public BoardResponse getBoardById(Long boardId){
        Board board=boardRepository.findById(boardId)
                .orElseThrow(()->new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return new BoardResponse(board);
    }

    //게시글 전체 조회 - 내가 작성한
    @Transactional(readOnly = true)
    public List<BoardResponse> getMyBoards(Long memberId){
        List<Board> boards=boardRepository.findAllByMemberId(memberId);
        return boards.stream().map(BoardResponse::new).collect(Collectors.toList());
    }

    //게시글 단일 조회 - 내가 작성한
    @Transactional(readOnly = true)
    public BoardResponse getMyBoardDetail(Long memberId, Long boardId){
        Board board=boardRepository.findByMemberIdAndId(memberId, boardId)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다."));
        return new BoardResponse(board);
    }

    //내가 좋아요 한 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getMyLovedBoard(Long memberId){
        List<Board> boards=boardRepository.findLikedBoardByMemberId(memberId);
        return boards.stream().map(BoardResponse::new).collect(Collectors.toList());
    }

    //내가 스크랩한 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getMyScrapedBoard(Long memberId){
        List<Board> boards=boardRepository.findScrapedBoardByMemberId(memberId);
        return boards.stream().map(BoardResponse::new).collect(Collectors.toList());
    }

}
