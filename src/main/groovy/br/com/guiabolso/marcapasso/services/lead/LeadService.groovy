package br.com.guiabolso.marcapasso.services.lead

import br.com.guiabolso.marcapasso.models.lead.CreateLeadRequest
import br.com.guiabolso.marcapasso.models.lead.CreateLeadResponse

interface LeadService {
    CreateLeadResponse create(Long userId, CreateLeadRequest request)
}