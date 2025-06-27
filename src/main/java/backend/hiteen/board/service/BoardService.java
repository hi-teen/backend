package backend.hiteen.board.service;

import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
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

    /**
     * Creates a new board post associated with the specified member.
     *
     * Retrieves the member by email, constructs a new board post with the provided title, content, and disclosure status, saves it, and returns a response DTO representing the created board.
     *
     * @param email the email address of the member creating the board post
     * @param request the request object containing the board's title, content, and disclosure status
     * @return a response DTO representing the newly created board post
     * @throws IllegalArgumentException if the member with the given email does not exist
     */
    @Transactional
    public BoardResponse createBoard(String email,final BoardCreateRequest request){

        Member member=memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board=Board.create(member,request.getTitle(),request.getContent(), request.getDisclosureStatus());
        boardRepository.save(board);

        return new BoardResponse(board);
    }

    //게시글 전체 조회 - 모든 사용자에 대한
    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoards(){
        List<Board> boards =boardRepository.findAll();
        return boards.stream().map(BoardResponse::new).toList();

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
    public List<BoardResponse> getAllMyBoards(String email){

        Member member=memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Board> boards=boardRepository.findAllByMember(member);

        return boards.stream().map(BoardResponse::new).toList();
    }

}
