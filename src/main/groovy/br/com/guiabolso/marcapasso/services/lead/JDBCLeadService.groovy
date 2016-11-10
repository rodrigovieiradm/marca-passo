package br.com.guiabolso.marcapasso.services.lead

import br.com.guiabolso.marcapasso.Utils.UtsUtils
import br.com.guiabolso.marcapasso.models.lead.CreateLeadRequest
import br.com.guiabolso.marcapasso.models.lead.CreateLeadResponse
import br.com.guiabolso.marcapasso.services.customerInfo.CustomerInfoService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component

import javax.sql.DataSource

@Slf4j
@Component
class JDBCLeadService implements LeadService {

    private static final int LEAD_STATUS_CREATED = 1

    JdbcTemplate jdbcTemplate
    CustomerInfoService customerInfoService

    @Autowired
    JDBCLeadService(DataSource dataSource, CustomerInfoService customerInfoService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
        this.customerInfoService = customerInfoService
    }

    @Override
    CreateLeadResponse create(Long userId, CreateLeadRequest request) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("lead")

            Map<String, Object> uts = request.uts

            CreateLeadResponse response = new CreateLeadResponse()
            response.id = UUID.randomUUID().toString()
            response.customerInfo = UtsUtils.getCustomerInfoMapByUTS(uts)
            response.createdAt = new Date()

            response.status = LEAD_STATUS_CREATED

            response.utsId = request.utsId
            response.lastUpdate = new Date()

            response.apiClientUniqueId = uts.getOrDefault("USER.EMAIL", "")

            response.userId = userId
            response.adId = request.adId
            response.recommendationId = request.recommendationId

            response.bankAccounts = UtsUtils.getBankAccountsByUts(uts)

            Map<String, Object> params = new HashMap<>()
            params.put("id", response.id)

            params.put("costumer_info", UtsUtils.getCustomerInfoJsonByUts(response.customerInfo))

            params.put("created_at", response.createdAt)
            params.put("status", response.status)
            params.put("uts_id", response.utsId)
            params.put("session_id", response.id)
            params.put("last_update", response.lastUpdate)
            params.put("api_client_unique_id", response.apiClientUniqueId)
            params.put("user_id", response.userId)
            params.put("ad_id", response.adId)
            params.put("recommendation_id", response.recommendationId)

            long customerInfoId = customerInfoService.create(userId, response.customerInfo)
            params.put("customer_info_id", customerInfoId)

            simpleJdbcInsert.execute(params)

            return response
        } catch (DataAccessException exeption) {
            log.error("Fatal could not write lead in database lead={}", request, exeption)
            throw exeption
        }
    }
}