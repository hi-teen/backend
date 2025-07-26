package backend.hiteen.message.entity;


import backend.hiteen.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_room_id", nullable = false)
    private MessageRoom messageRoom;

    private Long senderId;
    private Long receiverId;
    private String content;

    @Column(name = "is_read")
    private boolean isRead = false;
}
