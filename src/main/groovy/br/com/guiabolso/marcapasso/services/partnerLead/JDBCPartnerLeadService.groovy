package br.com.guiabolso.marcapasso.services.partnerLead

import br.com.guiabolso.marcapasso.models.partnerLead.CreatePartnerLeadRequest
import br.com.guiabolso.marcapasso.models.partnerLead.CreatePartnerLeadResponse
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component

import javax.sql.DataSource

@Slf4j
@Component
class JDBCPartnerLeadService implements PartnerLeadService {

    JdbcTemplate jdbcTemplate

    @Autowired
    JDBCPartnerLeadService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    @Override
    CreatePartnerLeadResponse create(CreatePartnerLeadRequest partnerLead) {
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("partner_lead")
                    .usingGeneratedKeyColumns("sequence_id")

            CreatePartnerLeadResponse response = new CreatePartnerLeadResponse(
                    id: UUID.randomUUID().toString(),
                    interestedId: partnerLead.interestedId,
                    userId: partnerLead.userId,
                    offerId: partnerLead.offerId,
                    leadId: partnerLead.leadId,
                    url: partnerLead.url,
                    createdAt: new Date()
            )

            Map<String, Object> params = new HashMap<>();
            params.put("id", response.id)
            params.put("interested_id", response.interestedId)
            params.put("user_id", response.userId)
            params.put("offer_id", response.offerId)
            params.put("lead_id", response.leadId)
            params.put("url", response.url)
            params.put("created_at", response.createdAt)

            response.sequenceId = simpleJdbcInsert.executeAndReturnKey(params)

            return response

        } catch (DataAccessException exeption) {
            log.error("Fatal could not write partnerlead in database partnerlead={}", partnerLead, exeption)
            throw exeption
        }
    }
}