<p align="center">
    <img src="https://www.svgrepo.com/show/184143/java.svg" width="130" />
    <img src="https://www.edureka.co/blog/wp-content/uploads/2019/08/recyclebin-data-1.png" width="220" />
</p>

# ContactHub - API - v1.0.0


## Funcionalidades implementadas

[x] Busca de profissionais paginada com filtro de nome, cargo ou data de criação

[x] Criação, atualização, deleção de profissionais

[x] Busca de contatos por professional

[x] Criação, atualização, deleção de contatos associados a um profissional

## Pré-requisitos

Para execução do serviço é necessário ter instalado no ambiente os softwares abaixo nas versões descritas ou superiores:

- Java 17
- Maven
- Docker (Opcional)

## Principais dependências

- Spring Boot v3.2.3
- Spring Data JPA
- PostgreSQL Driver
- Kaczmarzyk Specification Arg Resolver v2.6.2
- Lombok
- Spock Framework (Testes)

### Instalação de dependências

``` bash
   mvn install
```

## Environment Variables

O projeto se utiliza de variáveis de ambiente que podem ser definidas nos arquivos **application.yml** ou na tag **environment** quando se é utilizado o Docker.
Utilizar como exemplo o arquivo application.yml.example
O Springboot trabalha com a utilização de **profiles**, neste projeto temos o arquivo **application.yml** que possui algumas propriedades padrões ao ambiente, servindo de base.
Outros profiles podem ser criados, exemplo:

- **application-dev.yml**
- **application-staging.yml**

Estes profiles especificam as variaveis de seus ambientes. 

## Execução local

Para executar o projeto de forma local, faça a configuração do arquivo application.yml seguindo o arquivo **.application.yml.example** e execute os comandos abaixo.

``` bash
   SPRING_PROFILES_ACTIVE=prod spring-boot:run
```

**--> Caso queria executar com um profile diferente, subistiua o **[prod]** pelo profile desejado <--**

A aplicação por padrão estara disponivel em **http://127.0.0.1:8082** a porta pode ser configurada no arquivo **application.yml** no atributo **server.port**

## Instalação e execução com Docker

Para facilitar instalação e execução da aplicação foi implementado containers Docker, onde é realizado o processo de build, testes e execução.
O profile configurado para utilizanção no Docker é o **application-docker.yml**

Estrutura dos arquivos Docker:

- **Dockerfile:** responsável por realizar o build da imagem da aplicação
- **docker-compose.yml:** responsável por realizar o build e execução do servicos
- **docker-compose-db.yml:** responsável por executar um serço de banco de dados PostgreSql

Os passos abaixo devem ser executados na raiz do projeto.

### Build

``` bash
    docker-compose -p contact-hub -f docker-compose/docker-compose.yml build
```

### Up

``` bash
    docker-compose -p contact-hub -f docker-compose/docker-compose.yml up -d
```

### Down

``` bash
    docker-compose -p contact-hub -f docker-compose/docker-compose.yml down
```

## Testes

Foi utilizado do Spock Framework para realizar a criação e execução de testes de unidade/integração, para execução dos testes são
necessário executar os comandos abaixo.

``` bash
    mvn test
```

## Swagger

Para facilitar a documentação e interação com a API deste projeto, utilizamos o Swagger, uma ferramenta que
permite visualizar, testar e entender melhor os endpoints disponíveis. Abaixo estão as principais informações
relacionadas ao Swagger neste projeto:


### Acessando o Swagger
Após iniciar o serviço localmente ou através do Docker, você pode acessar a interface do Swagger no seguinte endereço:

Local: http://localhost:8082/swagger-ui-custom.html
Docker: Substitua a porta `8082` pela porta configurada no **application-docker.yml**


### Explorando a API
Ao acessar o Swagger, você terá uma visão completa dos endpoints disponíveis, suas descrições, tipos de requisições
suportadas (GET, POST, etc.), parâmetros necessários e exemplos de resposta.



### Testando Endpoints
O Swagger permite que você teste os endpoints diretamente na interface, fornecendo entradas de dados e visualizando as
respostas. Isso facilita o processo de desenvolvimento e depuração.



## Desenvolvedores

- Bruno F Godoi - brunofgodoi@outlook.com.br
