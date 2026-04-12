# 🛠️ Documentação Técnica - Nexus Social API

Este documento descreve a infraestrutura técnica e as escolhas de design feitas para garantir que a **Nexus Social API** seja resiliente, testável e fácil de manter. O foco aqui é demonstrar como o sistema resolve problemas de consistência de dados e automação.

---

## 1. Stack & Ambiente
* **Linguagem:** Java 17 (LTS) - Utilizando record types e melhorias de performance.
* **Framework:** Spring Boot 3.5.11.
* **Persistência:** Spring Data JPA com Hibernate.
* **Containerização:** Docker & Docker Compose (Isolamento total de ambiente).
* **Ambiente de Dev:** WSL 2 (Windows Subsystem for Linux) para máxima performance de I/O.
* **Banco de Dados:** PostgreSQL 15 (Utilizado tanto em Dev quanto em Produção via containers).
    * **Testes:** H2 Database (In-memory) para isolamento total.
* **Validação:** Jakarta Bean Validation (Hibernate Validator).
* **Testes:** JUnit 5 e Mockito para testes de integração e unitários.
* **Validação da API:** Postman para testes manuais de endpoints
* **Documentação:** SpringDoc OpenAPI (Swagger).
* **CI/CD:** GitHub Actions para automação de build e execução do ecossistema de testes.

---

## 2. Arquitetura em Camadas e Padrões
A API segue o padrão de **Camadas (Layered Architecture)**, garantindo o desacoplamento para separação de responsabilidades e a facilidade na manutenção:

1.  **Controller:** Porta de entrada da aplicação, responsável pelo gerenciamento de endpoints e contratos HTTP via DTOs.
2.  **Service:** Concentração da lógica de negócio, gerenciando regras e garantindo controle transacional das operações.
3.  **Repository:** Camada de persistência de dados que utiliza Spring Data para comunicação com o banco de dados.
4.  **DTO (Data Transfer Objects):** Utilização de `records` para transferência de dados, protegendo as entidades de domínio e evitando exposição desnecessária de IDs ou senhas.

---

## 3. Modelagem de Dados e Integridade
O sistema foi projetado para manter uma consistência rígida entre as entidades, utilizando o diagrama de classes abaixo como referência:

> ![Diagrama de Classes](./images/Class%20DiagramV1.png)
> **Relacionamentos Complexos:**
> * **Institution ↔ Address:** Relacionamento `@OneToOne` com chave estrangeira única.
> * **Necessity ↔ Institution:** Relacionamento `@ManyToOne`, onde uma necessidade não pode existir sem uma instituição vinculada.
> * **Donation ↔ Necessity:** Relacionamento `@ManyToOne` que alimenta o campo `reachedQuantity` da necessidade em tempo real.

---

## 4. Padronização e Tratamento de Erros
A API foi projetada para ser previsível. Utilizando um `@ControllerAdvice` para interceptar exceções e garantir que o cliente receba **Status Codes** consistentes:

* **201 Created:** Retornado em cadastros bem-sucedidos (`Institution`, `Necessity`, `Donor`, `Donation`, `Address`).
* **204 No Content:** Utilizado em deleções confirmadas.
* **400 Bad Request:** Acionado pelo **Bean Validation** quando campos obrigatórios (como valores nulos) são enviados.
* **404 Not Found:** Retornado quando uma busca por ID não encontra registros no banco (Ex: busca de necessidade inexistente).

---

## 5. Estratégia de Testes (Quality Assurance)
A confiabilidade do Nexus Social API é garantida por um ecossistema de testes de integração que validam o fluxo de dados de ponta a ponta assegurando que as regras de negócio e as restrições de banco de dados sejam respeitadas.

### 🧪 Diferenciais Técnicos nos Testes:
* **Testes Unitários (Service Layer):** Foco na validação isolada das regras de negócio. Utilizando Mockito para simular o comportamento dos repositórios, garantindo que a lógica (como o processamento de doações) esteja correta sem depender de conexões externas.
   * **Destaque:** Validação da transição automática de estados, garantindo que uma Necessity mude seu status (ex: de OPEN para FULFILLED) assim que a meta de doações é atingida.
* **Testes de Integração (End-to-End):** Validação do fluxo completo (Controller -> Service -> Repository), garantindo que os contratos HTTP e a persistência no banco estejam íntegros
* **Isolamento com H2:** Uso do perfil `@ActiveProfiles("test")` para garantir execuções rápidas, voláteis e independentes do ambiente de desenvolvimento.
* **MockMvc & JacksonTester:** Validação rigorosa de contratos JSON e códigos de status HTTP (201, 204, 400, 404).
* **Gerenciamento de Constraints:** Implementação do `@BeforeEach` respeitando a **hierarquia de integridade referencial**.
* **Validação de Estado Persistido:** Em cenários de `PUT`, o teste consulta o `Repository` após a requisição para confirmar se o dado foi realmente gravado com os valores corretos.
* **CI/CD:** Integração com **GitHub Actions** para execução automática de testes a cada commit.

---

## 6. Fluxos de Negócio 

| Operação | Endpoint | Validação Principal | Status Esperado |
| :--- | :--- | :--- | :--- |
| **Cadastro Instituição** | `POST /institutions` | Validação de CNPJ e Address obrigatório. | `201 Created` |
| **Gestão de Necessidade** | `POST /necessities` | Cálculo automático de `createdAt` e status inicial `OPEN`. | `201 Created` |
| **Atualização de Progresso** | `PUT /necessities/{id}` | Validação de estado persistido pós-update (Assert do Banco). | `200 OK` |
| **Remoção de Registro** | `DELETE /necessities/{id}` | Verificação de integridade referencial antes da exclusão. | `204 No Content` |
| **Registro de Doação** | `POST /donations` | Incremento atômico de quantidade na necessidade vinculada. | `201 Created` |

---

## 7. Execução e Comandos Úteis

Para facilitar a replicação do ambiente e garantir a qualidade do código, utilize os comandos abaixo centralizados no terminal (preferencialmente WSL2/Linux).

### 🐳 Ambiente Docker (Recomendado)
Para subir toda a infraestrutura (API + PostgreSQL) de forma isolada:
```bash
# Sobe os serviços e reconstrói a imagem da API
docker compose up --build
