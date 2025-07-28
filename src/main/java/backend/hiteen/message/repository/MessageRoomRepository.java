package backend.hiteen.message.repository;


import backend.hiteen.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    @Query("""
            select mr from MessageRoom mr
            where mr.board.id = :boardId
            and ((mr.senderId = :id1 and mr.receiverId = :id2)
            or (mr.senderId = :id2 and mr.receiverId = :id1))
            """)
    Optional<MessageRoom> findByBoardIdAndParticipants(
            @Param("boardId") Long boardId,
            @Param("id1") Long id1,
            @Param("id2") Long id2);

    @Query("""
                    select mr
                    from MessageRoom mr
                    where mr.senderId = :memberId or mr.receiverId = :memberId
                    order by (select max(m.createdAt) from Message m where m.messageRoom = mr) desc
            """)
    List<MessageRoom> findAllByMember(@Param("memberId") Long memberId);

    @Query("SELECT mr " +
            "FROM MessageRoom mr " +
            "WHERE mr.senderId = :memberId OR mr.receiverId = :memberId " +
            "ORDER BY mr.updatedAt DESC")
    List<MessageRoom> findAllByMemberOrderByUpdatedAtDesc(@Param("memberId") Long memberId);
}
