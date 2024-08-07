# Hotel Reservation API

## Descrição

Este projeto é uma API desenvolvida em Spring Boot para cadastro de reservas em hotéis. A API possui uma integração com o Telegram, permitindo que os usuários se cadastrem, façam reservas e acompanhem suas reservas diretamente pelo Telegram.

## Funcionalidades

### Estabelecimentos
- **Cadastro de Hotéis**: Permite que o estabelecimento registre diferentes hotéis.
- **Cadastro de Quartos**: Permite o registro de quartos dentro dos hotéis cadastrados.
- **Cadastro de Camas**: Permite o registro de camas dentro dos quartos.
- **Cadastro de Clientes**: Permite o registro de clientes que utilizarão os serviços dos hotéis.
- **Reservas**: Permite que as reservas sejam feitas para os clientes registrados.

### Clientes
- **Cadastro via Telegram**: Clientes podem se cadastrar diretamente pelo Telegram.
- **Reserva via Telegram**: Clientes podem fazer reservas utilizando o Telegram.
- **Acompanhamento de Reservas via Telegram**: Clientes podem acompanhar o status de suas reservas pelo Telegram.

## Tecnologias Utilizadas

- **Spring Boot**: Framework principal para o desenvolvimento da API.
- **Telegram API**: Integração para permitir interações através do Telegram.
- **JPA/Hibernate**: Para persistência de dados.
- **PostgreSQL**: Banco de dados para testes e desenvolvimento.

## Endpoints Principais

- **/hotel**: Endpoints para cadastro e gerenciamento de hotéis.
- **/quarto**: Endpoints para cadastro e gerenciamento de quartos.
- **/cama**: Endpoints para cadastro e gerenciamento de camas.
- **/cliente**: Endpoints para cadastro e gerenciamento de clientes.
- **/reserva**: Endpoints para fazer e gerenciar reservas.
