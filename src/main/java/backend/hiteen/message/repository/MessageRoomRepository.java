package backend.hiteen.message.repository;


import backend.hiteen.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    @Query("select mr from MessageRoom mr " +
            "where mr.board.id = :boardId " +
            "and ((mr.senderId = :id1 and mr.receiverId = :id2) " +
            "or (mr.senderId = :id2 and mr.receiverId = :id1))")
    Optional<MessageRoom> findByBoardIdAndParticipants(
            @Param("boardId") Long boardId,
            @Param("id1") Long id1,
            @Param("id2") Long id2);
}
