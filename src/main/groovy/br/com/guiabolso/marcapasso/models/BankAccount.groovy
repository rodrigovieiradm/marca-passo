package br.com.guiabolso.marcapasso.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class BankAccount {
    Integer financialEntity
    String agency
    String accountNumber
    String cpf
    String accountDigit
    String bacenCode
}