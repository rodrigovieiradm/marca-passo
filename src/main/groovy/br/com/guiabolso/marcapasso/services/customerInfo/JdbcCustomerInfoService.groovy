package br.com.guiabolso.marcapasso.services.customerInfo

import br.com.guiabolso.marcapasso.models.CustomerInfoData
import br.com.guiabolso.marcapasso.services.encryptor.EncryptorService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component

import javax.sql.DataSource
import java.sql.Types

@Slf4j
@Component
class JdbcCustomerInfoService implements CustomerInfoService {

    private static final List<String> encriptedKeys = Arrays.asList(
            'COSTUMER_CONTACT_EMAIL',
            'COSTUMER_CONTACT_PHONE',
            'COSTUMER_ADDRESS_COMPL',
            'COSTUMER_ADDRESS_STREET',
            'COSTUMER_ADDRESS_NUMBER',
            'COSTUMER_ADDRESS_ZIP',
            'COSTUMER_DOC_NUMBER',
            'COSTUMER_DOC_ISSUE_DATE',
            'COSTUMER_DOC_TYPE',
            'COSTUMER_DOC_UF',
            'COSTUMER_DOC_CPF',
            'COSTUMER_DOC_FULL_NAME',
            'COSTUMER_DOC_MOTHERS_NAME',
            'COSTUMER_DOC_ISSUING_AGENCY',
            'COSTUMER_BANK_ACCOUNT',
            'BANK_INCOME_ACCOUNT',
            'BANK_PAYMENT_ACCOUNT'
    )

    JdbcTemplate jdbcTemplate
    EncryptorService encryptorService

    @Autowired
    JDBCLeadService(DataSource dataSource, EncryptorService encryptorService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
        this.encryptorService = encryptorService
    }

    @Override
    Long create(Long userId, Map<String, Object> customerInfo) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("customer_info")
                    .usingGeneratedKeyColumns("id")

            Map<String, Object> params = new HashMap<>()
            params.put("user_id", userId)
            params.put("created_at", new Date())
            params.put("updated_at", new Date())

            Long customerInfoId = simpleJdbcInsert.executeAndReturnKey(params)

            List<CustomerInfoData> customerInfoDataList = customerInfo.collect {
                String type = it.value.getClass().getSimpleName()
                String value = String.valueOf(it.value)

                if (type != 'Boolean' && type != 'Double' && type != 'Long' && type != 'String') {
                    type = 'String'
                }

                new CustomerInfoData(
                        key: String.valueOf(it.key),
                        value: value,
                        type: type,
                        createdAt: new Date(),
                        updatedAt: new Date()
                )
            }

            saveCustomerInfoData(customerInfoId, customerInfoDataList)

            return customerInfoId
        } catch (DataAccessException exeption) {
            log.error("Fatal could not write customerInfo in database customerInfo={}", customerInfo, exeption)
            throw exeption
        }
    }

    private void saveCustomerInfoData(Long customerInfoId, List<CustomerInfoData> customerInfoDataList) {
        customerInfoDataList.each {
            saveCustomerInfoData(customerInfoId, it)
        }
    }

    private void saveCustomerInfoData(Long customerInfoId, CustomerInfoData customerInfoData) {
        try {
            log.info("Start saving customerInfo: customerInfoId={} and customerInfoKey={}", customerInfoId, customerInfoData.key)

            if (shouldEncryptValue(customerInfoData.key)) {
                customerInfoData.value = encryptorService.getEncryptedValue(customerInfoData.value)
            }

            Object[] args = [customerInfoId, customerInfoData.key, customerInfoData.value, customerInfoData.type, customerInfoData.value, customerInfoData.type]
            int[] argTypes = [Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR]

            String sql = "INSERT INTO customer_info_data " +
                    "(customer_info_id, customer_info_key, customer_info_value, customer_info_type, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, NOW(), NOW()) " +
                    "ON DUPLICATE KEY UPDATE customer_info_value = ?, customer_info_type = ?, updated_at = NOW() "
            jdbcTemplate.update(sql, args, argTypes)

            log.info("Finish saving customerInfo: customerInfoId={} and customerInfoKey={}", customerInfoId, customerInfoData.key)
        } catch (DataAccessException e) {
            log.error("Fatal could not write CustomerInfoData in database customerInfoData={}", customerInfoData)
            log.error(e.getMessage(), e)
            throw e
        }
    }

    private static boolean shouldEncryptValue(String customerInfoKey) {
        return encriptedKeys.contains(customerInfoKey)
    }


}
