package br.com.guiabolso.marcapasso.models.prospect

import groovy.transform.ToString

@ToString(includeFields = true)
class CreateProspectResponse {
    Long sequenceId
    String id
    String adId
    Long userId
    String leadId
    Date createdAt
}