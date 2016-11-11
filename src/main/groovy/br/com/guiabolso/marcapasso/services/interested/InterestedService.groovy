package br.com.guiabolso.marcapasso.services.interested

import br.com.guiabolso.marcapasso.models.interested.CreateInterestedRequest
import br.com.guiabolso.marcapasso.models.interested.CreateInterestedResponse

interface InterestedService {
    CreateInterestedResponse create(String leadId, CreateInterestedRequest request)
}