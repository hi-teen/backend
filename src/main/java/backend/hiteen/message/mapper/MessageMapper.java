package backend.hiteen.message.mapper;

import backend.hiteen.board.entity.Board;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.message.dto.response.MessageResponse;
import backend.hiteen.message.entity.Message;
import backend.hiteen.message.entity.MessageRoom;

import java.util.Optional;

public class MessageMapper {
    private MessageMapper() {
    }

    // 작성자라면 작성자 댓글이면 익명번호 기반으로 조회
    public static String computeDisplayName(Message message, CommentRepository commentRepository) {
        Board board = message.getMessageRoom().getBoard();
        Long boardOwnerId = board.getMember().getId();

        if (message.getSenderId().equals(boardOwnerId)) {
            return "작성자";
        } else {
            Optional<Comment> commentOptional =
                    commentRepository.findByBoardIdAndMemberId(board.getId(), message.getSenderId());
            return commentOptional.map(comment -> "익명 " + comment.getAnonymousNumber())
                    .orElse("익명");
        }
    }

    public static String computeDisplayName(MessageRoom room, Long memberId, CommentRepository commentRepository) {
        Board board = room.getBoard();
        Long boardOwnerId = board.getMember().getId();

        if (memberId.equals(boardOwnerId)) {
            return "작성자";
        } else {
            Optional<Comment> commentOptional =
                    commentRepository.findByBoardIdAndMemberId(board.getId(), memberId);
            return commentOptional.map(comment -> "익명 " + comment.getAnonymousNumber())
                    .orElse("익명");
        }
    }

    public static MessageResponse toDto(Message message, Long memberId, CommentRepository commentRepository) {
        return new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getContent(),
                message.getCreatedAt(),
                computeDisplayName(message, commentRepository),
                message.getSenderId().equals(memberId)
        );
    }
}