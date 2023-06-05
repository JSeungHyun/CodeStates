package com.codestates.homework;

import com.codestates.member.controller.MemberController;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import com.codestates.stamp.Stamp;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codestates.util.ApiDocumentUtils.*;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerDocumentationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Autowired
    private Gson gson;

    @Test
    public void getMemberTest() throws Exception {
        // TODO 여기에 MemberController의 getMember() 핸들러 메서드 API 스펙 정보를 포함하는 테스트 케이스를 작성 하세요.
        Member member = new Member("JJangu@gmail.com", "신짱구", "010-1111-1111");
        member.setMemberId(1L);
        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member.setStamp(new Stamp());

        MemberDto.Response response = new MemberDto.Response(
                1L, "JJangu@gmail.com", "신짱구", "010-1111-1111",
                Member.MemberStatus.MEMBER_ACTIVE, new Stamp());

        given(memberService.findMember(Mockito.anyLong())).willReturn(member);
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);


        mockMvc.perform(get("/v11/members/{member-id}/", member.getMemberId()))
                .andExpect(jsonPath("$.data.name").value(member.getName()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()))
                .andExpect(jsonPath("$.data.phone").value(member.getPhone()))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("get-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(parameterWithName("member-id").description("회원 식별자")
                        ),
                        PayloadDocumentation.responseFields(
                                List.of(fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),           // (5)
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("data.memberStatus").type(JsonFieldType.STRING).description("회원 상태: 활동중 / 휴면 상태 / 탈퇴 상태"),
                                        fieldWithPath("data.stamp").type(JsonFieldType.NUMBER).description("스탬프 갯수"))
                        )));


    }

    @Test
    public void getMembersTest() throws Exception {
        // TODO 여기에 MemberController의 getMembers() 핸들러 메서드 API 스펙 정보를 포함하는 테스트 케이스를 작성 하세요.
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
        List<MemberDto.Response> responses = Arrays.asList(
                new MemberDto.Response(1L, "hgd1@gmail.com", "홍길동", "010-1111-1111", Member.MemberStatus.MEMBER_ACTIVE, new Stamp()),
                new MemberDto.Response(2L, "JJanggu@gmail.com", "신짱구", "010-2222-2222", Member.MemberStatus.MEMBER_ACTIVE, new Stamp())
        );

        given(memberService.findMembers(Mockito.anyInt(), Mockito.anyInt())).willReturn(page);
        given(mapper.membersToMemberResponses(Mockito.anyList())).willReturn(responses);


        mockMvc.perform(
                        get("/v11/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(1))
                                .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].email", hasItems(members.stream().map(Member::getEmail).toArray())))
                .andExpect(jsonPath("$.data[*].name", hasItems(members.stream().map(Member::getName).toArray())))
                .andExpect(jsonPath("$.data[*].phone", hasItems(members.stream().map(Member::getPhone).toArray())))
                .andDo(MockMvcRestDocumentation.document("get-members",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 당 회원 수")
                                )
                        ),
                        PayloadDocumentation.responseFields(
                                List.of(fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.[].email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data.[].phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("data.[].memberStatus").type(JsonFieldType.STRING).description("회원 상태: 활동중 / 휴면 상태 / 탈퇴 상태"),
                                        fieldWithPath("data.[].stamp").type(JsonFieldType.NUMBER).description("스탬프 갯수"),
                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 당 회원 수"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 회원 수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"))
                        )));
    }

    @Test
    public void deleteMemberTest() throws Exception {
        // TODO 여기에 MemberController의 deleteMember() 핸들러 메서드 API 스펙 정보를 포함하는 테스트 케이스를 작성 하세요.
        long memberId = 1L;
        doNothing().when(memberService).deleteMember(Mockito.anyLong());

        mockMvc.perform(delete("/v11/members/{member-id}", memberId))
                .andExpect(status().isNoContent())
                .andDo(
                        MockMvcRestDocumentation.document("delete-member",
                                getRequestPreProcessor(),
                                getResponsePreProcessor(),
                                pathParameters(parameterWithName("member-id").description("회원 식별자"))
                        )
                );


    }
}
