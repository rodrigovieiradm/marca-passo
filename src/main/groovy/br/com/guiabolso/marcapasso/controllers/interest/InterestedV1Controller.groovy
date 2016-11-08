package br.com.guiabolso.marcapasso.controllers.interest

import br.com.guiabolso.marcapasso.models.interested.CreateInterestedRequest
import br.com.guiabolso.marcapasso.models.interested.CreateInterestedResponse
import br.com.guiabolso.marcapasso.services.interested.InterestedService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RequestMapping(value = "/interested/v1/")
@RestController
class InterestedController {

    private InterestedService interestedService

    @Autowired
    InterestedController(InterestedService interestedService) {
        this.interestedService = interestedService
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CreateInterestedResponse create(@Validated @RequestBody CreateInterestedRequest request) {
        log.info("request={}", request)
        return interestedService.create(request)
    }
}
