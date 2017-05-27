package project.interceptors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import project.services.UserToken;
import project.services.UserTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ShiroSessionInterceptor implements HandlerInterceptor
{
	@Autowired
	private UserTokenService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		Subject subject = SecurityUtils.getSubject();

		if(!subject.isAuthenticated())
		{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		userService.setCurrentThreadTokenIdentifier(subject.getSession().getId().toString());
		userService.putToken(new UserToken(subject.getPrincipal().toString()+subject.getSession().getId().toString()),false);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
	{
		userService.clearCurrentThreadTokenIdentifier();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
