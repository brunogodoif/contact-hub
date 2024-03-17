package br.com.brunogodoif.contacthub.adapters.configs;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


@Configuration
public class ResolverConfig extends WebMvcConfigurationSupport {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());

        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        argumentResolvers.add(resolver);
        super.addArgumentResolvers(argumentResolvers);
    }

}

//@Configuration
//public class ResolverConfig implements WebMvcConfigurer {

//    @Bean
//    @ConditionalOnMissingBean
//    public SpecificationArgumentResolver specificationArgumentResolver() {
//        return new SpecificationArgumentResolver();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
//        return new PageableHandlerMethodArgumentResolver();
//    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new SpecificationArgumentResolver());
//        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
//    }

//}
