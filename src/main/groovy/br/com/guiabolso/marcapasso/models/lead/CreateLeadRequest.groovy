package br.com.guiabolso.marcapasso.models.lead

class CreateLeadRequest {
    String adId
    String utsId
    Long recommendationId
    Map<String, Object> variables
}