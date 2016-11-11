package br.com.guiabolso.marcapasso.services.customerInfo

interface CustomerInfoService {
    Long create(Long userId, Map<String, Object> customerInfo)
}