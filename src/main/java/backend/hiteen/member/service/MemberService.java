package backend.hiteen.member.service;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.externalapi.school.entity.School;
import backend.hiteen.externalapi.school.exception.SchoolNotFoundException;
import backend.hiteen.externalapi.school.reporitory.SchoolRepository;
import backend.hiteen.global.exception.BusinessException;
import backend.hiteen.member.dto.request.MemberCreateRequest;
import backend.hiteen.member.dto.request.MemberUpdateRequest;
import backend.hiteen.member.dto.response.MemberResponse;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        memberRepository.save(member);
        return new MemberResponse(member);
    }


    //이메일 중복 예외처리
    private void validateDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }
    }


    //비밀번호 확인 예외처리
    private void validatePassword(String password, String passwordConfirm){
        if(!password.equals(passwordConfirm)){
            throw new BusinessException(ErrorCode.MEMBER_PASSWORD_NOT_MATCH);
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

        String newNick = request.getNickname();
        if (newNick != null && !member.getNickname().equals(newNick)) {
            validateDuplicateNickname(newNick);
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
}
