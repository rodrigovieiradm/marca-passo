package br.com.guiabolso.marcapasso.models.lead

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class CreateLeadRequest {

    @NotNull
    Map<String, Object> uts

    @NotNull
    String utsId

    @Size(min = 36, max = 36)
    @NotNull
    String adId

    @NotNull
    Long recommendationId
    
}
