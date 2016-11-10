package br.com.guiabolso.marcapasso.controllers.lead

import br.com.guiabolso.marcapasso.models.lead.CreateLeadRequest
import br.com.guiabolso.marcapasso.models.lead.CreateLeadResponse
import br.com.guiabolso.marcapasso.services.lead.LeadService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Slf4j
@RequestMapping(value = "/lead/v1/")
@RestController
class LeadV1Controller {

    @Autowired
    private LeadService leadService

    @RequestMapping(value = "create/{userId}", method = RequestMethod.POST, consumes = "application/json")
    public CreateLeadResponse create(@PathVariable Long userId, @RequestBody String json) {

        ObjectMapper mapper = new ObjectMapper()
        CreateLeadRequest request = mapper.readValue(json, CreateLeadRequest.class)

        log.info("userId={} request={}", userId, request)
        return leadService.create(userId, request)
    }

}
