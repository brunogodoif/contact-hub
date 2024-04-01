package br.com.brunogodoif.contacthub.adapters.configs;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ResolverConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public SpecificationArgumentResolver specificationArgumentResolver() {
        return new SpecificationArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
        return new PageableHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }

}
