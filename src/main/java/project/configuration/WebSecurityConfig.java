package project.configuration;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.*;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mariosk89 on 20-Mar-16.
 */

@Configuration
public class WebSecurityConfig {

    @Autowired
    @Qualifier(value="shiroSessionListener")
    SessionListener shiroSessionListener;


    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        Map<String, String> filterChainDefinitionMapping = new HashMap<>();

        /*
        * URL path expressions are evaluated against an incoming request in the order they are defined and the FIRST MATCH WINS. For example, let's asume that there are the following chain definitions:
             /account/** = ssl, authc
             /account/signup = anon
          If an incoming request is intended to reach /account/signup/index.html (accessible by all 'anon'ymous users), it will never be handled!. The reason is that the /account/** pattern matched the incoming request first and 'short-circuited' all remaining definitions.
          Always remember to define your filter chains based on a FIRST MATCH WINS policy!
        * */

        filterChainDefinitionMapping.put("/login.html", "anon");
        filterChainDefinitionMapping.put("/logout", "logout");
        filterChainDefinitionMapping.put("/css/**", "anon");
        filterChainDefinitionMapping.put("/register/**", "anon");
        filterChainDefinitionMapping.put("/app/**", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
        shiroFilter.setSecurityManager(securityManager());

        shiroFilter.setLoginUrl("/login.html");
        shiroFilter.setSuccessUrl("/success.html");
        shiroFilter.setUnauthorizedUrl("/unauthorized.html");

        Map<String, Filter> filters = new HashMap<>();

        filters.put("pass", new PassThruAuthenticationFilter());
        filters.put("authc", new FormAuthenticationFilter());
        filters.put("anon", new AnonymousFilter());

        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/login.html?logout");
        filters.put("logout", logoutFilter);

        filters.put("roles", new RolesAuthorizationFilter());
        filters.put("user", new UserFilter());

        shiroFilter.setFilters(filters);

        return shiroFilter;
    }


    @Value("${preferredShiroRealm}")
    private String preferredShiroRealm;

    private Realm getRealm(String preferredShiroRealm)
    {
        ShiroRealms realm = ShiroRealms.getRealmForString(preferredShiroRealm);
        switch(realm)
        {
            case INI:
            {
                return iniRealm();
            }
            case JDBC:
            {
                return jdbcRealm();
            }
            default:
                return iniRealm();
        }
    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(getRealm(preferredShiroRealm));
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {

        final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setSessionValidationInterval(300000);
        sessionManager.setSessionIdCookie(cookie());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        sessionManager.setSessionListeners(sessionListeners());
        return sessionManager;
    }

    @Bean
    public Cookie cookie()
    {
        Cookie cookie = new SimpleCookie();
        cookie.setHttpOnly(true);
        cookie.setName("JSESSIONID");

        return cookie;
    }

    @Bean
    public List<SessionListener> sessionListeners()
    {
        List<SessionListener> sessionListeners = new ArrayList<SessionListener>();
        sessionListeners.add(shiroSessionListener);
        return sessionListeners;
    }

    /*Basic IniRealm working with hard coded usernames and passwords*/
    @Bean(name = "iniRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public IniRealm iniRealm() {
        IniRealm realm = new IniRealm();
        realm.addRole("admin");
        realm.addAccount("admin", "admin", "admin");
        return realm;
    }

    @Autowired
    private DataSource dataSource;

    @Bean(name = "jdbcRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public JdbcRealm jdbcRealm() {
        JdbcRealm realm = new JdbcRealm();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        realm.setCredentialsMatcher(credentialsMatcher);
        realm.setDataSource(dataSource);
        realm.init();
        return realm;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
