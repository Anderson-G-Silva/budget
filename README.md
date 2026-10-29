## Controle de orçamento pessoal utilizando IA
Desenvolvimento de aplicação (backend) para controle de orçamento pessoal, utilizando IA para caputurar mensagens de voz gravadas pelo usuário da aplicação via interface RestFull para analisar os dados transcritos, persistir em banco de dados, finalizando com uma mensagem ao usuário de confirmação da transação com informações sobre o registro.

## Diagrama de classes

```mermaid
classDiagram
    class TransactionEntity {
        +TransactionId id
        +String description
        +Category category
        +Subcategory subcategory
        +BigDecimal amount
        +LocalDate date
        +LocalDate paymentDate
        +PaymentMethod paymentMethod
        +PaymentBrand paymentBrand
        +UUID userId
        +Instant createdAt
        +void consistency()
    }

    class User {
        +UserId id
        +String username
        +String password
        +String email
        +UserRole role
        +Instant createdAt
        +User(String username, String password, String email, UserRole role)
    }

    User "1" -- "0..*" TransactionEntity : has >

```
## Diagrama de classes (Enum)

```mermaid
classDiagram      
    class Category{
      MORADIA
      CONTAS_DE_CONSUMO
      ALIMENTAÇÃO
      TRANSPORTE
      SAÚDE
      EDUCAÇÃO
      VESTUÁRIO
      LAZER
      CUIDADOS_PESSOAIS
      FAMÍLIA
      PETS
      FINANCEIRO
      IMPOSTOS
      DOAÇÕES_E_PRESENTES
      OUTROS
    }
    
    class Subcategory{
      ALUGUEL(Category.MORADIA)
      CONDOMÍNIO(Category.MORADIA)
      IPTU(Category.MORADIA)
      FINANCIAMENTO_IMOBILIÁRIO(Category.MORADIA)
      MANUTENÇÃO_E_REPAROS(Category.MORADIA)
      SEGURO_RESIDENCIAL(Category.MORADIA)
      ENERGIA_ELÉTRICA(Category.CONTAS_DE_CONSUMO)
      ÁGUA_E_ESGOTO(Category.CONTAS_DE_CONSUMO)
      GÁS(Category.CONTAS_DE_CONSUMO)
      INTERNET(Category.CONTAS_DE_CONSUMO)
      TELEFONIA(Category.CONTAS_DE_CONSUMO)
      TV_POR_ASSINATURA_STREAMING(Category.CONTAS_DE_CONSUMO)
      SUPERMERCADO(Category.ALIMENTAÇÃO)
      FEIRA_HORTIFRUTI(Category.ALIMENTAÇÃO)
      RESTAURANTES_E_DELIVERY(Category.ALIMENTAÇÃO)
      LANCHES_E_CAFÉS(Category.ALIMENTAÇÃO)
      COMBUSTÍVEL(Category.TRANSPORTE)
      ESTACIONAMENTO_E_PEDÁGIO(Category.TRANSPORTE)
      TRANSPORTE_PÚBLICO(Category.TRANSPORTE)
      APLICATIVOS_DE_TRANSPORTE_UBER_99(Category.TRANSPORTE)
      MANUTENÇÃO_DO_VEÍCULO(Category.TRANSPORTE)
      SEGURO_DO_VEÍCULO(Category.TRANSPORTE)
      IPVA_LICENCIAMENTO(Category.TRANSPORTE)
      FINANCIAMENTO_DO_VEÍCULO(Category.TRANSPORTE)
      PLANO_DE_SAÚDE(Category.SAÚDE)
      MEDICAMENTOS(Category.SAÚDE)
      CONSULTAS_E_EXAMES(Category.SAÚDE)
      DENTISTA(Category.SAÚDE)
      ACADEMIA_ATIVIDADE_FÍSICA(Category.SAÚDE)
      MENSALIDADE_ESCOLAR_FACULDADE(Category.EDUCAÇÃO)
      CURSOS_E_CERTIFICAÇÕES(Category.EDUCAÇÃO)
      MATERIAL_DIDÁTICO_E_LIVROS(Category.EDUCAÇÃO)
      IDIOMAS(Category.EDUCAÇÃO)
      ROUPAS(Category.VESTUÁRIO)
      CALÇADOS(Category.VESTUÁRIO)
      ACESSÓRIOS(Category.VESTUÁRIO)
      CINEMA_SHOWS_E_EVENTOS(Category.LAZER)
      VIAGENS(Category.LAZER)
      HOBBIES(Category.LAZER)
      ASSINATURAS_DE_ENTRETENIMENTO(Category.LAZER)
      CABELEIREIRO_BARBEARIA(Category.CUIDADOS_PESSOAIS)
      ESTÉTICA_E_COSMÉTICOS(Category.CUIDADOS_PESSOAIS)
      HIGIENE_PESSOAL(Category.CUIDADOS_PESSOAIS)
      ESCOLA_DOS_FILHOS(Category.FAMÍLIA)
      BABÁ_CRECHE(Category.FAMÍLIA)
      MESADA(Category.FAMÍLIA)
      PENSÃO_ALIMENTÍCIA(Category.FAMÍLIA)
      RAÇÃO_E_PETISCOS(Category.PETS)
      VETERINÁRIO(Category.PETS)
      BANHO_E_TOSA(Category.PETS)
      EMPRÉSTIMOS_E_FINANCIAMENTOS(Category.FINANCEIRO)
      TARIFAS_BANCÁRIAS(Category.FINANCEIRO)
      SEGUROS_DIVERSOS(Category.FINANCEIRO)
      INVESTIMENTOS(Category.FINANCEIRO)
      RESERVA_DE_EMERGÊNCIA(Category.FINANCEIRO)
      IMPOSTO_DE_RENDA(Category.IMPOSTOS)
      CONTRIBUIÇÕES_E_TAXAS(Category.IMPOSTOS)
      DOAÇÕES(Category.DOAÇÕES_E_PRESENTES)
      PRESENTES(Category.DOAÇÕES_E_PRESENTES)
      DESPESAS_DIVERSAS(Category.OUTROS)
    }
    
    class PaymentMethod{
      DÉBITO_PIX
      CRÉDITO
      DINHEIRO
    }
    
    class PaymentBrand{ 
        DINHEIRO(PaymentMethod.DINHEIRO,0,0)
        SANTANDER(PaymentMethod.DÉBITO_PIX,0,0)
        ITAU(PaymentMethod.DÉBITO_PIX,0,0)
        BB(PaymentMethod.DÉBITO_PIX,0,0)
        CEF(PaymentMethod.DÉBITO_PIX,0,0)
        VISA(PaymentMethod.CRÉDITO,25,5)
        MASTERCARD(PaymentMethod.CRÉDITO,5,15)
        ELO(PaymentMethod.CRÉDITO,15,25)

        +int closingInvoiceDate
        +int invoicePaymentDate
    }

  <<enumeration>> Category
  <<enumeration>> Subcategory
  <<enumeration>> PaymentMethod
  <<enumeration>> PaymentBrand
   

```

