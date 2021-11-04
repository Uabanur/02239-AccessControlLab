package dtu.group42.server.services;

import java.util.UUID;

import dtu.group42.server.SessionToken;

public interface ISessionService {
    UUID createSession(String username);
    SessionToken getSessionDataIfValid(UUID token);
    void clearExpired();
    boolean isValidToken(SessionToken token);
}
