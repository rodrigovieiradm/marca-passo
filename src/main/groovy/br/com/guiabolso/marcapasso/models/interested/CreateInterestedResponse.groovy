package br.com.guiabolso.marcapasso.models.interested

import br.com.guiabolso.marcapasso.models.Offer
import groovy.transform.ToString

@ToString(includeFields = true)
class CreateInterestedResponse {
    Long sequenceId
    String id
    String prospectId
    Long userId
    String leadId
    Offer offer
    Date createdAt
}