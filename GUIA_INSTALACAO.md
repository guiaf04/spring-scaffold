# Guia de Instalação e Uso - Spring Scaffold CLI

## 📋 Visão Geral

O **Spring Scaffold CLI** é uma ferramenta de linha de comando desenvolvida em Java com Picocli que facilita a criação de aplicações Spring Boot através da geração automática de código. A ferramenta permite criar rapidamente models, controllers, services, repositories e projetos completos com estrutura padronizada.

## 🛠️ Tecnologias Utilizadas

- **Java 17+**: Linguagem principal
- **Picocli 4.7.5**: Framework para CLI
- **Mustache 0.9.10**: Engine de templates
- **Lombok 1.18.30**: Redução de boilerplate
- **Jackson 2.15.2**: Manipulação de JSON/YAML
- **Logback 1.4.11**: Sistema de logging
- **Maven**: Gerenciamento de dependências

## 🚀 Instalação

### Pré-requisitos
- Java 17 ou superior
- Maven 3.8+ (apenas para desenvolvimento)

### 1. Clone ou Baixe o Projeto
```bash
git clone <repository-url>
cd spring-scaffold
```

### 2. Compile o Projeto
```bash
mvn clean package
```

### 3. Torne o Script Executável
```bash
chmod +x spring-scaffold
```

### 4. (Opcional) Adicione ao PATH
```bash
# Adicione esta linha ao seu ~/.bashrc ou ~/.zshrc
export PATH="$PATH:/caminho/para/spring-scaffold"
```

## 📖 Como Usar

### Execução Básica
```bash
# Via script (recomendado)
./spring-scaffold --help

# Via JAR diretamente
java -jar target/spring-scaffold.jar --help
```

### Comandos Disponíveis

#### 1. Gerar Model/Entity
```bash
# Exemplo básico
./spring-scaffold model User

# Com campos específicos
./spring-scaffold model Product -f "name:String,price:BigDecimal,active:Boolean"

# Com configurações personalizadas
./spring-scaffold model Customer \
  -p com.mycompany.model \
  -t customer_table \
  --validation
```

**Opções do comando `model`:**
- `-p, --package`: Pacote da classe (padrão: com.example.model)
- `-f, --fields`: Campos no formato "nome:tipo,nome:tipo"
- `-t, --table`: Nome da tabela
- `--jpa`: Incluir annotations JPA (padrão: true)
- `--lombok`: Usar Lombok (padrão: true)
- `--validation`: Incluir Bean Validation (padrão: false)
- `-o, --output`: Diretório de saída

#### 2. Gerar Controller
```bash
# Exemplo básico
./spring-scaffold controller UserController

# Com model específico
./spring-scaffold controller ProductController -m Product

# Com configurações personalizadas
./spring-scaffold controller CustomerController \
  -m Customer \
  --path /api/v2 \
  --swagger
```

**Opções do comando `controller`:**
- `-p, --package`: Pacote do controller
- `-m, --model`: Nome da classe model associada
- `--path`: Path base da API (padrão: /api/v1)
- `--crud`: Incluir operações CRUD (padrão: true)
- `--swagger`: Incluir annotations Swagger (padrão: true)
- `--validation`: Incluir validações (padrão: true)

#### 3. Gerar Service
```bash
# Exemplo básico
./spring-scaffold service UserService

# Com model específico
./spring-scaffold service ProductService -m Product

# Sem interface
./spring-scaffold service CustomerService --no-interface
```

**Opções do comando `service`:**
- `-p, --package`: Pacote do service
- `-m, --model`: Nome da classe model associada
- `--interface`: Gerar interface (padrão: true)
- `--crud`: Incluir métodos CRUD (padrão: true)
- `--transactional`: Incluir @Transactional (padrão: true)

#### 4. Gerar Repository
```bash
# Exemplo básico
./spring-scaffold repository UserRepository

# Com model específico
./spring-scaffold repository ProductRepository -m Product

# MongoDB
./spring-scaffold repository CustomerRepository --type mongodb
```

**Opções do comando `repository`:**
- `-p, --package`: Pacote do repository
- `-m, --model`: Nome da classe model associada
- `--type`: Tipo (JPA, MONGODB, REACTIVE_MONGO, REACTIVE_R2DBC)
- `--id-type`: Tipo do ID (padrão: Long)
- `--custom-queries`: Incluir queries customizadas (padrão: true)
- `--pagination`: Incluir paginação (padrão: true)

