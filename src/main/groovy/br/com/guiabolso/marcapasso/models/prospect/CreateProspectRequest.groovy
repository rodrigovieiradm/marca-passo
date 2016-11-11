package br.com.guiabolso.marcapasso.models.prospect

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.ToString

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(includeFields = true)
class CreateProspectRequest {
    @Size(min = 36, max = 36)
    @NotNull
    String adId

    @NotNull
    Long userId
}