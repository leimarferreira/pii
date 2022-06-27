# Trabalho da disciplina de Projeto Integrador I

## Links Importantes

  * [Construção da interface](https://www.figma.com/file/cjcD0lrqYbjVx5qEjYPyjt/ProjetoIntegrador?node-id=0%3A1)

## Executar

### Requisitos:

- [MySQL 8.0+](https://www.mysql.com/)
- [Docker](https://www.docker.com/) (OPCIONAL, caso não queira instalar o MySQL na máquina)
- [Docker Compose](https://www.docker.com/) (OPCIONAL, caso não queira instalar o MySQL na máquina)

### Executar

1. Configure e inicie o banco de dados.

Caso não queira instalar e executar o MySQL, pode utilizar o docker para
executar um container com o MySQL. Para executar o MySQL utilizando o Docker,
execute os seguintes comandos:

```
cd docker
docker-compose up --build -d
```

2. [Execute o backend](backend/README.md).
3. [Execute o frontend](frontend/README.md)
