# Spring Scaffold CLI

🚀 **Versão 2.0.0** - Aplicativo de linha de comando para auxiliar na construção de aplicações Java e Spring Boot, oferecendo geração automática e **funcional** de scaffold para models, controllers, services, repositories e projetos completos.

## ✨ Funcionalidades

### 🎯 Geração de Código (100% Funcional)
- **Models/Entities**: Criação de classes com JPA annotations, validações, Lombok e construtores
- **Controllers**: Controllers REST completos com endpoints CRUD, Swagger e validações
- **Services**: Classes de serviço com interfaces, implementações e transações
- **Repositories**: Interfaces de repositório JPA/MongoDB com queries customizadas
- **Projects**: Projetos Spring Boot completos com estrutura Maven

### 🏗️ Templates de Projeto
- **Projeto Spring Boot**: Estrutura completa com pom.xml, application.properties
- **Configurações de Banco**: Suporte a H2, MySQL, PostgreSQL
- **Docker**: Arquivos Dockerfile opcionais
- **Documentação**: README.md e .gitignore automáticos

## 🚀 Início Rápido

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Instalação
```bash
git clone <repository>
cd spring-scaffold
mvn clean package -DskipTests
```

### Uso Básico
```bash
# Gerar um model
./spring-scaffold model User -p com.example.model -f "name:String,email:String,age:Integer"

# Gerar um controller
./spring-scaffold controller UserController -m User

# Gerar um service
./spring-scaffold service UserService -m User

# Gerar um repository
./spring-scaffold repository UserRepository -m User

# Criar novo projeto
./spring-scaffold project my-spring-app --package com.example.myapp
```

## 📚 Comandos Disponíveis

### `model`
Gera uma classe model/entity com JPA annotations.

**Sintaxe:**
```bash
spring-scaffold model <NomeClasse> [opções]
```

**Opções:**
- `-p, --package <package>`: Pacote da classe (padrão: com.example.model)
- `-f, --fields <fields>`: Lista de campos no formato "nome:tipo,nome:tipo"
- `-t, --table <table>`: Nome da tabela (padrão: nome da classe em snake_case)
- `--jpa`: Adicionar annotations JPA (padrão: true)
- `--lombok`: Usar Lombok annotations (padrão: true)

**Exemplo:**
```bash
spring-scaffold model User -p com.example.model -f "name:String,email:String,age:Integer,active:Boolean"
```

### `controller`
Gera um controller REST com endpoints CRUD.

**Sintaxe:**
```bash
spring-scaffold controller <NomeController> [opções]
```

**Opções:**
- `-p, --package <package>`: Pacote do controller (padrão: com.example.controller)
- `-m, --model <model>`: Classe do model associado
- `--path <path>`: Path base da API (padrão: /api/v1)
- `--crud`: Incluir operações CRUD completas (padrão: true)

### `service`
Gera uma classe de serviço.

**Sintaxe:**
```bash
spring-scaffold service <NomeService> [opções]
```

**Opções:**
- `-p, --package <package>`: Pacote do service (padrão: com.example.service)
- `-m, --model <model>`: Classe do model associado
- `--interface`: Gerar interface do service (padrão: true)

### `repository`
Gera um repository JPA.

**Sintaxe:**
```bash
spring-scaffold repository <NomeRepository> [opções]
```

**Opções:**
- `-p, --package <package>`: Pacote do repository (padrão: com.example.repository)
- `-m, --model <model>`: Classe do model associado
- `--type <type>`: Tipo de repository (JPA, MongoDB) (padrão: JPA)

### `project`
Cria um novo projeto Spring Boot.

**Sintaxe:**
```bash
spring-scaffold project <nome-projeto> [opções]
```

**Opções:**
- `--package <package>`: Pacote base do projeto (padrão: com.example)
- `--spring-version <version>`: Versão do Spring Boot (padrão: latest)
- `--dependencies <deps>`: Dependências separadas por vírgula
- `--database <db>`: Tipo de banco (h2, mysql, postgresql, mongodb)

## 🏗️ Arquitetura