#### 5. Criar Projeto Completo
```bash
# Projeto básico
./spring-scaffold project my-app

# Projeto com configurações personalizadas
./spring-scaffold project ecommerce-api \
  --package com.mycompany.ecommerce \
  --dependencies web,jpa,security,validation \
  --database postgresql \
  --docker
```

**Opções do comando `project`:**
- `--package`: Pacote base (padrão: com.example)
- `--spring-version`: Versão do Spring Boot (padrão: 3.2.0)
- `--java-version`: Versão do Java (padrão: 17)
- `--dependencies`: Dependências separadas por vírgula
- `--database`: Tipo de banco (H2, MYSQL, POSTGRESQL, MONGODB)
- `--docker`: Incluir arquivos Docker (padrão: true)
- `--gitignore`: Incluir .gitignore (padrão: true)

## 📂 Estrutura de Arquivos Gerados

### Model
```java
package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String name;
    
    @Column
    private String email;
}
```

### Controller
```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "API para gerenciamento de User")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @Operation(summary = "Lista todos os Users")
    public ResponseEntity<List<User>> findAll() {
        // Implementação CRUD
    }
    
    // Outros endpoints...
}
```

## 🔧 Desenvolvimento e Personalização

### Estrutura do Projeto
```
spring-scaffold-cli/
├── src/main/java/com/scaffold/
│   ├── SpringScaffoldCLI.java        # Classe principal
│   ├── commands/                     # Comandos CLI
│   ├── generators/                   # Geradores de código
│   ├── models/                      # Modelos de dados
│   └── utils/                       # Utilitários
├── src/main/resources/
│   ├── templates/                   # Templates Mustache
│   └── logback.xml                  # Configuração de logging
├── target/
│   └── spring-scaffold.jar         # JAR executável
├── spring-scaffold                  # Script de execução
├── pom.xml                         # Configuração Maven
└── README.md                       # Documentação
```

### Adicionando Novos Comandos

1. **Criar classe Command:**
```java
@Command(name = "novo-comando", description = "Descrição do comando")
public class NovoCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        // Implementação
        return 0;
    }
}
```

2. **Registrar no SpringScaffoldCLI:**
```java
@Command(subcommands = {
    // Comandos existentes...
    NovoCommand.class
})
```

3. **Criar Generator correspondente:**
```java
public class NovoGenerator {
    public boolean generate(/* parâmetros */) {
        // Lógica de geração
    }
}
```

4. **Criar template Mustache:**
```mustache
// Template em src/main/resources/templates/
```

### Personalizando Templates

Os templates estão em `src/main/resources/templates/` e usam sintaxe Mustache:

```mustache
package {{packageName}};

{{#imports}}
import {{.}};
{{/imports}}

{{#annotations}}
{{.}}
{{/annotations}}
public class {{className}} {
    {{#fields}}
    private {{type}} {{name}};
    {{/fields}}
}
```

## 🐛 Solução de Problemas

### Problemas Comuns

1. **Erro de compilação Java:**
   - Verifique se está usando Java 17+
   - Execute `java -version`

2. **JAR não encontrado:**
   - Execute `mvn clean package` para gerar o JAR

3. **Permissão negada no script:**
   - Execute `chmod +x spring-scaffold`

4. **Conflitos de dependências:**
   - Limpe o cache: `mvn clean`
   - Recompile: `mvn compile`

### Logs e Debug

```bash
# Modo verboso
./spring-scaffold -v model User

# Logs são salvos em spring-scaffold.log
tail -f spring-scaffold.log
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Faça commits com mensagens claras
4. Abra um Pull Request

## 📞 Suporte

- **Issues**: Reporte bugs no GitHub
- **Documentação**: Este README e javadocs no código
- **Exemplos**: Diretório `examples/` (se existir)

## 🏆 Próximos Passos

### Implementações Futuras
- [ ] Templates reais com Mustache
- [ ] Geração de arquivos físicos
- [ ] Suporte a diferentes bancos de dados
- [ ] Templates customizáveis pelo usuário
- [ ] Integração com Spring Initializr
- [ ] Testes automatizados
- [ ] Plugin para IDEs

### Roadmap
1. **Fase 1**: Implementação completa dos generators
2. **Fase 2**: Sistema de templates customizáveis
3. **Fase 3**: Interface gráfica opcional
4. **Fase 4**: Plugins e extensões

---

**Versão**: 1.0.0  
**Última atualização**: Agosto 2025  
**Licença**: MIT
