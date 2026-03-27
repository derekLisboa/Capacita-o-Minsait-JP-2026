# 🛒 JP Capacitacao 2026 

API REST desenvolvida em Java com Spring Boot para gerenciamento de um sistema de e-commerce, incluindo controle de produtos, categorias, carrinho, pedidos, usuários e histórico de preços.

---

## 🚀 Tecnologias Utilizadas

- Java 21+
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT (Autenticação)
- Swagger (Documentação)
- Maven
- Banco de Dados Relacional (ex: PostgreSQL ou H2)

---

## 📌 Funcionalidades

✔️ Cadastro e autenticação de usuários  
✔️ Controle de permissões (roles)  
✔️ CRUD de produtos e categorias  
✔️ Gerenciamento de carrinho de compras  
✔️ Criação e acompanhamento de pedidos  
✔️ Controle de estoque  
✔️ Registro de movimentações de estoque  
✔️ Histórico de alteração de preços  
✔️ Tratamento global de exceções  

---

## 🔐 Autenticação

A API utiliza autenticação via **JWT (JSON Web Token)**.

---

🧱 Estrutura do Projeto

├── config              # Configurações (Security, Swagger)
├── controller          # Endpoints da API
├── dto                 # Objetos de transferência de dados
├── enums               # Enumerações do sistema
├── exception           # Tratamento de erros
├── model               # Entidades JPA
├── repository          # Interfaces de acesso ao banco
├── security            # Utilitários de segurança (JWT)
├── service             # Regras de negócio

---

##📚 Endpoints Principais

👤 Usuários
  POST /users
  GET /users
  
🔐 Autenticação
  POST /auth/login
  
📦 Produtos
  GET /products
  POST /products
  PUT /products/{id}
  DELETE /products/{id}
  
🗂️ Categorias
  GET /categories
  POST /categories
  
🛒 Carrinho
  GET /cart
  POST /cart/items
  
📑 Pedidos
  POST /orders
  GET /orders
  
📊 Histórico de Preço
  GET /price-history/{productId}
  
📦 Estoque
  POST /inventory-transactions

---

##📖 Documentação da API

A documentação interativa está disponível via Swagger:
  
  http://localhost:9090/swagger-ui/index.html

---

👨‍💻 Autor

Desenvolvido por Derek M. Lisboa

---
