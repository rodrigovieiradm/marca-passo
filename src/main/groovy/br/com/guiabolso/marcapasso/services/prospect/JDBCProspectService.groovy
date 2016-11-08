package br.com.guiabolso.marcapasso.services.prospect

import br.com.guiabolso.marcapasso.models.prospect.CreateProspectRequest
import br.com.guiabolso.marcapasso.models.prospect.CreateProspectResponse
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component

import javax.sql.DataSource

@Slf4j
@Component
class JDBCProspectService implements ProspectService {

    JdbcTemplate jdbcTemplate

    @Autowired
    JDBCProspectService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    CreateProspectResponse create(CreateProspectRequest request) {
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("prospect")
                    .usingGeneratedKeyColumns("sequence_id")

            CreateProspectResponse response = new CreateProspectResponse(
                    id: UUID.randomUUID().toString(),
                    adId: request.adId,
                    userId: request.userId,
                    leadId: "----------------DUMMY---------------",
                    createdAt: new Date()
            )

            Map<String, Object> params = new HashMap<>()
            params.put("id", response.id)
            params.put("ad_id", response.adId)
            params.put("user_id", response.userId)
            params.put("lead_id", response.leadId)
            params.put("created_at", response.createdAt)

            response.sequenceId = simpleJdbcInsert.executeAndReturnKey(params)

            return response
        } catch (DataAccessException exeption) {
            log.error("Fatal could not write prospect in database prospect={}", request, exeption)
            throw exeption
        }
    }
}