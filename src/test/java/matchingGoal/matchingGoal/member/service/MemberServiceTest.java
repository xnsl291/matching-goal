package matchingGoal.matchingGoal.member.service;

import matchingGoal.matchingGoal.member.exception.InvalidPasswordFormatException;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    String validPw = "password123!!";
    String invalidPw1 = "password!!!!";  // no digit
    String invalidPw2 = "pass123";  //len < 10
    String invalidPw3 = "123456!!!!";  // no alpha

    MemberRegisterDto invalid_registerDto_1 = MemberRegisterDto.builder()
            .email("test_00@gmail.com")
            .name("test111")
            .password(invalidPw1)
            .nickname("test5 FC")
            .introduction("hello, there")
            .region("seoul")
            .build();

    MemberRegisterDto invalid_registerDto_2 = MemberRegisterDto.builder()
            .email("test_11@gmail.com")
            .name("test111")
            .password(invalidPw2)
            .nickname("test3 FC")
            .introduction("hello, there")
            .region("seoul")
            .build();
    MemberRegisterDto invalid_registerDto_3 = MemberRegisterDto.builder()
            .email("test_22@gmail.com")
            .name("test111")
            .password(invalidPw3)
            .nickname("test2 FC")
            .introduction("hello, there")
            .region("seoul")
            .build();
    MemberRegisterDto valid_registerDto = MemberRegisterDto.builder()
            .email("test111@gmail.com")
            .name("test111")
            .password(validPw)
            .nickname("test FC")
            .introduction("hello, there")
            .region("seoul")
            .build();

    @Test
    void register(){
        memberService.registerMember(valid_registerDto);

        System.out.println(invalid_registerDto_1.getPassword()+": ");
        Assertions.assertThrows(InvalidPasswordFormatException.class, () -> memberService.registerMember(invalid_registerDto_1));

        System.out.println(invalid_registerDto_2.getPassword()+": ");
        Assertions.assertThrows(InvalidPasswordFormatException.class, () -> memberService.registerMember(invalid_registerDto_2));

        System.out.println(invalid_registerDto_3.getPassword()+": ");
        Assertions.assertThrows(InvalidPasswordFormatException.class, () -> memberService.registerMember(invalid_registerDto_3));
    }

    @Test
    void checknickname(){
        String name1 = "test FC";
        String name2 = "new FC";
        System.out.println(name1 +" : " + memberService.checkNickname(name1));
        System.out.println(name2 + " : " + memberService.checkNickname(name2));
    }
}