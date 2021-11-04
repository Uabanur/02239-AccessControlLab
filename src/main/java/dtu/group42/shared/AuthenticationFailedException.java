package dtu.group42.shared;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message){
        super(message);
    }
}
