package dtu.group42.server.services;

public interface IHashingService {
    byte[] hashPassword(final char[] password, final byte[] salt);
}
