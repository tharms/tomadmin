package demo;

import javax.servlet.ServletContext;

import org.springframework.boot.legacy.context.web.SpringBootContextLoaderListener;

import org.springframework.web.context.WebApplicationContext;

public class CustomSpringBootContextLoaderListener extends SpringBootContextLoaderListener {

    @Override
    public WebApplicationContext initWebApplicationContext(final ServletContext servletContext) {
        return super.initWebApplicationContext(servletContext);
    }
}
