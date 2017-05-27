package project.listeners;

import project.services.UserToken;

public interface UserTokenServiceListener {

    public void tokenCreated(UserToken userToken);
    public void tokenDestroyed(UserToken userToken);
}
