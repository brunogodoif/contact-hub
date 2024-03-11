package br.com.brunogodoif.contacthub.adapters.inbound.controllers;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response.ListingContactsRecord;
import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response.ListingProfessionalsRecordResponse;
import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ProfessionalPersistenceRequest;
import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response.ProfessionalRecordResponse;
import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationRequest;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalUpdateDomain;
import br.com.brunogodoif.contacthub.core.domain.response.ListingContactsResponse;
import br.com.brunogodoif.contacthub.core.domain.response.ListingProfessionalsResponse;
import br.com.brunogodoif.contacthub.core.ports.inbound.ContactFindServicePort;
import br.com.brunogodoif.contacthub.core.ports.inbound.ProfessionalFindServicePort;
import br.com.brunogodoif.contacthub.core.ports.inbound.ProfessionalPersistenceServicePort;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/professionals")
public class ProfessionalsController {

    private final ProfessionalFindServicePort professionalFindService;
    private final ProfessionalPersistenceServicePort professionalPersistenceService;
    private final ContactFindServicePort contactFindService;

    public ProfessionalsController(ProfessionalFindServicePort professionalFindService,
                                   ProfessionalPersistenceServicePort professionalPersistenceService,
                                   ContactFindServicePort contactFindService) {
        this.professionalFindService = professionalFindService;
        this.professionalPersistenceService = professionalPersistenceService;
        this.contactFindService = contactFindService;
    }

    @GetMapping
    public ResponseEntity<ListingProfessionalsRecordResponse> listProfessionalsPaginate(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Receiving request to find professional by filters parameters [{}]", pageable);
        PaginationRequest paginationRequest = new PaginationRequest(pageable.getPageNumber(), pageable.getPageSize());
        ListingProfessionalsResponse listingProfessionalsResponse = professionalFindService.findAllWithPaginate(paginationRequest);
        return ResponseEntity.ok(ListingProfessionalsRecordResponse.fromDomain(listingProfessionalsResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalRecordResponse> getProfessionalById(@PathVariable String id) {
        log.info("Receiving request to get contact by id [id: {}]", id);
        ProfessionalDomain professionalDomain = professionalFindService.findById(id);
        return ResponseEntity.ok(ProfessionalRecordResponse.fromDomain(professionalDomain));
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<ListingContactsRecord> listAllContactsByProfessionalId(@PathVariable String id) {
        log.info("Receiving request to list all contacts by professionalId: [id: {}]", id);
        ListingContactsResponse listingContactsResponse = contactFindService.findAllByProfessionalId(id);
        return ResponseEntity.ok(ListingContactsRecord.fromDomain(listingContactsResponse));
    }

    @PostMapping
    public ResponseEntity<ProfessionalRecordResponse> createProfessional(@RequestBody ProfessionalPersistenceRequest professionalPersistenceRequest) {
        log.info("Receiving request to create professional [data: {}]", professionalPersistenceRequest);
        ProfessionalDomain createdProfessional = professionalPersistenceService.create(ProfessionalCreateDomain.toDomain(professionalPersistenceRequest));
        if (createdProfessional == null)
            return ResponseEntity.internalServerError().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfessionalRecordResponse.fromDomain(createdProfessional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateProfessional(@PathVariable String id, @RequestBody ProfessionalPersistenceRequest professionalPersistenceRequest) {
        log.info("Receiving request to update professional [id:{}, data: {}]", id, professionalPersistenceRequest);
        ProfessionalDomain updatedProfessional = professionalPersistenceService.update(ProfessionalUpdateDomain.toDomain(professionalPersistenceRequest, id));
        if (updatedProfessional == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProfessional(@PathVariable String id) {
        log.info("Receiving request to delete professional [id:{}]", id);
        if (!professionalPersistenceService.deleteById(id))
            return ResponseEntity.unprocessableEntity().body(false);

        return ResponseEntity.ok().body(true);

    }
}