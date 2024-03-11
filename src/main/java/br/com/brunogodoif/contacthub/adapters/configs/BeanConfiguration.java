package br.com.brunogodoif.contacthub.adapters.configs;

import br.com.brunogodoif.contacthub.ContactHubApplication;
import br.com.brunogodoif.contacthub.core.ports.inbound.ContactFindServicePort;
import br.com.brunogodoif.contacthub.core.ports.inbound.ContactPersistenceServicePort;
import br.com.brunogodoif.contacthub.core.ports.inbound.ProfessionalFindServicePort;
import br.com.brunogodoif.contacthub.core.ports.inbound.ProfessionalPersistenceServicePort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort;
import br.com.brunogodoif.contacthub.core.services.ContactFindService;
import br.com.brunogodoif.contacthub.core.services.ContactPersistenceService;
import br.com.brunogodoif.contacthub.core.services.ProfessionalFindService;
import br.com.brunogodoif.contacthub.core.services.ProfessionalPersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ContactHubApplication.class)
public class BeanConfiguration {

    @Bean
    ProfessionalFindServicePort ProfessionalFindService(ProfessionalAdapterPort professionalAdapter) {
        return new ProfessionalFindService(professionalAdapter);
    }

    @Bean
    ProfessionalPersistenceServicePort ProfessionalPersistenceService(ProfessionalAdapterPort professionalAdapter) {
        return new ProfessionalPersistenceService(professionalAdapter);
    }

    @Bean
    ContactFindServicePort ContactFindService(ContactAdapterPort contactAdapter) {
        return new ContactFindService(contactAdapter);
    }

    @Bean
    ContactPersistenceServicePort ContactPersistenceService(ContactAdapterPort contactAdapter, ProfessionalAdapterPort professionalAdapterPort) {
        return new ContactPersistenceService(contactAdapter, professionalAdapterPort);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
