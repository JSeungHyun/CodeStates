package com.codestates.homework;

import com.codestates.helper.RandomPasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class RandomPasswordGeneratorTest {
    @DisplayName("실습 3: 랜덤 패스워드 생성 테스트")
    @Test
    public void generateTest() {
        // TODO 여기에 테스트 케이스를 작성해주세요.
        // given
        RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
        int numberOfUpperCaseLetters = 2;
        int numberOfLowerCaseLetters = 5;
        int numberOfNumeric = 2;
        int numberOfSpecialChars = 1;

        // when
        String password = randomPasswordGenerator.generate(numberOfUpperCaseLetters, numberOfLowerCaseLetters, numberOfNumeric, numberOfSpecialChars);
        int expectedTotalLength = 10;
        int actualNumberOfUpperCaseLetters = 0;
        int actualNumberOfLowerCaseLetters = 0;
        int actualNumberOfNumeric = 0;
        int actualNumberOfSpecialChars = 0;
        for (int i=0; i < password.length(); i++){
            char c = password.charAt(i);
            if ( Character.isUpperCase(c))
                actualNumberOfUpperCaseLetters++;
            else if ( Character.isLowerCase(c))
                actualNumberOfLowerCaseLetters++;
            else if ( Character.isDigit(c))
                actualNumberOfNumeric++;
            else
                actualNumberOfSpecialChars++;
        }


        // then
        System.out.println(password);
        assertEquals(expectedTotalLength, password.length()); // 총 문자의 길이
        assertEquals(numberOfUpperCaseLetters, actualNumberOfUpperCaseLetters); // 대문자의 개수
        assertEquals(numberOfLowerCaseLetters, actualNumberOfLowerCaseLetters); // 소문자의 개수
        assertEquals(numberOfNumeric, actualNumberOfNumeric); // 숫자의 개수
        assertEquals(numberOfSpecialChars, actualNumberOfSpecialChars);
    }
}
