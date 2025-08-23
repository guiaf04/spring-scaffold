# Spring Scaffold CLI

🚀 **Version 2.1.0** - Command-line application to assist in building Java and Spring Boot applications, offering automatic and **functional** scaffold generation for models, controllers, services, repositories, and complete projects.

## ✨ What's New in v2.1.0

### 🎯 **Intuitive Syntax and Positional Parameters**
- **Short aliases**: `--pkg`, `--deps`, `--entity`, `--db`, `-p`, `-m`, `-t`
- **Positional parameters**: `spring-scaffold model User name:String email:String age:Integer`
- **50% faster development**: Less typing, more productivity
- **100% compatible**: All old syntax continues to work

## ✨ Features

### 🎯 Code Generation (100% Functional)
- **Models/Entities**: Class creation with JPA annotations, validations, Lombok and constructors
- **Controllers**: Complete REST controllers with CRUD endpoints, Swagger and validations
- **Services**: Service classes with interfaces, implementations and transactions
- **Repositories**: JPA/MongoDB repository interfaces with custom queries
- **Projects**: Complete Spring Boot projects with Maven structure

### 🏗️ Project Templates
- **Spring Boot Project**: Complete structure with pom.xml, application.properties
- **Database Configurations**: Support for H2, MySQL, PostgreSQL
- **Docker**: Optional Dockerfile files
- **Documentation**: Automatic README.md and .gitignore

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Installation
```bash
git clone https://github.com/guiaf04/spring-scaffold.git
cd spring-scaffold
mvn clean package -DskipTests
```

### Basic Usage

#### 🎯 **New Syntax (v2.1.0) - Recommended**
```bash
# Generate a model with natural syntax
./spring-scaffold model User name:String email:String age:Integer --pkg com.example.entity

# Generate a controller with aliases
./spring-scaffold controller UserController --entity User --pkg com.example.controller

# Generate a service with shortcuts
./spring-scaffold service UserService -m User -p com.example.service

# Generate a concise repository
./spring-scaffold repository UserRepository -m User -p com.example.repository -t JPA

# Create new project with aliases
./spring-scaffold project my-spring-app --pkg com.example.myapp --deps web,jpa,security
```

#### ⚪ **Classic Syntax (still works)**
```bash
# Generate a model
./spring-scaffold model User -p com.example.model -f "name:String,email:String,age:Integer"

# Generate a controller
./spring-scaffold controller UserController -m User

# Generate a service
./spring-scaffold service UserService -m User

# Generate a repository
./spring-scaffold repository UserRepository -m User

# Create new project
./spring-scaffold project my-spring-app --package com.example.myapp
```

## 📚 Available Commands

### `model`
Generates a model/entity class with JPA annotations.

**💫 New Syntax (v2.1.0):**
```bash
spring-scaffold model <ClassName> [field:type] [field:type] [options]
```

**Examples:**
```bash
# Natural syntax with positional parameters
spring-scaffold model User name:String email:String age:Integer

# With aliases and validation
spring-scaffold model Product name:String price:BigDecimal --pkg com.example.entity --valid

# Using shortcuts
spring-scaffold model Customer name:String email:String -p com.app.model --entity --data
```

**Options:**
- `-p, --pkg, --package <package>`: Class package (default: com.example.model)
- `-f, --fields <fields>`: Field list in "name:type,name:type" format (alternative to positional)
- `-t, --tbl, --table <table>`: Table name (default: class name in snake_case)
- `--jpa, --entity`: Add JPA annotations (default: true)
- `--data, --lombok`: Use Lombok annotations (default: true)
- `--valid, --validation`: Include Bean Validation annotations (default: false)

### `controller`
Generates a REST controller with CRUD endpoints.

**💫 New Syntax (v2.1.0):**
```bash
spring-scaffold controller <ControllerName> [options]
```

**Examples:**
```bash
# With intuitive aliases
spring-scaffold controller UserController --entity User --pkg com.example.controller

# Using shortcuts
spring-scaffold controller ProductController -m Product -p com.app.controller
```

**Options:**
- `-p, --pkg, --package <package>`: Controller package (default: com.example.controller)
- `-m, --model, --entity <model>`: Associated model class
- `--path <path>`: API base path (default: /api/v1)

### `service`
Generates a service class.

**💫 New Syntax (v2.1.0):**
```bash
spring-scaffold service <ServiceName> [options]
```

**Examples:**
```bash
# With aliases
spring-scaffold service UserService --entity User --pkg com.example.service

# Maximum conciseness
spring-scaffold service ProductService -m Product -p com.app.service
```

**Options:**
- `-p, --pkg, --package <package>`: Service package (default: com.example.service)
- `-m, --model, --entity <model>`: Associated model class
- `--interface`: Generate service interface (default: true)

