package br.com.brunogodoif.contacthub.adapters.inbound.controllers;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ContactPersistenceRequest;
import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response.ContactRecord;
import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain;
import br.com.brunogodoif.contacthub.core.ports.inbound.ContactPersistenceServicePort;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/contacts")
public class ContactsController {
    private final ContactPersistenceServicePort contactPersistenceService;

    public ContactsController(ContactPersistenceServicePort contactPersistenceService) {
        this.contactPersistenceService = contactPersistenceService;
    }
    @PostMapping
    public ResponseEntity<ContactRecord> createContact(@RequestBody ContactPersistenceRequest contactPersistenceRequest) {
        log.info("Receiving request to create contact [data: {}]", contactPersistenceRequest);
        ContactDomain createdContact = contactPersistenceService.create(ContactCreateDomain.toDomain(contactPersistenceRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContactRecord.fromDomain(createdContact));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateContact(@PathVariable String id, @RequestBody ContactPersistenceRequest contactPersistenceRequest) {
        log.info("Receiving request to update contact [id:{}, data: {}]", id, contactPersistenceRequest);
        contactPersistenceService.update(ContactUpdateDomain.toDomain(contactPersistenceRequest, id));
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteContact(@PathVariable String id) {
        log.info("Receiving request to delete contact [id:{}]", id);
        if (!contactPersistenceService.deleteById(id))
            return ResponseEntity.unprocessableEntity().body(false);

        return ResponseEntity.ok().body(true);
    }
}
