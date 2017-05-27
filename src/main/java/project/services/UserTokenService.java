package project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import project.listeners.UserTokenServiceListener;

@Service
public  class UserTokenService
{
    private ConcurrentMap<Object, UserToken> tokenStore = new ConcurrentHashMap<>();
    private List<UserTokenServiceListener> userTokenServiceListeners = new ArrayList<UserTokenServiceListener>();
    private ThreadLocal<Object> tokenIdentifier = new ThreadLocal<Object>();

    public UserToken getToken()
    {
        if (this.tokenIdentifier.get() != null)
        {
            return tokenStore.get(this.tokenIdentifier.get());
        }
        else
        {
            throw new IllegalStateException();
        }
    }

    public void putToken(UserToken userToken,boolean replace)
    {
        if (!tokenStore.containsKey(this.tokenIdentifier.get()) || replace)
        {
            tokenStore.put(this.tokenIdentifier.get(), userToken);
            fireTokenAdded(userToken);
        }
    }

    public void setCurrentThreadTokenIdentifier(Object object)
    {
        tokenIdentifier.set(object);
    }

    public void clearCurrentThreadTokenIdentifier()
    {
        tokenIdentifier.remove();
    }

    private void fireTokenAdded(UserToken userToken)
    {
        for (UserTokenServiceListener userTokenServiceListener:userTokenServiceListeners)
        {
            userTokenServiceListener.tokenCreated(userToken);
        }
    }

    public void deleteToken()
    {
        if (tokenStore.containsKey(this.tokenIdentifier.get()))
        {
            UserToken userToken = tokenStore.get(this.tokenIdentifier.get());
            tokenStore.remove(this.tokenIdentifier.get());
            fireTokenDeleted(userToken);
        }
    }

    public String getUserNameFromToken()
    {
        return StringUtils.replace(getToken().getToken().toString(), this.tokenIdentifier.get().toString(), "");
    }

    private void fireTokenDeleted(UserToken userToken)
    {
        for (UserTokenServiceListener userTokenServiceListener:userTokenServiceListeners)
        {
            userTokenServiceListener.tokenDestroyed(userToken);
        }
    }

    public void addUserTokenServiceListener(UserTokenServiceListener userTokenServiceListener)
    {
        userTokenServiceListeners.add(userTokenServiceListener);
    }

    public void removeUserTokenServiceListener(UserTokenServiceListener userTokenServiceListener)
    {
        if (userTokenServiceListeners.contains(userTokenServiceListener))
        {
            userTokenServiceListeners.remove(userTokenServiceListener);
        }
    }
}

