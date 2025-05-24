# Biblioteca IBT

Esse projeto tem como intuito fornecer um sistema capaz de controlar os empréstimos realizados na biblioteca da Igreja
Batista Trindade.

# Pré-requisito

Como pré-requisito, você precisará de um banco com o DDL adequado para o projeto. Nesse caso, o
arquivo [docker-compose.yml](./docker-compose.yml) contém essas instruções. Você pode mudar as variáveis de ambiente do
contêiner caso ache necessário. Para subir o contêiner, rode o seguinte comando (Para rodar esse comando, você precisará
ter instalado o [Docker](https://www.docker.com/) em sua máquina):

```shell
docker compose up -d
```

# Configuração

Para realizar a configuração do projeto, você precisará configurar apenas quatro variáveis de ambiente:

- `DATABASE_URL`: A URL em formato JDBC do banco de dados do qual você deseja conectar
- `DATABASE_USER`: O usuário do banco a qual você deseja conectar o sistema
- `DATABASE_PASSWORD`: A senha do banco a qual você deseja conectar o sistema
- `JWT_SECRET`: A secret responsável por codificar os tokens JWT

Após a configuração das variáveis de ambiente, rode o seguinte comando:

```shell
./gradlew run
```