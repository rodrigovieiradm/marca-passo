package br.com.guiabolso.marcapasso.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerInfoData {
    String key
    String value
    String type
    Date createdAt
    Date updatedAt
}