package br.com.guiabolso.marcapasso.models.interested

import br.com.guiabolso.marcapasso.models.Offer
import groovy.transform.ToString

@ToString(includeFields = true)
class CreateInterestedRequest {
    Long userId
    String utsId
    Long recommendationId
    String adId
    String prospectId
    Offer offer
    Map<String, Object> parameters
    Map<String, Object> simulation
    Map<String, Object> variables
}