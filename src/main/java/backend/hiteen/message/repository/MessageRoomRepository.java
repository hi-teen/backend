package backend.hiteen.message.repository;


import backend.hiteen.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
    Optional<MessageRoom> findMessageRoomByBoardIdAndSenderIdAndReceiverId(Long boardId, Long senderId, Long receiverId);
}
