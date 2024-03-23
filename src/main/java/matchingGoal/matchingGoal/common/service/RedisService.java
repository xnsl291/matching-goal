package matchingGoal.matchingGoal.common.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void addChatRoomMember(String chatRoomId, String sessionId, Long memberId) {

        redisTemplate.opsForHash().put(chatRoomId, sessionId, memberId);
        saveSessionIdAndChatRoomId(sessionId, chatRoomId);
        log.info("[redis saved] chatRoomId: " + chatRoomId + " sessionId: " + sessionId + " memberId: " + memberId);
    }

    public List<Object> getChatRoomMember(String chatRoomId) {

        return redisTemplate.opsForHash().values(chatRoomId);
    }

    @Transactional
    public void removeChatRoomMember(String sessionId) {

        String chatRoomId = getChatRoomId(sessionId);

        redisTemplate.opsForHash().delete(chatRoomId, sessionId);
        deleteChatRoomId(sessionId);
        log.info("[redis delete] chatRoomId: " + chatRoomId + " sessionId: " + sessionId);
    }
    public void saveSessionIdAndChatRoomId(String sessionId, String chatRoomId){

        stringRedisTemplate.opsForValue().append(sessionId, chatRoomId);
        log.info("[redis saved] sessionId: " + sessionId + " chatRoomId: " + chatRoomId);
    }

    public String getChatRoomId(String sessionId) {

        return stringRedisTemplate.opsForValue().get(sessionId);
    }

    public void deleteChatRoomId(String sessionId) {
        stringRedisTemplate.delete(sessionId);
        log.info("[redis delete] sessionId: " + sessionId);
    }

}
