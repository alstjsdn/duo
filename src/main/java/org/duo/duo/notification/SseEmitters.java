package org.duo.duo.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class SseEmitters {

    private static final long TIMEOUT = 30 * 60 * 1000L; // 30분

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(Long userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.computeIfAbsent(userId, id -> new CopyOnWriteArrayList<>()).add(emitter);

        Runnable cleanup = () -> {
            List<SseEmitter> list = emitters.get(userId);
            if (list != null) {
                list.remove(emitter);
                if (list.isEmpty()) emitters.remove(userId);
            }
        };

        emitter.onCompletion(() -> { log.debug("SSE 연결 종료: userId={}", userId); cleanup.run(); });
        emitter.onTimeout(()  -> { log.debug("SSE 타임아웃: userId={}", userId);  cleanup.run(); });
        emitter.onError(e     -> { log.debug("SSE 에러: userId={}", userId);      cleanup.run(); });

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (Exception e) {
            cleanup.run();
        }

        return emitter;
    }

    public void send(Long userId, Object data) {
        List<SseEmitter> list = emitters.get(userId);
        if (list == null || list.isEmpty()) return;

        List<SseEmitter> dead = new java.util.ArrayList<>();
        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(data));
            } catch (Exception e) {
                log.debug("SSE 전송 실패, emitter 제거: userId={}", userId);
                dead.add(emitter);
            }
        }
        list.removeAll(dead);
        if (list.isEmpty()) emitters.remove(userId);
    }
}