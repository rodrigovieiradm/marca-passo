package br.com.guiabolso.marcapasso.controllers.health

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @RequestMapping(value = "/health")
    String health() {
        return "OK"
    }
}