### Estrutura do Projeto
```
spring-scaffold-cli/
├── src/main/java/
│   ├── com/scaffold/
│   │   ├── SpringScaffoldCLI.java          # Classe principal
│   │   ├── commands/                        # Comandos CLI
│   │   │   ├── ModelCommand.java
│   │   │   ├── ControllerCommand.java
│   │   │   ├── ServiceCommand.java
│   │   │   ├── RepositoryCommand.java
│   │   │   └── ProjectCommand.java
│   │   ├── generators/                      # Geradores de código
│   │   │   ├── ModelGenerator.java
│   │   │   ├── ControllerGenerator.java
│   │   │   ├── ServiceGenerator.java
│   │   │   ├── RepositoryGenerator.java
│   │   │   └── ProjectGenerator.java
│   │   ├── templates/                       # Engine de templates
│   │   │   ├── TemplateEngine.java
│   │   │   └── TemplateContext.java
│   │   ├── models/                          # Modelos de dados
│   │   │   ├── FieldInfo.java
│   │   │   ├── ClassInfo.java
│   │   │   └── ProjectInfo.java
│   │   └── utils/                           # Utilitários
│   │       ├── FileUtils.java
│   │       ├── NamingUtils.java
│   │       └── ValidationUtils.java
├── src/main/resources/
│   └── templates/                           # Templates Mustache
│       ├── model.java.mustache
│       ├── controller.java.mustache
│       ├── service.java.mustache
│       ├── service-impl.java.mustache
│       ├── repository.java.mustache
│       ├── test.java.mustache
│       └── project/                         # Templates de projeto
│           ├── pom.xml.mustache
│           ├── application.yml.mustache
│           └── main-class.java.mustache
└── pom.xml
```

### Tecnologias Utilizadas
- **Picocli**: Framework para CLI
- **Mustache**: Engine de templates
- **Lombok**: Redução de boilerplate
- **Jackson**: Manipulação de JSON/YAML
- **Maven**: Gerenciamento de dependências

## 🛠️ Desenvolvimento

### Como Contribuir
1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

### Adicionando Novos Comandos
1. Criar classe command em `src/main/java/com/scaffold/commands/`
2. Implementar generator em `src/main/java/com/scaffold/generators/`
3. Criar template em `src/main/resources/templates/`
4. Registrar comando na classe principal

### Executando Testes
```bash
mvn test
```

### Build
```bash
mvn clean package
```

## 📝 Exemplos

### Criando um CRUD Completo
```bash
# 1. Criar o model
spring-scaffold model Product -f "name:String,price:BigDecimal,description:String,active:Boolean"

# 2. Criar o repository
spring-scaffold repository ProductRepository -m Product

# 3. Criar o service
spring-scaffold service ProductService -m Product

# 4. Criar o controller
spring-scaffold controller ProductController -m Product

# 5. Criar testes
spring-scaffold test ProductTest -m Product --type unit
```

### Criando um Projeto Completo
```bash
spring-scaffold project ecommerce-api \
  --package com.example.ecommerce \
  --dependencies web,jpa,security,validation \
  --database postgresql
```

## 🔧 Configuração

### Arquivo de Configuração
Crie um arquivo `.scaffold-config.yml` no diretório do projeto:

```yaml
# Configurações padrão
defaults:
  package:
    base: com.example
    model: model
    controller: controller
    service: service
    repository: repository
  
  templates:
    customPath: ./custom-templates
  
  database:
    type: postgresql
    naming: snake_case
  
  features:
    lombok: true
    jpa: true
    validation: true
    swagger: true
```

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Suporte

Para suporte, abra uma issue no GitHub ou entre em contato através de [email].

## � Changelog

### Versão 2.0.0 (2025-08-22)
- ✅ **FUNCIONAL**: Todos os comandos agora geram arquivos reais
- 🎯 Controllers REST completos com CRUD, Swagger, validações
- 🔧 Services com interface + implementação ou classe única
- 📦 Repositories JPA com queries customizadas
- 🏗️ Projetos Spring Boot completos com Maven
- 🐛 Correções de bugs e melhorias de estabilidade

### Versão 1.0.0 (2025-08-22)
- 🎉 Versão inicial com estrutura CLI
- 📋 Comando model funcional
- ⚠️ Outros comandos apenas simulavam geração

Ver [CHANGELOG.md](CHANGELOG.md) para detalhes completos.

## �🗺️ Roadmap

- [ ] Suporte a Kotlin
- [ ] Templates para microserviços
- [ ] Integração com Spring Cloud
- [ ] Geração de documentação automática
- [ ] Plugin para IDEs
- [ ] Suporte a GraphQL
- [ ] Templates para testes de performance
