package matchingGoal.matchingGoal.common.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

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
}
