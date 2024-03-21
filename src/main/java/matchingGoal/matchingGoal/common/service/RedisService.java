package matchingGoal.matchingGoal.common.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    public String getData(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value, long durationInMinutes) {
        stringRedisTemplate.opsForValue().set(key, value, durationInMinutes, TimeUnit.MINUTES);
    }

    public void deleteData(String key){
        stringRedisTemplate.delete(key);
    }

    public boolean hasKey(String email) {
        Boolean keyExists = stringRedisTemplate.hasKey(email);
        return keyExists != null && keyExists;
    }

    public void setBlackList(String key, String value, long durationInMinutes) {
        stringRedisTemplate.opsForSet().add(key, value);
        stringRedisTemplate.expire(key, durationInMinutes, TimeUnit.MINUTES);
    }

    public boolean hasKeyAndValue(String key, String value){
        System.out.println(key +"  " + value);
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, value));
    }

    public void addChatRoomMember(String chatroomId, String sessionId, Long memberId) {
        redisTemplate.opsForHash().put(chatroomId,sessionId, memberId);
        log.info("[redis saved] chatroomId: " + chatroomId + " sessionId: " + sessionId + " memberId: " + memberId);
    }

    public List<Object> getChatRoomMember(String chatroomId) {
        return redisTemplate.opsForHash().values(chatroomId);
    }
    public void removeChatRoomMember(String chatroomId, String sessionId) {
        log.info("[redis removed] chatroomId: " + chatroomId + " sessionId: " + sessionId);
        redisTemplate.opsForHash().delete(chatroomId, sessionId);

    }


}
