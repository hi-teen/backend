package backend.hiteen.member.exception.referral;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class ReferralSelfNotAllowedException extends BusinessException {
    public ReferralSelfNotAllowedException() {
        super(ErrorCode.REFERRAL_SELF_NOT_ALLOWED);
    }
}
