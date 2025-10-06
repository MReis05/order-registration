Registro de Pedidos

Descrição do Projeto

O Registro de Pedidos é uma aplicação de desktop desenvolvida em Java com JavaFX para o gerenciamento de pedidos de um estabelecimento comercial. O sistema permite registrar e acompanhar as vendas realizadas tanto pela plataforma iFood quanto por venda direta (PV - Ponto de Venda), oferecendo um balanço detalhado dos ganhos e taxas do dia.

O projeto foi criado com o objetivo de praticar e demonstrar habilidades em desenvolvimento de software com Java, utilizando o padrão de arquitetura MVC, manipulação de banco de dados com JDBC e criação de interfaces gráficas com JavaFX.

Funcionalidades

O sistema oferece as seguintes funcionalidades:

Registro de Pedidos iFood:

Cadastro de novos pedidos com valor do produto, valor da entrega e método de pagamento.

Cálculo automático de taxas e comissões da plataforma.

Diferenciação de pedidos pagos pela loja ou pelo aplicativo.

Registro de Pedidos Diretos (PV):

Cadastro de vendas diretas com valor do pedido, valor da entrega e forma de pagamento (Dinheiro, Cartão, Pix).

Listagem e Gerenciamento:

Visualização de todos os pedidos registrados em tabelas separadas para iFood e PV.

Opção para remover pedidos individualmente.

Balanço do Dia:

Tela de resultados que consolida todas as vendas do dia.

Exibe o faturamento total, total por canal (iFood/PV), total de entregas, comissões e valores recebidos por cada forma de pagamento.

Reset de Dados:

Funcionalidade para apagar todos os registros do banco de dados e iniciar um novo dia de vendas.

Tecnologias Utilizadas
Linguagem: Java 17+

Interface Gráfica: JavaFX

Banco de Dados: MySQL

Conectividade com BD: JDBC Driver para MySQL

IDE: Desenvolvido no Eclipse

Como Executar o Projeto
Para executar este projeto, você precisará ter o Java (JDK 17 ou superior) e o MySQL instalados em sua máquina.

1. Clone o Repositório:

```Bash

git clone https://github.com/MReis05/order-registration.git
cd order-registration
```
2. Configure o Banco de Dados:

Crie um banco de dados no seu MySQL com o nome registy (ou outro nome de sua preferência).

Execute o script SQL abaixo para criar as tabelas necessárias:

```SQL

CREATE TABLE ifood (
  Id int(11) NOT NULL AUTO_INCREMENT,
  OrderValue double DEFAULT NULL,
  DeliveryValue double DEFAULT NULL,
  Tax double DEFAULT NULL,
  ForIfood double DEFAULT NULL,
  PaymentType varchar(60) DEFAULT NULL,
  PaymentValue double DEFAULT NULL,
  category varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE directorder (
  Id int(11) NOT NULL AUTO_INCREMENT,
  OrderValue double DEFAULT NULL,
  DeliveryValue double DEFAULT NULL,
  PaymentType varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);
```
Atualize o arquivo db.propeties com suas credenciais do MySQL:

```propeties

user=seu_usuario_mysql
password=sua_senha_mysql
dburl=jdbc:mysql://localhost:3306/registy?useSSL=false
```
3. Importe e Execute no Eclipse:
Abra o Eclipse e importe o projeto como "Existing Projects into Workspace".

Certifique-se de que as bibliotecas do JavaFX e o Connector/J (JDBC Driver para MySQL) estão adicionadas ao Build Path do projeto.

Execute o arquivo src/application/Main.java como "Java Application".

Estrutura do Projeto
O projeto segue a arquitetura em camadas, separando as responsabilidades:

application: Contém a classe Main que inicia a aplicação JavaFX.

gui: Pacote com as telas (.fxml), controladores e utilitários da interface gráfica.

model: Contém as entidades (entities), os serviços de negócio (services) e os objetos de acesso a dados (dao).

db: Classes para conexão com o banco de dados.

Autor
Matheus Reis Cardoso

LinkedIn: https://www.linkedin.com/in/matheus-reis-cardoso-6a619120b/

GitHub: https://github.com/MReis05