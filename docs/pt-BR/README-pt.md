# Spring Scaffold CLI

🚀 **Versão 2.1.3** - Aplicativo de linha de comando para auxiliar na construção de aplicações Java e Spring Boot, oferecendo geração automática e **funcional** de scaffold para models, controllers, services, repositories e projetos completos.

## ✨ Novidades v2.1.3

### 🚀 **Pipeline CI/CD e Gerenciamento de Releases**
- **Pipeline DevOps Completo**: CI/CD automatizado com GitHub Actions
- **Integração Maven Central**: Deploy automatizado com assinatura GPG
- **Garantia de Qualidade**: Integração com SonarCloud e scanning de segurança
- **Automação de Release**: Processo de release simplificado com testes automatizados

### 🔧 **Experiência do Desenvolvedor Aprimorada (v2.1.2)**
- **Documentação Melhorada**: Instruções claras de configuração para todas as integrações
- **Melhor Configuração**: Clarity de workflow aprimorada e orientação para chaves de API

### 🎯 **Sintaxe Intuitiva e Parâmetros Posicionais (v2.1.0)**
- **Aliases curtos**: `--pkg`, `--deps`, `--entity`, `--db`, `-p`, `-m`, `-t`
- **Parâmetros posicionais**: `spring-scaffold model User name:String email:String age:Integer`
- **Desenvolvimento 50% mais rápido**: Menos digitação, mais produtividade
- **100% compatível**: Todas as sintaxes antigas continuam funcionando

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

#### 🎯 **Nova Sintaxe (v2.1.0) - Recomendada**
```bash
# Gerar um model com sintaxe natural
./spring-scaffold model User name:String email:String age:Integer --pkg com.example.entity

# Gerar um controller com aliases
./spring-scaffold controller UserController --entity User --pkg com.example.controller

# Gerar um service com atalhos
./spring-scaffold service UserService -m User -p com.example.service

# Gerar um repository conciso
./spring-scaffold repository UserRepository -m User -p com.example.repository -t JPA

# Criar novo projeto com aliases
./spring-scaffold project my-spring-app --pkg com.example.myapp --deps web,jpa,security
```

#### ⚪ **Sintaxe Clássica (ainda funciona)**
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

**💫 Nova Sintaxe (v2.1.0):**
```bash
spring-scaffold model <NomeClasse> [campo:tipo] [campo:tipo] [opções]
```

**Exemplos:**
```bash
# Sintaxe natural com parâmetros posicionais
spring-scaffold model User name:String email:String age:Integer

# Com aliases e validação
spring-scaffold model Product name:String price:BigDecimal --pkg com.example.entity --valid

# Usando atalhos
spring-scaffold model Customer name:String email:String -p com.app.model --entity --data
```

**Opções:**
- `-p, --pkg, --package <package>`: Pacote da classe (padrão: com.example.model)
- `-f, --fields <fields>`: Lista de campos no formato "nome:tipo,nome:tipo" (alternativa aos posicionais)
- `-t, --tbl, --table <table>`: Nome da tabela (padrão: nome da classe em snake_case)
- `--jpa, --entity`: Adicionar annotations JPA (padrão: true)
- `--data, --lombok`: Usar Lombok annotations (padrão: true)
- `--valid, --validation`: Incluir Bean Validation annotations (padrão: false)

### `controller`
Gera um controller REST com endpoints CRUD.

**💫 Nova Sintaxe (v2.1.0):**
```bash
spring-scaffold controller <NomeController> [opções]
```

**Exemplos:**
```bash
# Com aliases intuitivos
spring-scaffold controller UserController --entity User --pkg com.example.controller

# Usando atalhos
spring-scaffold controller ProductController -m Product -p com.app.controller
```

**Opções:**
- `-p, --pkg, --package <package>`: Pacote do controller (padrão: com.example.controller)
- `-m, --model, --entity <model>`: Classe do model associado
- `--path <path>`: Path base da API (padrão: /api/v1)
- `--crud`: Incluir operações CRUD completas (padrão: true)

### `service`
Gera uma classe de serviço.

**💫 Nova Sintaxe (v2.1.0):**
```bash
spring-scaffold service <NomeService> [opções]
```

**Exemplos:**
```bash
# Com aliases
spring-scaffold service UserService --entity User --pkg com.example.service

# Máxima concisão
spring-scaffold service ProductService -m Product -p com.app.service
```

**Opções:**
- `-p, --pkg, --package <package>`: Pacote do service (padrão: com.example.service)
- `-m, --model, --entity <model>`: Classe do model associado
- `--interface`: Gerar interface do service (padrão: true)

### `repository`
Gera um repository JPA.

**💫 Nova Sintaxe (v2.1.0):**
```bash
spring-scaffold repository <NomeRepository> [opções]
```

**Exemplos:**
```bash
# Com aliases
spring-scaffold repository UserRepository --entity User --pkg com.example.repository

# Usando atalhos de uma letra
spring-scaffold repository ProductRepository -m Product -p com.app.repo -t JPA
```

**Opções:**
- `-p, --pkg, --package <package>`: Pacote do repository (padrão: com.example.repository)
- `-m, --model, --entity <model>`: Classe do model associado
- `-t, --type <type>`: Tipo de repository (JPA, MongoDB) (padrão: JPA)

