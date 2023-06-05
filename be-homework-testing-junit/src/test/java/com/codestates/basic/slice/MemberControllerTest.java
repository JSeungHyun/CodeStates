package com.codestates.basic.slice;

import com.codestates.member.dto.MemberDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("멤버 등록")
    void postMemberTest() throws Exception{

        // given
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com", "홍길동", "010-1234-5678");
        String content = gson.toJson(post); // post를 json으로 변환

        // when
        ResultActions actions = mockMvc.perform( // Controller 핸들러 메서드에 요청 전송
                post("/v11/members") // request URL 설정
                        .accept(MediaType.APPLICATION_JSON) // 클라이언트 쪽에서 리턴 받을 응답 데이터
                        .contentType(MediaType.APPLICATION_JSON) // 서버 쪽에서 처리 가능한 Type
                        .content(content));

        // then
        actions
                .andExpect(status().isCreated()) // 201인지 확인
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))));

    }

    @Test
    @DisplayName("get 테스트")
    void getMemberTest() throws Exception {
        // postMember()를 이용한 테스트 데이터 생성 시작
        // given
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(post("/v11/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // "/v11/members/1"
        String location = postActions.andReturn().getResponse().getHeader("Location");

        // when, then
        mockMvc.perform(get(location).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())    // (4)
                .andExpect(jsonPath("$.data.email").value(post.getEmail()))   // (5)
                .andExpect(jsonPath("$.data.name").value(post.getName()))     // (6)
                .andExpect(jsonPath("$.data.phone").value(post.getPhone()));  // (7)

    }
}
