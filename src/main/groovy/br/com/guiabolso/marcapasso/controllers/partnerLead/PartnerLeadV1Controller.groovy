package br.com.guiabolso.marcapasso.controllers.partnerLead

import br.com.guiabolso.marcapasso.models.partnerLead.CreatePartnerLeadRequest
import br.com.guiabolso.marcapasso.models.partnerLead.CreatePartnerLeadResponse
import br.com.guiabolso.marcapasso.services.partnerLead.PartnerLeadService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RequestMapping(value = "/partnerLead/v1/")
@RestController
class PartnerLeadV1Controller {

    private PartnerLeadService partnerLeadService

    @Autowired
    PartnerLeadV1Controller(PartnerLeadService partnerLeadService) {
        this.partnerLeadService = partnerLeadService
    }

    @RequestMapping(value = "/create/", method = RequestMethod.POST)
    public CreatePartnerLeadResponse create(@Validated @RequestBody CreatePartnerLeadRequest request) {
        log.info("request={}", request)
        return partnerLeadService.create(request)
    }
}
