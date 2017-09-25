package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

	@Autowired 	
	@Qualifier(value="shiroSessionInterceptor")
	HandlerInterceptor shiroSessionInterceptor;
	
	/*Define the DispatcherServlet servlet*/
    @Bean
    public DispatcherServlet dispatcherServlet() {

        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setDispatchOptionsRequest(true);

        return servlet;
    }

    /*Register the DispatcherSevlet to the desired URL*/
    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {

        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/*");

        return registration;
    }


    /*Registering the Interceptors*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
      InterceptorRegistration reg = registry.addInterceptor(shiroSessionInterceptor);      
      reg.addPathPatterns("/**");
      reg.excludePathPatterns("/register");
    }

    
    /*Handles Content Negotiation*/
    /*https://spring.io/blog/2013/05/11/content-negotiation-using-spring-mvc*/
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        Map<String, MediaType> types = new HashMap<>();
        types.put("json", MediaType.APPLICATION_JSON);
        types.put("xml", MediaType.APPLICATION_XML);

        configurer.favorPathExtension(false).
        	useJaf(false).
            defaultContentType(MediaType.APPLICATION_JSON).
            mediaType("xml", MediaType.APPLICATION_XML).
            mediaType("json", MediaType.APPLICATION_JSON);                      
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(jackson());
        converters.add(jaxb());

        super.configureMessageConverters(converters);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson() {

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return converter;
    }

    @Bean
    public Jaxb2RootElementHttpMessageConverter jaxb() {

        final Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();

        return converter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
//        registry.addViewController("/login").setViewName("forward:/account/login.html");
    }

}
