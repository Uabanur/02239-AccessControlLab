package dtu.group42.server.services;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import dtu.group42.server.models.SessionToken;

import java.time.LocalDateTime;

@Service
public class SessionService implements ISessionService {
    private ConcurrentHashMap<UUID, SessionToken> _sessions = new ConcurrentHashMap<UUID, SessionToken>();
    private static int timeoutHours = 1;

    public synchronized UUID createSession(String username) {
        UUID token = UUID.randomUUID();
        _sessions.put(token, new SessionToken(token, username));
        return token;
    }

    public synchronized SessionToken getSessionDataIfValid(UUID token) {
        if (token == null)
            return null;
        SessionToken sessionToken = _sessions.get(token);
        if (sessionToken == null)
            return null;
        return isValidToken(sessionToken) ? sessionToken : null;
    }

    public void clearExpired() {
        _sessions.entrySet().removeIf(entry -> !isValidToken(entry.getValue()));
    }

    public boolean isValidToken(SessionToken token) {
        return token.StartTime.isAfter(LocalDateTime.now().minusHours(timeoutHours));
    }
}