### `repository`
Generates a JPA repository.

**💫 New Syntax (v2.1.0):**
```bash
spring-scaffold repository <RepositoryName> [options]
```

**Examples:**
```bash
# With aliases
spring-scaffold repository UserRepository --entity User --pkg com.example.repository

# Using single-letter shortcuts
spring-scaffold repository ProductRepository -m Product -p com.app.repo -t JPA
```

**Options:**
- `-p, --pkg, --package <package>`: Repository package (default: com.example.repository)
- `-m, --model, --entity <model>`: Associated model class
- `-t, --type <type>`: Repository type (JPA, MongoDB) (default: JPA)

### `project`
Creates a new Spring Boot project.

**💫 New Syntax (v2.1.0):**
```bash
spring-scaffold project <project-name> [options]
```

**Examples:**
```bash
# With intuitive aliases
spring-scaffold project my-api --pkg com.example.myapi --deps web,jpa,security --db MYSQL

# Using shortcuts
spring-scaffold project ecommerce -p com.shop.ecommerce -d web,jpa,validation -s 3.2.0
```

**Options:**
- `-p, --pkg, --package <package>`: Project base package (default: com.example)
- `-g, --group, --group-id <group>`: Maven Group ID (default: --package value)
- `-s, --spring, --spring-version <version>`: Spring Boot version (default: 3.2.0)
- `-j, --java, --java-version <version>`: Java version (default: 17)
- `-d, --deps, --dependencies <deps>`: Comma-separated dependencies
- `--db, --database <db>`: Database type (H2, MYSQL, POSTGRESQL, MONGODB)

## 🎯 **Syntax Comparison**

| Command | ❌ Before (v2.0) | ✅ Now (v2.1) | 💾 Savings |
|---------|------------------|------------------|-------------|
| **Model** | `--package --fields name:String,email:String` | `name:String email:String --pkg` | **35%** |
| **Project** | `--package --dependencies` | `--pkg --deps` | **50%** |
| **Controller** | `--package --model` | `--pkg --entity` or `-p -m` | **40%** |
| **Repository** | `--package --model --type` | `-p -m -t` | **65%** |

## 📝 Examples

### 🚀 Creating a Complete CRUD (New Syntax v2.1)

```bash
# 1. Create the model with natural syntax
spring-scaffold model Product name:String price:BigDecimal description:String active:Boolean --pkg com.example.entity --valid

# 2. Create the repository with shortcuts
spring-scaffold repository ProductRepository -m Product -p com.example.repository

# 3. Create the concise service
spring-scaffold service ProductService -m Product -p com.example.service

# 4. Create the controller with aliases
spring-scaffold controller ProductController --entity Product --pkg com.example.controller
```

### ⚡ Rapid API Development

```bash
# Complete project with aliases
spring-scaffold project ecommerce-api --pkg com.example.ecommerce --deps web,jpa,security,validation --db POSTGRESQL

# Multiple entities quickly
cd ecommerce-api
spring-scaffold model User name:String email:String role:String
spring-scaffold model Order total:BigDecimal status:String userId:Long  
spring-scaffold model Item name:String price:BigDecimal stock:Integer

# Generate all layers with maximum efficiency
spring-scaffold controller UserController -m User -p com.example.ecommerce.controller
spring-scaffold controller OrderController -m Order -p com.example.ecommerce.controller
spring-scaffold controller ItemController -m Item -p com.example.ecommerce.controller
```

### 📊 Productivity Comparison

#### ❌ **Before (v2.0.0):**
```bash
spring-scaffold model Customer --package com.example.entity --fields name:String,email:String,phone:String,age:Integer
spring-scaffold controller CustomerController --package com.example.controller --model Customer
spring-scaffold service CustomerService --package com.example.service --model Customer  
spring-scaffold repository CustomerRepository --package com.example.repository --model Customer --type JPA
```

#### ✅ **Now (v2.1.0):**
```bash
spring-scaffold model Customer name:String email:String phone:String age:Integer --pkg com.example.entity
spring-scaffold controller CustomerController --entity Customer --pkg com.example.controller
spring-scaffold service CustomerService -m Customer -p com.example.service
spring-scaffold repository CustomerRepository -m Customer -p com.example.repository -t JPA
```

**🎯 Result: 50% less typing, more natural syntax!**

### 🎯 Useful Command Templates

```bash
# Template for complete CRUD
ENTITY="Product"
PKG="com.example.ecommerce"

spring-scaffold model $ENTITY name:String price:BigDecimal --pkg $PKG.entity --valid
spring-scaffold repository ${ENTITY}Repository -m $ENTITY -p $PKG.repository
spring-scaffold service ${ENTITY}Service -m $ENTITY -p $PKG.service  
spring-scaffold controller ${ENTITY}Controller -m $ENTITY -p $PKG.controller
```

