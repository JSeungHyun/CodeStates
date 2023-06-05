package com.codestates.homework;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.dto.MemberPatchDto;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.common.NormalizedString.toArray;
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

    private final MemberRepository memberRepository;

    @Autowired
    public MemberControllerHomeworkTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    @DisplayName("멤버 등록 테스트")
    void postMemberTest() throws Exception {
        // given
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com",
                "홍길동",
                "010-1234-5678");
        String content = gson.toJson(post);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/v11/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))));
    }

    @Test
    @DisplayName("멤버 수정 테스트")
    void patchMemberTest() throws Exception {
        // TODO MemberController의 patchMember() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);
        ResultActions postActions = mockMvc.perform(
                        post("/v11/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        String location = postActions.andReturn().getResponse().getHeader("Location"); // URL = "/v11/members/1"
        long memberId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        MemberDto.Patch patch = new MemberDto.Patch("신짱구", "010-1234-1234");
        patch.setMemberId(memberId);
        String patchContent = gson.toJson(patch);

        mockMvc.perform(
                patch("/v11/members/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(patch.getName()))
                .andExpect(jsonPath("$.data.phone").value(patch.getPhone()));


    }

    @Test
    void getMemberTest() throws Exception {
        // given: MemberController의 getMember()를 테스트하기 위해서 postMember()를 이용해 테스트 데이터를 생성 후, DB에 저장
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(
                        post("/v11/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );
        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v11/members/1"
        // when / then
        mockMvc.perform(
                        get(location)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(post.getEmail()))
                .andExpect(jsonPath("$.data.name").value(post.getName()))
                .andExpect(jsonPath("$.data.phone").value(post.getPhone()));
    }

    @Test
    void getMembersTest() throws Exception {
        // TODO MemberController의 getMembers() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // given
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);
        ResultActions postActions = mockMvc.perform(
                post("/v11/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postContent)
        );
        MemberDto.Post post2 = new MemberDto.Post("JJangu@gmail.com","신짱구","010-1234-5678");
        String postContent2 = gson.toJson(post2);
        ResultActions postActions2 = mockMvc.perform(
                post("/v11/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postContent2)
        );
        MemberDto.Post post3 = new MemberDto.Post("kimcoding@gmail.com","김코딩","010-0000-0000");
        String postContent3 = gson.toJson(post3);
        ResultActions postActions3 = mockMvc.perform(
                post("/v11/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postContent3)
        );

        List<Member> members = memberRepository.findAll(PageRequest.of(0, 10, Sort.by("memberId").descending())).getContent();

        mockMvc.perform(get("/v11/members")
                .accept(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].email", hasItems(members.stream().map(Member::getEmail).toArray())))
                .andExpect(jsonPath("$.data[*].name", hasItems(members.stream().map(Member::getName).toArray())))
                .andExpect(jsonPath("$.data[*].phone", hasItems(members.stream().map(Member::getPhone).toArray())));


    }

    @Test
    void deleteMemberTest() throws Exception {
        // TODO MemberController의 deleteMember() 핸들러 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // given
        MemberDto.Post post= new MemberDto.Post("JJangu@gmail.com","신짱구","010-1234-5678");
        String postContent = gson.toJson(post);
        ResultActions postActions = mockMvc.perform(
                post("/v11/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postContent)
        );

        // when
        String location = postActions.andReturn().getResponse().getHeader("Location"); // URL = "/v11/members/1"

        // then
        mockMvc.perform(
                delete(location)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent());
    }
}
