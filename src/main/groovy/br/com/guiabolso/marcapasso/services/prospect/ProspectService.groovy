package br.com.guiabolso.marcapasso.services.prospect

import br.com.guiabolso.marcapasso.models.prospect.CreateProspectRequest
import br.com.guiabolso.marcapasso.models.prospect.CreateProspectResponse

interface ProspectService {
    CreateProspectResponse create(CreateProspectRequest request)
}