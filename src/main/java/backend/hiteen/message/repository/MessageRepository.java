package backend.hiteen.message.repository;

import backend.hiteen.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMessageRoomIdOrderByCreatedAtAsc(Long messageRoomId);

    Optional<Message> findTopByMessageRoomIdOrderByCreatedAtDesc(Long messageRoomId);

    int countByMessageRoomIdAndReceiverIdAndIsReadFalse(Long roomId, Long memberId);
    List<Message> findByMessageRoomIdAndReceiverIdAndIsReadFalse(Long roomId, Long receiverId);

}

