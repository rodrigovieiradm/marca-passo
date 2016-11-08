package br.com.guiabolso.marcapasso.models.partnerLead

import groovy.transform.ToString

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ToString(includeFields = true)
class CreatePartnerLeadRequest {
    @Size(min = 36, max = 36)
    @NotNull
    String interestedId

    @NotNull
    Long userId

    @Size(min = 1)
    @NotNull
    String offerId

    @Size(min = 36, max = 36)
    @NotNull
    String leadId

    @Size(min = 1)
    @NotNull
    String url

}
