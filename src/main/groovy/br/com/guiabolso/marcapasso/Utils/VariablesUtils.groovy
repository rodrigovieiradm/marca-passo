package br.com.guiabolso.marcapasso.Utils

import br.com.guiabolso.marcapasso.models.BankAccount
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j

@Slf4j
class VariablesUtils {

    static List<BankAccount> getBankAccountsByVariables(Map<String, Object> variables) {
        List<BankAccount> bankAccounts = new ArrayList<>()

        int accountsSize = variables.getOrDefault("USER.ACCOUNTS.LENGTH", 0) as int

        for (int i = 0; i < accountsSize; ++i) {

            int bank = variables.getOrDefault("USER.ACCOUNTS[" + i + "].BANK", 1000) as int
            String bacenCode = variables.getOrDefault("USER.ACCOUNTS[" + i + "].BACENCODE", "") as String
            String accountAgency = variables.getOrDefault("USER.ACCOUNTS[" + i + "].AGENCY", "") as String
            String accountNumber = variables.getOrDefault("USER.ACCOUNTS[" + i + "].ACCOUNTNUMBER", "") as String
            String accountDigit = variables.getOrDefault("USER.ACCOUNTS[" + i + "].ACCOUNTDIGIT", "") as String

            String accountCPF = variables.getOrDefault("USER.ACCOUNTS[" + i + "].CPF", "") as String

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

    static Map<String, Object> getCustomerInfoMapByVariables(Map<String, Object> variables) {
        Map<String, Object> customerInfo = new HashMap<>()

        customerInfo.put('COSTUMER_CONTACT_EMAIL', getUserEmailByVariables(variables))

        String cpf = getUserCpfByVariables(variables)
        if (cpf != "") {
            customerInfo.put('COSTUMER_DOC_CPF', cpf)
        }

        String fullName = getUserFullNameByVariables(variables)
        if (fullName != "") {
            customerInfo.put('COSTUMER_DOC_FULL_NAME', fullName)
        }

        customerInfo.put('COSTUMER_CONTRACT_FIRST_PAYMENT_DATE', variables.getOrDefault("GUIABOLSO.DIA_DO_SALARIO#", 6))

        double costumerSalary = variables.getOrDefault("GUIABOLSO.SALARIO_MEDIA", -1D) as double
        if (costumerSalary > 0) {
            customerInfo.put('COSTUMER_SALARY', costumerSalary)
        }

        boolean hasCar = variables.getOrDefault("GUIABOLSO.TEM_CARRO?", 0) > 0
        customerInfo.put('VAR_HASCAR', hasCar)

        double valorMaximoSugeridoParaEmprestimo = variables.getOrDefault("GUIABOLSO.VALOR_MAXIMO_SUGERIDO_PARA_EMPRESTIMO", 0D) as double
        customerInfo.put('VALOR_MAXIMO_SUGERIDO_PARA_EMPRESTIMO', valorMaximoSugeridoParaEmprestimo)

        long prazoSugeridoParaEmprestimo = variables.getOrDefault("GUIABOLSO.PRAZO_SUGERIDO_PARA_EMPRESTIMO", 0l) as long
        customerInfo.put('PRAZO_SUGERIDO_PARA_EMPRESTIMO', prazoSugeridoParaEmprestimo)

        double menorSaldo30Dias = variables.getOrDefault("GUIABOLSO.SALDO_MAIS_BAIXO.1", 0D) as double
        customerInfo.put('MENOR_SALDO_30_DIAS', menorSaldo30Dias)

        double entradaRecorrenteMedia = variables.getOrDefault("GUIABOLSO.ENTRADAS_RECORRENTES_MEDIA", 0) as double
        customerInfo.put('ENTRADAS_RECORRENTES_MEDIA', entradaRecorrenteMedia)

        double saldoAtual = variables.getOrDefault("GUIABOLSO.SALDO.1", 0) as double
        customerInfo.put('SALDO_ATUAL', saldoAtual)

        double bvValorTotalRegistroDebito = variables.getOrDefault("BOAVISTA.REGISTRO_DEBITOS.RESUMO.VALOR_TOTAL", 0) as double
        double bvValorTotalProtesto = variables.getOrDefault("BOAVISTA.PROTESTOS.RESUMO.VALOR_TOTAL", 0) as double
        boolean bvNegativo = (bvValorTotalRegistroDebito + bvValorTotalProtesto) > 0
        customerInfo.put('BV_NEGATIVO', bvNegativo)

        double income = variables.getOrDefault("GUIABOLSO.ENTRADA_REAL_MEDIA", 0D) as double
        customerInfo.put('COSTUMER_INCOME', income)

        log.info("CustumerInfo=${customerInfo}")

        return customerInfo
    }

    static String getCustomerInfoJsonByVariables(Map<String, Object> variables) {
        Map customerInfo = getCustomerInfoMapByVariables(variables)
        return new JsonBuilder(customerInfo).toPrettyString()
    }

    static String getUserEmailByVariables(Map<String, Object> variables) {
        return variables.getOrDefault("USER.EMAIL", "")
    }

    static String getUserCpfByVariables(Map<String, Object> variables) {
        String cpf = ""
        List<BankAccount> bankAccounts = getBankAccountsByVariables(variables)
        if (bankAccounts.size() > 0) {
            List<BankAccount> accountsWithCpf = bankAccounts.findAll {
                it.cpf != null && it.cpf != ""
            }
            if (accountsWithCpf && accountsWithCpf.size() > 0) {
                cpf = accountsWithCpf.first().cpf
            }
        }
        return cpf
    }

    static String getUserFullNameByVariables(Map<String, Object> variables) {
        return variables.getOrDefault("BOAVISTA.IDENTIFICACAO.NOME", "")
    }

}