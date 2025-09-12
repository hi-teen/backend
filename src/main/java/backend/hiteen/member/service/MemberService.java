package backend.hiteen.member.service;

import backend.hiteen.externalapi.school.entity.School;
import backend.hiteen.externalapi.school.exception.SchoolNotFoundException;
import backend.hiteen.externalapi.school.reporitory.SchoolRepository;
import backend.hiteen.global.util.Base62;
import backend.hiteen.member.dto.request.MemberCreateRequest;
import backend.hiteen.member.dto.request.MemberUpdateRequest;
import backend.hiteen.member.dto.response.MemberResponse;
import backend.hiteen.member.dto.response.ReferralListResponse;
import backend.hiteen.member.dto.response.ReferredMemberResponse;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.member.MemberAlreadyExistsException;
import backend.hiteen.member.exception.member.MemberNotFoundException;
import backend.hiteen.member.exception.member.MemberPasswordNotMatchException;
import backend.hiteen.member.exception.referral.ReferralAlreadyAssignedException;
import backend.hiteen.member.exception.referral.ReferralCodeNotFoundException;
import backend.hiteen.member.exception.referral.ReferralSelfNotAllowedException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SchoolRepository schoolRepository;

    //회원가입
    @Transactional
    public MemberResponse signUp(final MemberCreateRequest request) {
        validateDuplicateEmail(request.getEmail());
        validatePassword(request.getPassword(), request.getPasswordConfirm());

        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(SchoolNotFoundException::new);

        Member member = request.toEntity(school, passwordEncoder);

        // 1) 저장해서 ID 먼저 발급
        memberRepository.save(member);

        // 2) 추천코드 생성
        String myReferralCode = Base62.generateReferralCode(member.getId());
        member.setReferralCode(myReferralCode);

        // 3) 추천 코드가 입력되었다면 추천인 매핑
        String inputReferralCode = request.getReferralCode();
        if (inputReferralCode != null && !inputReferralCode.isBlank()) {
            String code = inputReferralCode.trim();

            Member referrer = memberRepository.findByReferralCode(code)
                    .orElseThrow(ReferralCodeNotFoundException::new); // 추천코드 없음

            // 자기 자신 코드 방지
            if (referrer.getId().equals(member.getId())) {
                throw new ReferralSelfNotAllowedException();
            }

            // 이미 추천인 있는 경우 방지(정책: 1회만)
            if (member.getReferredBy() != null) {
                throw new ReferralAlreadyAssignedException();
            }

            member.setReferredBy(referrer);
        }

        return new MemberResponse(member);
    }

    //이메일 중복 확인
    public void checkEmailAvailable(String email){
        validateDuplicateEmail(email);
    }


    //이메일 중복 예외처리
    private void validateDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new MemberAlreadyExistsException();
        }
    }

    //비밀번호 확인 예외처리
    private void validatePassword(String password, String passwordConfirm){
        if(!password.equals(passwordConfirm)){
            throw new MemberPasswordNotMatchException();
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return new MemberResponse(member);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        String newEmail = request.getEmail();
        if (newEmail != null && !newEmail.isBlank()
                && !member.getEmail().equals(newEmail)) {
            validateDuplicateEmail(newEmail);
        }

        String newPw = request.getPassword();
        if (newPw != null && !newPw.isBlank()) {
            validatePassword(newPw, request.getPasswordConfirm());
        }

        member.updateProfile(
                request.getName(),
                request.getNickname(),
                request.getEmail(),
                request.getPassword(),
                request.getPasswordConfirm(),
                request.getGradeNumber(),
                request.getClassNumber(),
                passwordEncoder
        );

        return new MemberResponse(member);
    }

    // 총 회원 수 카운트
    @Transactional(readOnly = true)
    public long countMembers() {
        return memberRepository.count();
    }

    @Transactional(readOnly = true)
    public String getReferralCode(Long memberId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return me.getReferralCode();
    }

    @Transactional(readOnly = true)
    public ReferralListResponse getMyReferredMembers(Long memberId) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<ReferredMemberResponse> members = me.getReferredMembers().stream()
                .map(ReferredMemberResponse::from)
                .toList();

        return new ReferralListResponse(members.size(), members);
    }


    @Transactional(readOnly = true)
    public boolean existsByReferralCode(String code) {
        if (code == null) return false;
        String trimmed = code.trim();
        if (trimmed.isEmpty()) return false;

        return memberRepository.findByReferralCode(trimmed).isPresent();
    }

    @Transactional(readOnly = true)
    public long countMembersBySchool(Long schoolId) {
        schoolRepository.findById(schoolId).orElseThrow(SchoolNotFoundException::new);
        return memberRepository.countBySchool_Id(schoolId);
    }


}
