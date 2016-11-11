package br.com.guiabolso.marcapasso.services.interested

import br.com.guiabolso.marcapasso.Utils.VariablesUtils
import br.com.guiabolso.marcapasso.models.Offer
import br.com.guiabolso.marcapasso.models.interested.CreateInterestedRequest
import br.com.guiabolso.marcapasso.models.interested.CreateInterestedResponse
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component

import javax.sql.DataSource
import java.security.MessageDigest

@Slf4j
@Component
class JDBCInterestedService implements InterestedService {

    JdbcTemplate jdbcTemplate

    @Autowired
    JDBCInterestedService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    CreateInterestedResponse create(String leadId, CreateInterestedRequest request) {
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("interested")
                    .usingGeneratedKeyColumns("sequence_id")

            CreateInterestedResponse response = new CreateInterestedResponse(
                    id: UUID.randomUUID().toString(),
                    prospectId: request.prospectId,
                    userId: request.userId,
                    leadId: leadId,
                    offer: request.offer,
                    offerUrl: generateOfferUrl(request.offer, leadId, request.variables, request.simulation),
                    createdAt: new Date()
            )

            String offerRank = request.parameters.getOrDefault("offerRank", 0)

            Map<String, Object> params = new HashMap<>()
            params.put("id", response.id)
            params.put("prospect_id", response.prospectId)
            params.put("user_id", response.userId)
            params.put("offer_id", response.offer.id)
            params.put("lead_id", response.leadId)
            params.put("offer_rank", offerRank)
            params.put("created_at", response.createdAt)

            response.sequenceId = simpleJdbcInsert.executeAndReturnKey(params)

            return response
        } catch (DataAccessException exeption) {
            log.error("Fatal could not write interested in database interested={}", request, exeption)
            throw exeption
        }
    }

    private static generateOfferUrl(Offer offer, String leadId, Map<String, Object> variables, Map<String, Object> simulation) {

        /*
        'LENDICO', 'https://www.lendico.com.br/?utm_source=guiabolso&utm_medium=partnership&partner_id=guia_bolso&hash={HASH_CPF}&nome={NAME}&valor={DECIMAL_VALUE}&parcelas={INSTALMENTS}'
        'BANKFACIL', 'https://www.bkfonline.com.br/emprestimos/garantia-veiculo/afiliados/app?utm_source=guiabolso&utm_medium=affiliates&inputNome={NAME}&inputEmail={EMAIL}&how_much={VALUE}&instalments={INSTALMENTS}&what_for={NOT_EMPTY_REASON}&ref=guiabolso&hash={HASH_CPF}'
        'SIMPLIC', 'https://www.simplic.com.br/?utm_source=guiabolso&utm_medium=cpa&utm_campaign=marketplace&hash={HASH_CPF}'
        */

        String url = offer?.url?.replace('{LEAD_ID}', leadId)

        if (url) {

            String cpf = VariablesUtils.getUserCpfByVariables(variables)
            String hashCpf = MessageDigest.getInstance("MD5").digest(cpf.bytes).encodeHex().toString()
            String name = VariablesUtils.getUserFullNameByVariables(variables)
            String decimalValue = simulation.getOrDefault("amount", 0) + "00"
            String instalments = simulation.getOrDefault("installments", 0)
            String email = VariablesUtils.getUserEmailByVariables(variables)
            String value = simulation.getOrDefault("amount", 0)
            String notEmptyReason = ""

            url = url.replace('{HASH_CPF}', hashCpf)
            url = url.replace('{NAME}', name)
            url = url.replace('{DECIMAL_VALUE}', decimalValue)
            url = url.replace('{INSTALMENTS}', instalments)
            url = url.replace("'{EMAIL}", email)
            url = url.replace("'{VALUE}", value)
            url = url.replace("'{NOT_EMPTY_REASON}", notEmptyReason)

        }

        return url
    }

}