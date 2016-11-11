package br.com.guiabolso.marcapasso.services.encryptor

import groovy.transform.ToString
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@ToString(includeNames = true)
@Component
public class KMSConfig {

    @Value('${kms.endpoint}')
    String endpoint

    @Value('${kms.keyId}')
    String keyId
}
