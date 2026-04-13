# NexusSocial API 🌐

![CI/CD](https://github.com/Martins20321/Nexus-Social-API/actions/workflows/ci-cd.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.11-6DB33F?style=flat&logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=flat&logo=docker&logoColor=white)

## 📋 Sobre o Projeto   
O **NexusSocial** é uma solução backend desenhada para conectar doadores e instituições de caridade de forma assertiva. O objetivo principal é eliminar o desencontro de informações, permitindo que ONGs publiquem suas necessidades urgentes e doadores encontrem causas onde seu impacto será imediato.

> **Status do Projeto:** MVP Finalizado / Em expansão.

### 🎯 O Problema Resolvido
Muitas instituições carecem de visibilidade para demandas específicas (medicamentos, alimentos sazonais, itens de higiene). O NexusSocial centraliza essas demandas, permitindo uma logística de solidariedade muito mais eficiente.

---

## 🏗️ Visão Arquitetural

A API foi construída sob os princípios da **Clean Architecture** e **Domain-Driven Design (Lite)**, garantindo que a lógica de negócio seja independente de frameworks e fácil de testar.

### O Fluxo de Dados
1. **Instituições** cadastram necessidades específicas com metas de quantidade.
2. **Doadores** registram contribuições vinculadas a essas necessidades.
3. **Gestão de Ciclo de Vida:** O sistema reavalia o status da demanda automaticamente:
   * `OPEN` ➔ Criada, aguardando doação.
   * `IN_PROGRESS` ➔ Recebendo doações ativamente.
   * `FULFILLED` ➔ Meta atingida. O sistema encerra o recebimento para garantir transparência e evitar desperdício.

---

## 🛠️ Destaques de Engenharia

Neste projeto, apliquei práticas avançadas para garantir a qualidade de software:

* **Arquitetura em Camadas:** Separação clara entre Controllers, Services e Repositories.
* **Testes Automatizados:** Cobertura de testes unitários e de integração utilizando **JUnit 5**, **Mockito** e **MockMvc**.
* **Integridade Referencial:** Modelagem de banco de dados no PostgreSQL com constraints rigorosas para evitar inconsistências de dados.
* **Padronização REST:** Tratamento global de exceções e uso de DTOs (`Records`) para contratos de API limpos.

---

## 📖 Documentação Detalhada
Para entender as decisões de design, padrões de projeto (Layered Architecture, DTOs, Records), modelagem de dados e como a pirâmide de testes foi estruturada, acesse nosso:

👉 [**Guia Técnico de Desenvolvimento (Technical README)**](./docs/technical-docs.md)

---

## 🚀 Como Rodar (Quick Start)

Graças ao uso de **Docker**, você pode subir o ambiente completo (API + Banco de Dados) com apenas um comando:

```bash
# Clone o repositório
git clone https://github.com/Martins20321/Nexus-Social-API.git

# Acesse a pasta
cd Nexus-Social-API

# Suba a infraestrutura (API + Banco)
docker compose up --build
```

### Próximos Passos (Pós Execução)
Após o container subir, você pode interagir com a aplicação:
* **Acessar a Documentação:** Abra http://localhost:8080/swagger-ui.html para explorar e testar os endpoints via Swagger.
* **Monitorar Logs:** Utilize `docker logs -f nexus-api` para acompanhar o comportamento da aplicação em tempo real.
* **Encerrar o Ambiente:** Use `docker compose down` para parar os serviços e remover os containers de forma segura.

---

## 👤 Autor 
**José Gabriel Martins**

🔗 **Repositório Público:** https://github.com/Martins20321/Nexus-Social-API
