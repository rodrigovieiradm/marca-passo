package br.com.guiabolso.marcapasso.models.partnerLead

import groovy.transform.ToString

@ToString(includeFields = true)
class CreatePartnerLeadResponse {
    Long sequenceId
    String id
    String interestedId
    Long userId
    String offerId
    String leadId
    String url
    Date createdAt
}
