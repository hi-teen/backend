package backend.hiteen.global.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class RestPage<T> extends PageImpl<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public RestPage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public RestPage(List<T> content) {
        super(content);
    }
}
