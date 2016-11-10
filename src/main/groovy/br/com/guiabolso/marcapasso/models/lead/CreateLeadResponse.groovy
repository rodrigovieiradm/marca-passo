package br.com.guiabolso.marcapasso.models.lead

import br.com.guiabolso.marcapasso.models.BankAccount

class CreateLeadResponse {
    String id
    Map<String, Object> customerInfo
    Date createdAt
    Integer status
    String utsId
    Date lastUpdate
    String apiClientUniqueId
    Long userId
    String adId
    Long recommendationId
    List<BankAccount> bankAccounts
}
