package br.com.guiabolso.marcapasso.services.encryptor

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.kms.AWSKMSClient
import com.amazonaws.services.kms.model.DecryptRequest
import com.amazonaws.services.kms.model.EncryptRequest
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.nio.ByteBuffer
import java.nio.charset.Charset

@Slf4j
@Component
class KMSEncryptorService implements EncryptorService {

    private final AWSKMSClient kmsClient
    private final KMSConfig kmsConfig

    @Autowired
    KMSEncryptorService(KMSConfig kmsConfig) {
        this.kmsConfig = kmsConfig

        this.kmsClient = new AWSKMSClient(new DefaultAWSCredentialsProviderChain())
        kmsClient.setEndpoint(kmsConfig.endpoint)
    }

    @Override
    public String getEncryptedValue(String decryptedValue) {

        if (decryptedValue.length() < 1)
            return ""

        try {
            String encryptedValue = new String(Base64.encoder.encode(this.kmsEncrypt(decryptedValue).array()))
            return encryptedValue
        } catch (Exception e) {
            log.error("Error trying to crypt value", e)
            throw e
        }
    }

    @Override
    public String getDecryptedValue(String encryptedValue) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.decoder.decode(encryptedValue.getBytes()))
            String decryptedValue = this.kmsDecrypt(byteBuffer)
            return decryptedValue
        } catch (Exception e) {
            log.error("Error trying to decrypt value", e)
            throw e
        }
    }

    private ByteBuffer kmsEncrypt(String value) {
        ByteBuffer plaintext = ByteBuffer.wrap(value.getBytes(Charset.forName("UTF-8")))
        EncryptRequest req = new EncryptRequest().withKeyId(kmsConfig.keyId).withPlaintext(plaintext)
        ByteBuffer ciphertextBlob = kmsClient.encrypt(req).getCiphertextBlob()
        return ciphertextBlob
    }

    private String kmsDecrypt(ByteBuffer ciphertextBlob) {
        DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
        ByteBuffer plainText = kmsClient.decrypt(req).getPlaintext()
        return new String(plainText.array(), Charset.forName("UTF-8"))
    }

}
