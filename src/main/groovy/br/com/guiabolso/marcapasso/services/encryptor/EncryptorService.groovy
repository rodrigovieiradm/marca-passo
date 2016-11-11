package br.com.guiabolso.marcapasso.services.encryptor

interface EncryptorService {
    public String getEncryptedValue(String decryptedText)

    public String getDecryptedValue(String encryptedText)
}
