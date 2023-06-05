package com.codestates.mock;

import com.codestates.exception.BusinessLogicException;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceMockTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("Mock 멤버 생성 테스트")
    public void createMemberTest(){
        // given
        Member member = new Member("hgd@gmail.com", "홍길동", "010-1234-1234");
        BDDMockito.given(memberRepository.findByEmail(Mockito.anyString()))
                .willReturn(Optional.of(member));

        // when, then
        Assertions.assertThrows(BusinessLogicException.class, () -> memberService.createMember(member));
    }
}
