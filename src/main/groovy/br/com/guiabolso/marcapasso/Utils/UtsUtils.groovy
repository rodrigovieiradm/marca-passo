package br.com.guiabolso.marcapasso.Utils

import br.com.guiabolso.marcapasso.models.BankAccount
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j

@Slf4j
class UtsUtils {

    static List<BankAccount> getBankAccountsByUts(Map<String, Object> uts) {
        List<BankAccount> bankAccounts = new ArrayList<>()

        int accountsSize = uts.getOrDefault("USER.ACCOUNTS.LENGTH", 0) as int

        for (int i = 0; i < accountsSize; ++i) {

            int bank = uts.getOrDefault("USER.ACCOUNTS[" + i + "].BANK", 1000) as int
            String bacenCode = uts.getOrDefault("USER.ACCOUNTS[" + i + "].BACENCODE", "") as String
            String accountAgency = uts.getOrDefault("USER.ACCOUNTS[" + i + "].AGENCY", "") as String
            String accountNumber = uts.getOrDefault("USER.ACCOUNTS[" + i + "].ACCOUNTNUMBER", "") as String
            String accountDigit = uts.getOrDefault("USER.ACCOUNTS[" + i + "].ACCOUNTDIGIT", "") as String

            String accountCPF = uts.getOrDefault("USER.ACCOUNTS[" + i + "].CPF", "") as String

            bankAccounts.add(new BankAccount(
                    financialEntity: bank,
                    agency: accountAgency,
                    accountNumber: accountNumber,
                    cpf: accountCPF,
                    accountDigit: accountDigit,
                    bacenCode: bacenCode
            ))

        }

        return bankAccounts
    }

    static Map<String, Object> getCustomerInfoMapByUTS(Map<String, Object> uts) {
        Map<String, Object> customerInfo = new HashMap<>()

        customerInfo.put('COSTUMER_CONTACT_EMAIL', uts.getOrDefault("USER.EMAIL", ""))

        List<BankAccount> bankAccounts = getBankAccountsByUts(uts)
        if (bankAccounts.size() > 0) {
            List<BankAccount> accountsWithCpf = bankAccounts.findAll {
                it.cpf != null && it.cpf != ""
            }
            if (accountsWithCpf && accountsWithCpf.size() > 0) {
                customerInfo.put('COSTUMER_DOC_CPF', accountsWithCpf.first().cpf)
            }
        }

        String fullName = uts.getOrDefault("BOAVISTA.IDENTIFICACAO.NOME", "")
        if (fullName != "") {
            customerInfo.put('COSTUMER_DOC_FULL_NAME', fullName)
        }

        customerInfo.put('COSTUMER_CONTRACT_FIRST_PAYMENT_DATE', uts.getOrDefault("GUIABOLSO.DIA_DO_SALARIO#", 6))

        double costumerSalary = uts.getOrDefault("GUIABOLSO.SALARIO_MEDIA", -1D) as double
        if (costumerSalary > 0) {
            customerInfo.put('COSTUMER_SALARY', costumerSalary)
        }

        boolean hasCar = uts.getOrDefault("GUIABOLSO.TEM_CARRO?", 0) > 0
        customerInfo.put('VAR_HASCAR', hasCar)

        double valorMaximoSugeridoParaEmprestimo = uts.getOrDefault("GUIABOLSO.VALOR_MAXIMO_SUGERIDO_PARA_EMPRESTIMO", 0D) as double
        customerInfo.put('VALOR_MAXIMO_SUGERIDO_PARA_EMPRESTIMO', valorMaximoSugeridoParaEmprestimo)

        long prazoSugeridoParaEmprestimo = uts.getOrDefault("GUIABOLSO.PRAZO_SUGERIDO_PARA_EMPRESTIMO", 0l) as long
        customerInfo.put('PRAZO_SUGERIDO_PARA_EMPRESTIMO', prazoSugeridoParaEmprestimo)

        double menorSaldo30Dias = uts.getOrDefault("GUIABOLSO.SALDO_MAIS_BAIXO.1", 0D) as double
        customerInfo.put('MENOR_SALDO_30_DIAS', menorSaldo30Dias)

        double entradaRecorrenteMedia = uts.getOrDefault("GUIABOLSO.ENTRADAS_RECORRENTES_MEDIA", 0) as double
        customerInfo.put('ENTRADAS_RECORRENTES_MEDIA', entradaRecorrenteMedia)

        double saldoAtual = uts.getOrDefault("GUIABOLSO.SALDO.1", 0) as double
        customerInfo.put('SALDO_ATUAL', saldoAtual)

        double bvValorTotalRegistroDebito = uts.getOrDefault("BOAVISTA.REGISTRO_DEBITOS.RESUMO.VALOR_TOTAL", 0) as double
        double bvValorTotalProtesto = uts.getOrDefault("BOAVISTA.PROTESTOS.RESUMO.VALOR_TOTAL", 0) as double
        boolean bvNegativo = (bvValorTotalRegistroDebito + bvValorTotalProtesto) > 0
        customerInfo.put('BV_NEGATIVO', bvNegativo)

        double income = uts.getOrDefault("GUIABOLSO.ENTRADA_REAL_MEDIA", 0D) as double
        customerInfo.put('COSTUMER_INCOME', income)

        log.info("CustumerInfo=${customerInfo}")

        return customerInfo
    }

    static String getCustomerInfoJsonByUts(Map<String, Object> uts){
        Map customerInfo = getCustomerInfoMapByUTS(uts)
        return new JsonBuilder(customerInfo).toPrettyString()
    }

}
