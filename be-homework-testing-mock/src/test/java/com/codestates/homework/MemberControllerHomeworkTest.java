package com.codestates.homework;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import com.codestates.stamp.Stamp;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerHomeworkTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MemberMapper mapper;

    @Test
    @DisplayName("맴버 수정 테스트")
    void patchMemberTest() throws Exception {
        // TODO MemberController의 patchMember() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // TODO Mockito를 사용해야 합니다. ^^
        // given
        MemberDto.Patch patch = new MemberDto.Patch(1L,"신짱구","010-1111-2222", Member.MemberStatus.MEMBER_ACTIVE);
        Member patchMember = mapper.memberPatchToMember(patch);
        patchMember.setStamp(new Stamp());

        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(patchMember);
        String content = gson.toJson(patch);

        mockMvc.perform(
                patch("/v11/members/" + patchMember.getMemberId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(jsonPath("$.data.name").value(patch.getName()))
                .andExpect(jsonPath("$.data.phone").value(patch.getPhone()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getMember 테스트")
    void getMemberTest() throws Exception {
        // TODO MemberController의 getMember() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // TODO Mockito를 사용해야 합니다. ^^
        Member member = new Member("JJangu@gmail.com", "신짱구", "010-1111-2222");
        member.setMemberId(1L);
        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member.setStamp(new Stamp());

        given(memberService.findMember(Mockito.anyLong())).willReturn(member);
        mockMvc.perform(get("/v11/members/" + member.getMemberId()))
                .andExpect(jsonPath("$.data.name").value(member.getName()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()))
                .andExpect(jsonPath("$.data.phone").value(member.getPhone()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("get Members 테스트")
    void getMembersTest() throws Exception {
        // TODO MemberController의 getMembers() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // TODO Mockito를 사용해야 합니다. ^^
        Member member1 = new Member("hgd1@gmail.com", "홍길동", "010-1111-1111");
        Member member2 = new Member("JJanggu@gmail.com", "신짱구", "010-2222-2222");

        member1.setMemberId(1L);
        member1.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member1.setStamp(new Stamp());

        member2.setMemberId(2L);
        member2.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member2.setStamp(new Stamp());

        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        Page<Member> page = new PageImpl<>(members);

        given(memberService.findMembers(Mockito.anyInt(), Mockito.anyInt())).willReturn(page);

        mockMvc.perform(
                        get("/v11/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(1))
                                .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].email", hasItems(members.stream().map(Member::getEmail).toArray())))
                .andExpect(jsonPath("$.data[*].name", hasItems(members.stream().map(Member::getName).toArray())))
                .andExpect(jsonPath("$.data[*].phone", hasItems(members.stream().map(Member::getPhone).toArray())));
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteMemberTest() throws Exception {
        // TODO MemberController의 deleteMember() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // TODO Mockito를 사용해야 합니다. ^^
        Long memberId = 1L;
        doNothing().when(memberService).deleteMember(memberId);
        ResultActions actions = mockMvc.perform(delete("/v11/members/" + memberId));
        actions.andExpect(status().isNoContent());
    }

}
