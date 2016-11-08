package br.com.guiabolso.marcapasso.controllers.prospect

import br.com.guiabolso.marcapasso.models.prospect.CreateProspectRequest
import br.com.guiabolso.marcapasso.models.prospect.CreateProspectResponse
import br.com.guiabolso.marcapasso.services.prospect.ProspectService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RequestMapping(value = "/prospect/v1/")
@RestController
class ProspectV1Controller {

    private ProspectService prospectService

    @Autowired
    ProspectV1Controller(ProspectService prospectService) {
        this.prospectService = prospectService
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CreateProspectResponse create(@Validated @RequestBody CreateProspectRequest request) {
        log.info("request={}", request)
        return prospectService.create(request)
    }
}