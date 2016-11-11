package br.com.guiabolso.marcapasso.controllers.interest

import br.com.guiabolso.marcapasso.models.interested.CreateInterestedRequest
import br.com.guiabolso.marcapasso.models.interested.CreateInterestedResponse
import br.com.guiabolso.marcapasso.models.lead.CreateLeadRequest
import br.com.guiabolso.marcapasso.models.lead.CreateLeadResponse
import br.com.guiabolso.marcapasso.services.interested.InterestedService
import br.com.guiabolso.marcapasso.services.lead.LeadService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RequestMapping(value = "/interested/v1/")
@RestController
class InterestedV1Controller {

    private InterestedService interestedService
    private LeadService leadService

    @Autowired
    InterestedV1Controller(InterestedService interestedService, LeadService leadService) {
        this.interestedService = interestedService
        this.leadService = leadService
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = "application/json")
    public CreateInterestedResponse create(@RequestBody String json) {

        ObjectMapper mapper = new ObjectMapper()
        CreateInterestedRequest request = mapper.readValue(json, CreateInterestedRequest.class)

        log.info("request={}", request)
        String leadId = "----------------DUMMY---------------"
        if (request.offer.financialEntity.equals("GUIABOLSO") || request.offer.financialEntity.equals("SEMEAR")) {
            CreateLeadResponse createLeadResponse = leadService.create(request.userId, new CreateLeadRequest(
                    variables: request.variables,
                    utsId: request.utsId,
                    adId: request.adId,
                    recommendationId: request.recommendationId
            ))
            leadId = createLeadResponse.id
            log.info("createLeadResponse={}", createLeadResponse)
        }

        return interestedService.create(leadId, request)
    }
}