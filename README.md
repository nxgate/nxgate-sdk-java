# NXGATE PIX SDK para Java

SDK oficial para integração com a API NXGATE PIX em Java. Suporta operações de cash-in (cobrança via QR Code), cash-out (saque PIX), consulta de saldo, consulta de transações e processamento de webhooks.

## Requisitos

- **Java 11** ou superior
- **Maven 3.6+**

## Instalação

### Maven

Adicione a dependência ao seu `pom.xml`:

```xml
<dependency>
    <groupId>com.nxgate</groupId>
    <artifactId>nxgate-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Build local

```bash
git clone https://github.com/nxgate/nxgate-sdk-java.git
cd nxgate-sdk-java
mvn clean install
```

## Configuração

### Configuração mínima

```java
import com.nxgate.sdk.NXGate;

NXGate nx = NXGate.create(NXGate.builder()
    .clientId("nxgate_xxx")
    .clientSecret("sua_secret")
    .build());
```

### Configuração com HMAC (opcional)

Quando o `hmacSecret` é fornecido, todas as requisições são automaticamente assinadas com HMAC-SHA256:

```java
NXGate nx = NXGate.create(NXGate.builder()
    .clientId("nxgate_xxx")
    .clientSecret("sua_secret")
    .hmacSecret("seu_hmac_secret")
    .build());
```

### Configuração completa

```java
NXGate nx = NXGate.create(NXGate.builder()
    .clientId("nxgate_xxx")
    .clientSecret("sua_secret")
    .hmacSecret("seu_hmac_secret")       // opcional
    .baseUrl("https://api.nxgate.com.br") // padrão
    .maxRetries(2)                        // padrão: 2 tentativas em erro 503
    .connectTimeoutMs(10000)              // padrão: 10s
    .requestTimeoutMs(30000)              // padrão: 30s
    .build());
```

## Uso

### Gerar Cobrança PIX (Cash-in)

```java
import com.nxgate.sdk.model.PixGenerateRequest;
import com.nxgate.sdk.model.PixGenerateResponse;

PixGenerateResponse charge = nx.pixGenerate(PixGenerateRequest.builder()
    .valor(100.00)
    .nomePagador("João da Silva")
    .documentoPagador("12345678901")
    .emailPagador("joao@email.com")          // opcional
    .celular("11999998888")                   // opcional
    .descricao("Pagamento de serviço")        // opcional
    .webhook("https://meusite.com/webhook")   // opcional
    .magicId("pedido_123")                    // opcional
    .build());

System.out.println("ID da transação: " + charge.getIdTransaction());
System.out.println("Código PIX: " + charge.getPaymentCode());
System.out.println("QR Code Base64: " + charge.getPaymentCodeBase64());
```

#### Com Split de Pagamento

```java
import com.nxgate.sdk.model.PixGenerateRequest.SplitUser;
import java.util.Arrays;

PixGenerateResponse charge = nx.pixGenerate(PixGenerateRequest.builder()
    .valor(200.00)
    .nomePagador("Maria Souza")
    .documentoPagador("98765432100")
    .splitUsers(Arrays.asList(
        new SplitUser("usuario1", 60.0),
        new SplitUser("usuario2", 40.0)
    ))
    .build());
```

### Saque PIX (Cash-out)

```java
import com.nxgate.sdk.model.PixWithdrawRequest;
import com.nxgate.sdk.model.PixWithdrawResponse;
import com.nxgate.sdk.model.PixKeyType;

PixWithdrawResponse withdrawal = nx.pixWithdraw(PixWithdrawRequest.builder()
    .valor(50.0)
    .chavePix("joao@email.com")
    .tipoChave(PixKeyType.EMAIL)
    .documento("12345678901")                  // opcional
    .webhook("https://meusite.com/webhook")    // opcional
    .magicId("saque_456")                      // opcional
    .build());

System.out.println("Status: " + withdrawal.getStatus());
System.out.println("Referência: " + withdrawal.getInternalReference());
```

### Tipos de Chave PIX

```java
PixKeyType.CPF     // CPF
PixKeyType.CNPJ    // CNPJ
PixKeyType.PHONE   // Telefone
PixKeyType.EMAIL   // E-mail
PixKeyType.RANDOM  // Chave aleatória
```

### Consultar Saldo

```java
import com.nxgate.sdk.model.BalanceResponse;

BalanceResponse balance = nx.getBalance();

