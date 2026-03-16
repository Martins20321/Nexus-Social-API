# 🛠️ Documentação Técnica - Nexus Social API

## 1. Visão Geral
Esta API foi desenvolvida para conectar doadores a instituições sociais, permitindo a gestão de necessidades em tempo real e o rastreamento de doações.

## 2. Arquitetura de Dados
O sistema utiliza o mapeamento objeto-relacional (ORM - Object Relational Mapping) (JPA/Hibernate) seguindo o diagrama de classes abaixo:

![Diagrama de Classes](./images/Class%20Diagram0.png)

## 3. Entidades e Regras de Negócio

### 🏢 Institution (Instituição)
- **Regra:** O CNPJ deve ser único e validado antes da persistência.
- **Relacionamento:** Possui um `Address` (1:1) e várias `Necessity` (1:N).

### 🆘 Necessity (Necessidade)
- **Status:** `OPEN`, `PARTIAL`, `FULFILLED`, `CLOSED`.
- **Regra:** Quando `reachedQuantity` for igual ou superior a `requiredQuantity`, o status deve ser alterado automaticamente para `FULFILLED`.

### 🎁 Donation (Doação)
- **Regra:** Registra o instante exato (`moment`) da doação.
- **Fluxo:** Cada doação incrementa o campo `reachedQuantity` da necessidade vinculada.

## 4. Planejamento de Endpoints (Fase 1)
| Verbo | Endpoint | Descrição |
| :--- | :--- | :--- |
| POST | `/api/institutions` | Cadastro de nova ONG |
| GET | `/api/necessities` | Listagem de itens necessários |
| POST | `/api/donations` | Registro de contribuição |

## 5. Tecnologias
- **Java 17 / Spring Boot 3**
- **PostgreSQL**
- **Hibernate (JPA)**
- **Checkstyle (Google Checks)**