### `project`
Cria um novo projeto Spring Boot.

**💫 Nova Sintaxe (v2.1.0):**
```bash
spring-scaffold project <nome-projeto> [opções]
```

**Exemplos:**
```bash
# Com aliases intuitivos
spring-scaffold project my-api --pkg com.example.myapi --deps web,jpa,security --db MYSQL

# Usando atalhos
spring-scaffold project ecommerce -p com.shop.ecommerce -d web,jpa,validation -s 3.2.0
```

**Opções:**
- `-p, --pkg, --package <package>`: Pacote base do projeto (padrão: com.example)
- `-g, --group, --group-id <group>`: Group ID do Maven (padrão: valor do --package)
- `-s, --spring, --spring-version <version>`: Versão do Spring Boot (padrão: 3.2.0)
- `-j, --java, --java-version <version>`: Versão do Java (padrão: 17)
- `-d, --deps, --dependencies <deps>`: Dependências separadas por vírgula
- `--db, --database <db>`: Tipo de banco (H2, MYSQL, POSTGRESQL, MONGODB)

## 🎯 **Comparação de Sintaxes**

| Comando | ❌ Antes (v2.0) | ✅ Agora (v2.1) | 💾 Economia |
|---------|------------------|------------------|-------------|
| **Model** | `--package --fields name:String,email:String` | `name:String email:String --pkg` | **35%** |
| **Project** | `--package --dependencies` | `--pkg --deps` | **50%** |
| **Controller** | `--package --model` | `--pkg --entity` ou `-p -m` | **40%** |
| **Repository** | `--package --model --type` | `-p -m -t` | **65%** |

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

### 🚀 Criando um CRUD Completo (Nova Sintaxe v2.1)

```bash
# 1. Criar o model com sintaxe natural
spring-scaffold model Product name:String price:BigDecimal description:String active:Boolean --pkg com.example.entity --valid

# 2. Criar o repository com atalhos
spring-scaffold repository ProductRepository -m Product -p com.example.repository

# 3. Criar o service conciso
spring-scaffold service ProductService -m Product -p com.example.service

# 4. Criar o controller com aliases
spring-scaffold controller ProductController --entity Product --pkg com.example.controller
```

### ⚡ Desenvolvimento Rápido de API

```bash
# Projeto completo com aliases
spring-scaffold project ecommerce-api --pkg com.example.ecommerce --deps web,jpa,security,validation --db POSTGRESQL

# Múltiplas entidades rapidamente
cd ecommerce-api
spring-scaffold model User name:String email:String role:String
spring-scaffold model Order total:BigDecimal status:String userId:Long  
spring-scaffold model Item name:String price:BigDecimal stock:Integer

# Gerar todas as camadas com máxima eficiência
spring-scaffold controller UserController -m User -p com.example.ecommerce.controller
spring-scaffold controller OrderController -m Order -p com.example.ecommerce.controller
spring-scaffold controller ItemController -m Item -p com.example.ecommerce.controller
```

### 📊 Comparativo de Produtividade

#### ❌ **Antes (v2.0.0):**
```bash
spring-scaffold model Customer --package com.example.entity --fields name:String,email:String,phone:String,age:Integer
spring-scaffold controller CustomerController --package com.example.controller --model Customer
spring-scaffold service CustomerService --package com.example.service --model Customer  
spring-scaffold repository CustomerRepository --package com.example.repository --model Customer --type JPA
```

#### ✅ **Agora (v2.1.0):**
```bash
spring-scaffold model Customer name:String email:String phone:String age:Integer --pkg com.example.entity
spring-scaffold controller CustomerController --entity Customer --pkg com.example.controller
spring-scaffold service CustomerService -m Customer -p com.example.service
spring-scaffold repository CustomerRepository -m Customer -p com.example.repository -t JPA
```

**🎯 Resultado: 50% menos digitação, sintaxe mais natural!**

### 🎯 Templates de Comandos Úteis

```bash
# Template para CRUD completo
ENTITY="Product"
PKG="com.example.ecommerce"

spring-scaffold model $ENTITY name:String price:BigDecimal --pkg $PKG.entity --valid
spring-scaffold repository ${ENTITY}Repository -m $ENTITY -p $PKG.repository
spring-scaffold service ${ENTITY}Service -m $ENTITY -p $PKG.service  
spring-scaffold controller ${ENTITY}Controller -m $ENTITY -p $PKG.controller
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

## 📋 Changelog

### Versão 2.1.0 (2025-08-22)
- ✨ **NOVA SINTAXE**: Aliases intuitivos e parâmetros posicionais
- 🎯 **Parâmetros posicionais**: `model User name:String email:String age:Integer`
- ⚡ **Aliases curtos**: `--pkg`, `--deps`, `--entity`, `--db`, `-p`, `-m`, `-t`
- 🚀 **Produtividade**: Desenvolvimento até 50% mais rápido
- 🔄 **Compatibilidade**: 100% compatível com sintaxe v2.0.0
- 📝 **Documentação**: README atualizado com exemplos comparativos

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
