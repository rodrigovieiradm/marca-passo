package br.com.guiabolso.marcapasso.services.interested

import br.com.guiabolso.marcapasso.models.interested.CreateInterestedRequest
import br.com.guiabolso.marcapasso.models.interested.CreateInterestedResponse
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component

import javax.sql.DataSource

@Slf4j
@Component
class JDBCInterestedService implements InterestedService {

    JdbcTemplate jdbcTemplate

    @Autowired
    JDBCInterestedService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    CreateInterestedResponse create(CreateInterestedRequest request) {
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("interested")
                    .usingGeneratedKeyColumns("sequence_id")

            CreateInterestedResponse response = new CreateInterestedResponse(
                    id: UUID.randomUUID().toString(),
                    prospectId: request.prospectId,
                    userId: request.userId,
                    leadId: "----------------DUMMY---------------",
                    offerId: request.offerId,
                    offerRank: request.offerRank,
                    createdAt: new Date()
            )

            Map<String, Object> params = new HashMap<>();
            params.put("id", response.id)
            params.put("prospect_id", response.prospectId)
            params.put("user_id", response.userId)
            params.put("offer_id", response.offerId)
            params.put("lead_id", response.leadId)
            params.put("offer_rank", response.offerRank)
            params.put("created_at", response.createdAt)

            response.sequenceId = simpleJdbcInsert.executeAndReturnKey(params)

            return response
        } catch (DataAccessException exeption) {
            log.error("Fatal could not write interested in database interested={}", request, exeption)
            throw exeption
        }
    }
}