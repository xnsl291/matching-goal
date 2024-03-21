package matchingGoal.matchingGoal.member.service;

import matchingGoal.matchingGoal.member.dto.SignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;

    String validPw = "password123!!";
    String invalidPw1 = "password!!!!";  // no digit
    String invalidPw2 = "pass123";  //len < 10
    String invalidPw3 = "123456!!!!";  // no alpha

    SignUpDto invalid_registerDto_1 = SignUpDto.builder()
            .email("test_00@gmail.com")
            .name("test111")
            .password(invalidPw1)
            .nickname("test5 FC")
            .introduction("hello, there")
            .region("seoul")
            .build();

    SignUpDto invalid_registerDto_2 = SignUpDto.builder()
            .email("test_11@gmail.com")
            .name("test111")
            .password(invalidPw2)
            .nickname("test3 FC")
            .introduction("hello, there")
            .region("seoul")
            .build();
    SignUpDto invalid_registerDto_3 = SignUpDto.builder()
            .email("test_22@gmail.com")
            .name("test111")
            .password(invalidPw3)
            .nickname("test2 FC")
            .introduction("hello, there")
            .region("seoul")
            .build();
    SignUpDto valid_registerDto = SignUpDto.builder()
            .email("test111@gmail.com")
            .name("test111")
            .password(validPw)
            .nickname("test FC")
            .introduction("hello, there")
            .region("seoul")
            .build();

    @Test
    void register(){
        authService.registerMember(valid_registerDto);

//        System.out.println(invalid_registerDto_1.getPassword()+": ");
//        Assertions.assertThrows(InvalidPasswordFormatException.class, () -> authService.registerMember(invalid_registerDto_1));
//
//        System.out.println(invalid_registerDto_2.getPassword()+": ");
//        Assertions.assertThrows(InvalidPasswordFormatException.class, () -> authService.registerMember(invalid_registerDto_2));
//
//        System.out.println(invalid_registerDto_3.getPassword()+": ");
//        Assertions.assertThrows(InvalidPasswordFormatException.class, () -> authService.registerMember(invalid_registerDto_3));
    }

    @Test
    void checkDuplicatednickname(){
        String name1 = "test FC";
        String name2 = "new FC";
        System.out.println(name1 +" : " + memberService.isDuplicatedNickname(name1));
        System.out.println(name2 + " : " + memberService.isDuplicatedNickname(name2));
    }
}