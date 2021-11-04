package dtu.group42.server;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionToken {
    public UUID Content;
    public String User;
    public LocalDateTime StartTime;

    public SessionToken(UUID content, String user){
        this.Content = content;
        this.User = user;
        this.StartTime = LocalDateTime.now();
    }

}
