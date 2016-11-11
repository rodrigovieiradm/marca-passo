package br.com.guiabolso.marcapasso.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Offer {
    String id
    String financialEntity
    String url
}