## Fluxograma

```mermaid
graph TD
%% Nós do Fluxograma
    1([Início]) --> 2[Usuário grava áudio]
    2 --> 3[STT faz a transcrição do áudio para texto]
    3 --> 4A[Aplicação identifica a data da transação e adiciona a transcrição]
    4A --> 4B[ChatClient analisa as informações e identifica dados para a persistência]
    4B --> 5[Aplicação identifica meio de pagamento]

%% Bloco de Decisão
    5 --> 6{Meio de pagamento?}

    6 -- Dinheiro / Débito / Pix --> 6A[Data de pagamento igual à data da transação]
    6 -- Crédito --> 6B[Calcula data de pagamento conforme fechamento e vencimento de cada bandeira]

%% Unificação do Fluxo
    6A --> 7[Faz a persistência dos dados]
    6B --> 7
    7 --> 8[Gera arquivo de voz ao usuário confirmando o registro da transação]
    8 --> 9([Fim])

%% Estilização (Opcional - Visual Clean)
    style 1 fill:#d4edda,stroke:#28a745,stroke-width:2px
    style 6 fill:#fff3cd,stroke:#ffc107,stroke-width:2px
    style 9 fill:#f8d7da,stroke:#dc3545,stroke-width:2px

```

## Linguagens utilizadas
- Java Versão 21
- Spring Boot 4.1.1

## Recursos Utilizados
- Spring Boot Web
- Spring Boot JPA
- Spring Boot Docker-Compose
- Spring Boot Validation
- Spring Boot Actuator
- Spring Boot Admin e Server
- Spring AI model OpenAI
- Spring Boot Security
- Liquibase
- MapStruct

## Referências

- [DIO - dio-spring-boot-learning-track
  /05-spring-ai](https://github.com/thpoiani/dio-spring-boot-learning-track/tree/main/05-spring-ai)


## Autores

- [@Anderson-G-Silva](https://github.com/Anderson-G-Silva)