System.out.println("Saldo total: " + balance.getBalance());
System.out.println("Bloqueado: " + balance.getBlocked());
System.out.println("Disponível: " + balance.getAvailable());
```

### Consultar Transação

```java
import com.nxgate.sdk.model.TransactionResponse;

// Consultar transação cash-in
TransactionResponse tx = nx.getTransaction("cash-in", "txid_abc123");

System.out.println("Status: " + tx.getStatus());
System.out.println("Valor: " + tx.getAmount());
System.out.println("Pago em: " + tx.getPaidAt());
System.out.println("End-to-end: " + tx.getEndToEnd());
```

### Processar Webhooks

```java
import com.nxgate.sdk.NXGateWebhook;
import com.nxgate.sdk.model.WebhookEvent;
import com.nxgate.sdk.model.CashInWebhookEvent;
import com.nxgate.sdk.model.CashOutWebhookEvent;

// Em seu endpoint de webhook
String jsonBody = /* corpo da requisição HTTP */;

WebhookEvent event = NXGateWebhook.parse(jsonBody);

if (event.isCashIn()) {
    CashInWebhookEvent cashIn = event.asCashIn();
    System.out.println("Tipo: " + cashIn.getType());
    System.out.println("Valor: " + cashIn.getData().getAmount());
    System.out.println("Status: " + cashIn.getData().getStatus());
    System.out.println("TX ID: " + cashIn.getData().getTxId());
    System.out.println("Pagador: " + cashIn.getData().getDebtorName());
    System.out.println("Magic ID: " + cashIn.getData().getMagicId());

} else if (event.isCashOut()) {
    CashOutWebhookEvent cashOut = event.asCashOut();
    System.out.println("Tipo: " + cashOut.getType());
    System.out.println("Valor: " + cashOut.getAmount());
    System.out.println("Chave: " + cashOut.getKey());
    System.out.println("ID Transação: " + cashOut.getIdTransaction());

    if (cashOut.getError() != null) {
        System.out.println("Erro: " + cashOut.getError());
    }
}
```

#### Tipos de Evento Webhook

**Cash-in:**
| Tipo | Descrição |
|------|-----------|
| `QR_CODE_COPY_AND_PASTE_PAID` | Pagamento PIX confirmado |
| `QR_CODE_COPY_AND_PASTE_REFUNDED` | Pagamento PIX estornado |

**Cash-out:**
| Tipo | Descrição |
|------|-----------|
| `PIX_CASHOUT_SUCCESS` | Saque PIX realizado com sucesso |
| `PIX_CASHOUT_ERROR` | Erro no saque PIX |
| `PIX_CASHOUT_REFUNDED` | Saque PIX estornado |

## Tratamento de Erros

Todas as operações lançam `NXGateException` em caso de erro:

```java
import com.nxgate.sdk.NXGateException;

try {
    PixGenerateResponse charge = nx.pixGenerate(request);
} catch (NXGateException e) {
    System.out.println("Código HTTP: " + e.getCode());
    System.out.println("Título: " + e.getTitle());
    System.out.println("Descrição: " + e.getDescription());
    System.out.println("Status: " + e.getStatus());
}
```

## Funcionalidades

- **Gerenciamento automático de token**: O SDK obtém e renova o token OAuth2 automaticamente, com cache e renovação 60 segundos antes da expiração.
- **Assinatura HMAC**: Quando `hmacSecret` é configurado, todas as requisições incluem automaticamente os headers HMAC-SHA256.
- **Retry automático**: Requisições que retornam HTTP 503 são automaticamente retentadas com exponential backoff (1s, 2s, ...) até o limite configurado.
- **Validação de campos**: Os builders validam campos obrigatórios antes de enviar a requisição.
- **Zero dependências externas**: O SDK utiliza apenas `java.net.http.HttpClient` (Java 11+) e Gson para serialização JSON.

## Assinatura HMAC

Quando o `hmacSecret` é fornecido na configuração, o SDK adiciona automaticamente os seguintes headers em todas as requisições:

| Header | Descrição |
|--------|-----------|
| `X-Client-ID` | ID do cliente |
| `X-HMAC-Signature` | Assinatura HMAC-SHA256 em Base64 |
| `X-HMAC-Timestamp` | Timestamp ISO 8601 da requisição |
| `X-HMAC-Nonce` | UUID único por requisição |

O payload assinado segue o formato:
```
METHOD\nPATH\nTIMESTAMP\nNONCE\nBODY
```

## Executando os Testes

```bash
mvn test
```

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).
