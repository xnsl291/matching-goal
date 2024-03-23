package matchingGoal.matchingGoal.common.service;

import static org.junit.jupiter.api.Assertions.*;

import matchingGoal.matchingGoal.alarm.repository.AlarmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisServiceTest {
  @Autowired
  RedisService redisService;
  @Autowired
  private RedisTemplate<String, Object> redisTemplate;
  @Test
  void getChatRoomMember() {
    HashOperations<String, String, Long> hash = redisTemplate.opsForHash();
    String chatroomId = "find";
    System.out.println(hash.values("find"));
    System.out.println(hash.entries("find"));
    System.out.println(hash.get("find", "895ea644-9863-f14b-f3bc-c54f70cb425b"));
  }
}