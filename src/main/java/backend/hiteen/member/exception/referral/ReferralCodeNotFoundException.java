package backend.hiteen.member.exception.referral;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class ReferralCodeNotFoundException extends BusinessException {
    public ReferralCodeNotFoundException() {
        super(ErrorCode.REFERRAL_CODE_NOT_FOUND);
    }
}
