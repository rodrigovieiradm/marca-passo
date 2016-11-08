package br.com.guiabolso.marcapasso.services.partnerLead

import br.com.guiabolso.marcapasso.models.partnerLead.CreatePartnerLeadRequest
import br.com.guiabolso.marcapasso.models.partnerLead.CreatePartnerLeadResponse

interface PartnerLeadService {
    CreatePartnerLeadResponse create(CreatePartnerLeadRequest partnerLead);
}