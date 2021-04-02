package penguin.wordbook.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 조회
     * @param key {String} 데이터 키
     * @return {String} 데이터 값
     */
    public String getData(String key){
        if (key == null) return null;
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 생성
     * @param key {String} 데이터 키
     * @param value {String} 데이터 값
     */
    public void setData(String key, String value){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    /**
     * 시한부 생성
     * @param key {String} 데이터 키
     * @param value {String} 데이터 값
     * @param duration {long} 수명 - redis template 이 primitive type 을 사용하므로 인자도 primitive 로 받음
     */
    public void setDataExpire(String key,String value,long duration){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key,value,expireDuration);
    }

    /**
     * 삭제
     * @param key {String} 데이터 키
     */
    public void deleteData(String key){
        stringRedisTemplate.delete(key);
    }
}
