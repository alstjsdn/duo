package org.duo.duo.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitters {

    private static final long TIMEOUT = 30 * 60 * 1000L; // 30분

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(Long userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> {
            log.debug("SSE 연결 종료: userId={}", userId);
            emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.debug("SSE 타임아웃: userId={}", userId);
            emitters.remove(userId);
        });
        emitter.onError(e -> {
            log.debug("SSE 에러: userId={}", userId);
            emitters.remove(userId);
        });

        // 연결 직후 더미 이벤트 (503 방지)
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitters.remove(userId);
        }

        return emitter;
    }

    public void send(Long userId, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(data));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}