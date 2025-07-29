package backend.hiteen.message.repository;


import backend.hiteen.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    @Query("""
                select mr
                from MessageRoom mr
                where mr.board.id = :boardId
                and ((mr.senderId = :id1 and mr.receiverId = :id2)
                or (mr.senderId = :id2 and mr.receiverId = :id1))
                and ((:anonNum is null and mr.anonymousNumber is null)
                or ( :anonNum is not null and mr.anonymousNumber = :anonNum))
            """)
    Optional<MessageRoom> findByBoardAndParticipantsAndAnon(
            @Param("boardId") Long boardId,
            @Param("id1") Long id1,
            @Param("id2") Long id2,
            @Param("anonNum") Integer anonymousNumber
    );


    @Query("SELECT mr " +
            "FROM MessageRoom mr " +
            "WHERE mr.senderId = :memberId OR mr.receiverId = :memberId " +
            "ORDER BY mr.updatedAt DESC")
    List<MessageRoom> findAllByMemberOrderByUpdatedAtDesc(@Param("memberId") Long memberId);
}
