API de Cadastro de Produtos
Esta API RESTful foi desenvolvida para simplificar o gerenciamento de produtos em seu sistema. Ela oferece funcionalidades para criar, ler, atualizar, excluir e gerar relatório de categorias e produtos  de forma eficiente e escalável. A API também possui um recurso para enviar o catálogo de produtos para os clientes via email, aonde o mesmo receberá uma lista de produtos cadastrados.

🚀 Tecnologias Utilizadas

Linguagem: Java - versão 21
Framework: Spring Boot
Banco de Dados: Postgres
Containerização: Docker

🛠️ Montagem do Ambiente com Docker

Para facilitar a configuração e garantir um ambiente de desenvolvimento consistente, utilizamos o Docker. Siga estes passos para subir a aplicação:

1) Clone o repositório

  1.1) git clone [https://docs.github.com/pt/migrations/importing-source-code/using-the-command-line-to-import-source-code/adding-locally-hosted-code-to-github]    (https://github.com/bgrbarbosadev/catalogo-produtos.git)

  1.2) cd catalogo-produtos

  1.3) Construa e inicie os contêineres:

  Bash
  
  docker-compose up --build -d
  Este comando irá construir as imagens Docker (se necessário) e iniciar os contêineres para a aplicação e o banco de dados.

  1.4) Criar uma conexão com o banco e rodar o sql abaixo para o insert do usuário padrão para o inicio das atividades:

  Dados da conexão: 
  
  DATASOURCE_URL=jdbc:postgresql://product-catalog-db:5432/postgres
  DATASOURCE_USERNAME=postgres
  DATASOURCE_PASSWORD=example

  SQL:

  INSERT INTO tb_role("uuid", authority)VALUES('55f46b07-1c87-4bb4-b67f-ccbeaa36b631'::uuid, 'ROLE_ADMIN');
  INSERT INTO tb_role("uuid", authority)VALUES('9cfae039-7c5f-4369-97b2-883b68e2c031'::uuid, 'ROLE_USER');
  
  INSERT INTO tb_user("uuid", email, first_name, last_name, "password")
  VALUES('c2347cd4-53f0-4555-84f5-3c78763da59b'::uuid, 'admin@gmail.com', 'admin', 'admin', '$2a$10$fAdC5xIHW5/uCJfHpHEA/.N4P3jQ7ivnssz.Y0pLvCzyF8VT0NRwS');
  
  INSERT INTO tb_user("uuid", email, first_name, last_name, "password")
  VALUES('ea58c7c6-f3c7-41e3-bddd-f001512220db'::uuid, 'user@gmail.com', 'user', 'user', '$2a$10$JJBfgNfKE2SxqyEbko/gO.SmSGCS0Z7uW3HkAXEYtFu4zf0Kid1Ri');
  
  INSERT INTO tb_user_role(user_id, role_id)VALUES(c2347cd4-53f0-4555-84f5-3c78763da59b, 55f46b07-1c87-4bb4-b67f-ccbeaa36b631);
  INSERT INTO tb_user_role(user_id, role_id)VALUES(ea58c7c6-f3c7-41e3-bddd-f001512220db, 9cfae039-7c5f-4369-97b2-883b68e2c031);

Acesse a API:
Após os contêineres estarem rodando, a API estará disponível em http://localhost:8080. Consulte a seção de "Recursos" para ver as coleções de endpoints.

📚 Recursos e Documentação
Aqui você encontrará coleções de requisições para facilitar a interação com a API, além de links úteis para a documentação.

Postman Collections:

Para a utilização da api, dentro do raiz do diretório encontramos a collection utilizada na aplicação (postman_collection.json)

Documentação da API (Swagger/OpenAPI): http://localhost:8080/swagger-ui/index.html#/

Espero que este README atenda às suas necessidades! Você pode personalizá-lo ainda mais adicionando detalhes sobre as rotas principais, exemplos de requisições/respostas, ou informações sobre contribuição.

# catalogo-produtos
Projeto de Catálogo de Produtos


Próximos passos:

1) Adicionar log na aplicação   ok       
2) validação e mensagens padrão ok
3) pesquisa avançada com specification e pageable ok
4) envio de catálogo via email ok
5) relatório com pdf (jasper) ok
6) segurança ok
8) documentação swagger ok
9) testes
10) docker
