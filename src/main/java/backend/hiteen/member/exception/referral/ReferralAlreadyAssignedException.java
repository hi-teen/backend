package backend.hiteen.member.exception.referral;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class ReferralAlreadyAssignedException extends BusinessException {
    public ReferralAlreadyAssignedException() {
        super(ErrorCode.REFERRAL_ALREADY_ASSIGNED);
    }
}
