package project.listeners;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.stereotype.Component;

import project.services.UserTokenService;

/**
 *
 */
@Component
public class ShiroSessionListener extends SessionListenerAdapter
{
	@Resource
	private UserTokenService userTokenService;

	@Override
	public void onStop(Session session)
	{
		clearSession(session);
	}

	@Override
	public void onExpiration(Session session)
	{
		clearSession(session);
	}

	private void clearSession(final Session session)
	{
        userTokenService.setCurrentThreadTokenIdentifier(session.getId().toString());
		userTokenService.deleteToken();
	}
}
