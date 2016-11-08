package br.com.guiabolso.marcapasso.models.interested

import groovy.transform.ToString

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ToString(includeFields = true)
class CreateInterestedRequest {
    @Size(min = 36, max = 36)
    @NotNull
    String prospectId

    @NotNull
    Long userId

    @Size(min = 1)
    @NotNull
    String offerId

    @NotNull
    Integer offerRank
}
