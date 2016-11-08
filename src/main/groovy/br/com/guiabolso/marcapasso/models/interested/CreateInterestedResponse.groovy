package br.com.guiabolso.marcapasso.models.interested

import groovy.transform.ToString

@ToString(includeFields = true)
class CreateInterestedResponse {
    Long sequenceId
    String id
    String prospectId
    Long userId
    String leadId
    String offerId
    Integer offerRank
    Date createdAt
}