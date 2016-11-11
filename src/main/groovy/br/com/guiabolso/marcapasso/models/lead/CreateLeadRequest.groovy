package br.com.guiabolso.marcapasso.models.lead

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CreateLeadRequest {
    String adId
    String utsId
    Long recommendationId
    Map<String, Object> variables
}