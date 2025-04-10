package backend.hiteen.member.service;

import backend.hiteen.member.dto.request.MemberCreateRequest;
import backend.hiteen.member.dto.response.MemberResponse;
import backend.hiteen.member.entity.Member;
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

    //회원가입
    @Transactional
    public MemberResponse signUp(final MemberCreateRequest request) {
        validateDuplicateEmail(request.getEmail());
        validateDuplicateNickname(request.getNickname());
        validatePassword(request.getPassword(), request.getPasswordConfirm());
        Member member=request.toEntity(passwordEncoder);
        memberRepository.save(member);
        return new MemberResponse(member);
    }

    //이메일 중복 예외처리
    private void validateDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    //닉네임 중복 예외처리
    private void validateDuplicateNickname(String nickName){
        if(memberRepository.existsByNickname(nickName)){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    //비밀번호 확인 예외처리
    private void validatePassword(String password, String passwordConfirm){
        if(!password.equals(passwordConfirm)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
