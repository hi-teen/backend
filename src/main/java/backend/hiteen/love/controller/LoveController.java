package backend.hiteen.love.controller;

import backend.hiteen.love.service.LoveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "좋아요", description = "Love")
public class LoveController {

    private final LoveService loveService;

    @PostMapping("/member/{memberId}/board/{boardId}/love")
    public ResponseEntity<String> loveBoard(@PathVariable Long memberId, @PathVariable Long boardId){
        String message=loveService.updateLoveBoard(memberId,boardId);
        return ResponseEntity.ok(message);

    }
}
