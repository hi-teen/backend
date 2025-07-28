package backend.hiteen.message.repository;

import backend.hiteen.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMessageRoomIdOrderByCreatedAtAsc(Long messageRoomId);

    Optional<Message> findTopByMessageRoomIdOrderByCreatedAtDesc(Long messageRoomId);

    int countByMessageRoomIdAndReceiverIdAndIsReadFalse(Long roomId, Long memberId);

    @Modifying
    @Query("UPDATE Message m " +
            "SET m.isRead = true " +
            "WHERE m.messageRoom.id = :roomId " +
            "  AND m.receiverId = :memberId " +
            "  AND m.isRead = false")
    int markAllAsRead(@Param("roomId") Long roomId,
                      @Param("memberId") Long memberId);

}

