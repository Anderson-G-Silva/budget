## Controle de orçamento pessoal utilizando IA
Desenvolvimento de aplicação (backend) para controle de orçamento pessoal, utilizando IA para caputurar mensagens de voz gravadas pelo usuário da aplicação via interface RestFull para analisar os dados transcritos, persistir em banco de dados, finalizando com uma mensagem ao usuário de confirmação da transação com informações sobre o registro.

## Diagrama de classes

```mermaid
classDiagram
    class TransactionEntity {
        +UUID id
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
        -UserId id
        -String username
        -String password
        -String email
        -UserRole role
        -Instant createdAt
        +User(String username, String password, String email, UserRole role)
    }

    User "1" -- "0..*" TransactionEntity : has >

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

