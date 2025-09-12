package backend.hiteen.member.exception.referral;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class ReferralNotFoundException extends BusinessException {
    public ReferralNotFoundException() {
        super(ErrorCode.REFERRAL_REFERRER_NOT_FOUND);
    }
}