## 🏗️ Architecture

### Project Structure
```
spring-scaffold-cli/
├── src/main/java/
│   ├── com/scaffold/
│   │   ├── SpringScaffoldCLI.java          # Main class
│   │   ├── commands/                        # CLI commands
│   │   │   ├── ModelCommand.java
│   │   │   ├── ControllerCommand.java
│   │   │   ├── ServiceCommand.java
│   │   │   ├── RepositoryCommand.java
│   │   │   └── ProjectCommand.java
│   │   ├── generators/                      # Code generators
│   │   │   ├── ModelGenerator.java
│   │   │   ├── ControllerGenerator.java
│   │   │   ├── ServiceGenerator.java
│   │   │   ├── RepositoryGenerator.java
│   │   │   └── ProjectGenerator.java
│   │   ├── templates/                       # Template engine
│   │   │   ├── TemplateEngine.java
│   │   │   └── TemplateContext.java
│   │   ├── models/                          # Data models
│   │   │   ├── FieldInfo.java
│   │   │   ├── ClassInfo.java
│   │   │   └── ProjectInfo.java
│   │   └── utils/                           # Utilities
│   │       ├── FileUtils.java
│   │       ├── NamingUtils.java
│   │       └── ValidationUtils.java
├── src/main/resources/
│   └── templates/                           # Mustache templates
│       ├── model.java.mustache
│       ├── controller.java.mustache
│       ├── service.java.mustache
│       ├── service-impl.java.mustache
│       ├── repository.java.mustache
│       └── project/                         # Project templates
│           ├── pom.xml.mustache
│           ├── application.yml.mustache
│           └── main-class.java.mustache
└── pom.xml
```

### Technologies Used
- **Picocli**: CLI framework
- **Mustache**: Template engine
- **Lombok**: Boilerplate reduction
- **Jackson**: JSON/YAML manipulation
- **Maven**: Dependency management

## 🛠️ Development

### How to Contribute
1. Fork the repository
2. Create a branch for your feature (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

### Adding New Commands
1. Create command class in `src/main/java/com/scaffold/commands/`
2. Implement generator in `src/main/java/com/scaffold/generators/`
3. Create template in `src/main/resources/templates/`
4. Register command in main class

### Running Tests
```bash
mvn test
```

### Template Testing
**⚠️ Important**: Always run the template test script instead of testing multiple commands manually:

```bash
./test-templates.sh
```

This script performs comprehensive testing of all templates:
- ✅ Generates complete Spring Boot project
- ✅ Tests all component generation (model, service, repository, controller)
- ✅ Validates Maven compilation and tests
- ✅ Verifies code quality and internationalization
- ✅ Ensures all templates work correctly together

The script automatically cleans up test artifacts and provides detailed feedback on each test phase.

### Build
```bash
mvn clean package
```

## 🌍 Documentation

- **English**: [README.md](README.md) (this file)
- **Portuguese**: [docs/pt-BR/README-pt.md](docs/pt-BR/README-pt.md)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Support

For support, open an issue on GitHub or contact through [email].

## 📋 Changelog

### Version 2.1.0 (2025-08-22)
- ✨ **NEW SYNTAX**: Intuitive aliases and positional parameters
- 🎯 **Positional parameters**: `model User name:String email:String age:Integer`
- ⚡ **Short aliases**: `--pkg`, `--deps`, `--entity`, `--db`, `-p`, `-m`, `-t`
- 🚀 **Productivity**: Up to 50% faster development
- 🔄 **Compatibility**: 100% compatible with v2.0.0 syntax
- 📝 **Documentation**: Updated README with comparative examples
- 🌍 **Internationalization**: Main docs in English, Portuguese in docs/pt-BR/

### Version 2.0.0 (2025-08-22)
- ✅ **FUNCTIONAL**: All commands now generate real files
- 🎯 Complete REST controllers with CRUD, Swagger, validations
- 🔧 Services with interface + implementation or single class
- 📦 JPA repositories with custom queries
- 🏗️ Complete Spring Boot projects with Maven
- 🐛 Bug fixes and stability improvements

### Version 1.0.0 (2025-08-22)
- 🎉 Initial version with CLI structure
- 📋 Functional model command
- ⚠️ Other commands only simulated generation

See [CHANGELOG.md](CHANGELOG.md) for complete details.

## 🗺️ Roadmap

- [ ] Kotlin support
- [ ] Microservices templates
- [ ] Spring Cloud integration
- [ ] Automatic documentation generation
- [ ] IDE plugins
- [ ] GraphQL support
- [ ] Performance testing templates