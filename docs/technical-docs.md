# 🛠️ Documentação Técnica - Nexus Social API

Este documento descreve a infraestrutura técnica e as escolhas de design feitas para garantir que a **Nexus Social API** seja resiliente, testável e fácil de manter. O foco aqui é demonstrar como o sistema resolve problemas de consistência de dados e automação.

---

## 1. Stack & Ambiente
* **Linguagem:** Java 17 (LTS) - Utilizando record types e melhorias de performance.
* **Framework:** Spring Boot 3.5.11.
* **Persistência:** Spring Data JPA com Hibernate.
* **Banco de Dados:** * **Produção/Dev:** PostgreSQL.
    * **Testes:** H2 Database (In-memory) para isolamento total.
* **Validação:** Jakarta Bean Validation (Hibernate Validator).
* **Testes:**
* **Validação da API:** Postman para testes manuais de endpoints
* **Documentação:** SpringDoc OpenAPI (Swagger).
* **CI/CD:** GitHub Actions para automação de build e execução de suíte de testes.

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
Atualmente, o projeto prioriza a validação do fluxo completo (Controller -> Service -> Repository), garantindo que a comunicação com o banco de dados e os contratos HTTP estejam íntegros.
* **Isolamento com H2:** Uso do perfil `@ActiveProfiles("test")` para garantir execuções rápidas, voláteis e independentes do ambiente de desenvolvimento.
* **MockMvc & JacksonTester:** Validação rigorosa de contratos JSON e códigos de status HTTP (201, 204, 400, 404).
* **Gerenciamento de Constraints:** Implementação do `@BeforeEach` respeitando a **hierarquia de integridade referencial**.
* **Validação de Estado Persistido:** Em cenários de `PUT`, o teste consulta o `Repository` após a requisição para confirmar se o dado foi realmente gravado com os valores corretos.
* **CI/CD:** Integração com **GitHub Actions** para execução automática de testes a cada commit.

* > *Nota: A implementação de testes unitários isolados para a camada de Service está em desenvolvimento*

---

## 6. Fluxos de Negócio Implementados via API

| Operação | Endpoint | Validação Principal |
| :--- | :--- | :--- |
| **Cadastro Instituição** | `POST /institutions` | Validação de CNPJ e Address obrigatório. |
| **Gestão de Necessidade** | `POST /necessities` | Cálculo automático de `createdAt` e `status` inicial. |
| **Atualização de Progresso** | `PUT /necessities/{id}` | Validação de estado persistido pós-update (Assert do Banco). |
| **Registro de Doação** | `POST /donations` | Incremento atômico de quantidade na Necessity vinculada. |

---

## 7. Como Rodar os Testes Técnicos
Para validar a integridade do projeto e garantir que o pipeline de CI passará com sucesso:

```bash
# Executar todos os testes de integração
./mvnw test

# Executar uma classe específica (Ex: Necessity)
./mvnw test -Dtest=NecessityControllerTest

