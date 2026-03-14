# NexusSocial API 🌐

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/SEU_USUARIO/nexus-social-api/ci.yml?branch=main&style=for-the-badge)
![Version](https://img.shields.io/badge/version-1.0.0-blue?style=for-the-badge)
![Java](https://img.shields.io/badge/java-17-orange?style=for-the-badge)

## 📋 Sobre o Projeto
O **NexusSocial** é uma solução backend desenvolvida para resolver uma dor crítica na logística de solidariedade: o desencontro de informações entre doadores e instituições de caridade locais. 

Muitas vezes, pequenas ONGs e centros comunitários possuem necessidades urgentes (como falta de um medicamento específico ou mantimentos para a semana) que não chegam ao conhecimento do público. Esta API permite que instituições cadastrem suas demandas em tempo real, permitindo que doadores encontrem onde sua ajuda terá o maior impacto imediato.

> **Versão Atual:** `1.0.0` (Seguindo o padrão de Versionamento Semântico [SemVer](https://semver.org/lang/pt-BR/)).

### 🎯 Público-Alvo
* **Instituições Sociais:** Gestores de ONGs e abrigos.
* **Doadores:** Pessoas físicas ou empresas que buscam destinar recursos de forma assertiva.

---

### 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia | Detalhe |
| :--- | :--- | :--- |
| Framework | Spring Boot 3.x | Base da aplicação REST. |
| Linguagem | Java 17 | Linguagem principal do projeto. |
| Persistência | Spring Data JPA | Gerenciamento do banco de dados. |
| Banco de Dados | PostgreSQL | Armazenamento persistente dos dados. |
| Testes | JUnit e Mockito | Testes automatizados. |
| Qualidade | Checkstyle | Linting (Análise estática de código). |
| Automação | GitHub Actions | Pipeline de CI para testes automáticos. |

---

## ⚙️ Funcionalidades Principais (MVP)
- [ ] Cadastro e gestão de instituições sociais.
- [ ] Publicação de necessidades com níveis de urgência (Alta, Média, Baixa).
- [ ] Listagem inteligente de demandas para doadores.
- [ ] Interface CLI para interação rápida com o sistema.

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
* Java 17 ou superior.
* Maven 3.8+.
* Docker (opcional, para o banco de dados).

### Instalação
1. Clone o repositório:
   ```bash
   git clone https://github.com/Martins20321/Nexus-Social-API.git
