# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/spec/v2.0.0.html).

## [2.1.3] - 2025-08-23

### Fixed
- 🔧 **Pipeline CI/CD**: Melhorado o manuseio de chaves GPG e configuração de deploy no Maven
- 🛠️ **Workflow de Release**: Configuração aprimorada do agente GPG e gerenciamento de settings Maven
- 📦 **Deploy**: Corrigidos problemas de assinatura GPG no processo de deploy do Maven Central

## [2.1.2] - 2025-08-23

### Fixed
- 📝 **Documentação**: Atualizadas as etapas do workflow do SonarQube e OWASP Dependency Check
- 🔑 **Configuração**: Adicionadas instruções claras para habilitar requisitos de chave de API
- 🔧 **CI/CD**: Melhorada a clareza do workflow e orientação de configuração

## [2.1.1] - 2025-08-23

### Added
- 🚀 **Pipeline CI/CD**: Implementação completa de workflows do GitHub Actions
  - Workflow de CI com testes automatizados e relatórios de teste
  - Workflow de qualidade com integração SonarCloud e scanning de segurança  
  - Workflow de release com deploy no Maven Central e assinatura GPG
- 📋 **Templates de Projeto**: Adicionados templates e configuração do GitHub
  - Template de pull request com formato estruturado
  - Configuração do Dependabot para atualizações automáticas de dependências
  - Settings Maven e arquivo de licença

### Fixed
- 🔧 **Sistema de Build**: Configuração Maven aprimorada para publicação no Central
- 📦 **Processo de Release**: Implementado pipeline adequado de assinatura GPG e deploy

## [2.1.0] - 2025-08-22

### Added
- ✨ **Nova sintaxe intuitiva**: Parâmetros posicionais para o comando model
  - Suporte a `spring-scaffold model User name:String email:String age:Integer`
  - Combinação flexível entre parâmetros posicionais e opção `-f`
- ⚡ **Aliases curtos** para todos os comandos:
  - `--pkg` para `--package`
  - `--deps` para `--dependencies` 
  - `--entity` para `--model`
  - `--db` para `--database`
  - Atalhos de uma letra: `-p`, `-m`, `-t`, `-d`, `-g`, `-s`, `-j`
- 🎯 **Melhorias de usabilidade** em todos os comandos:
  - ModelCommand: `--pkg`, `--entity`, `--data`, `--valid` + parâmetros posicionais
  - ProjectCommand: `--pkg`, `--deps`, `--db`, `-p`, `-g`, `-s`, `-j`, `-d`
  - ControllerCommand: `--pkg`, `--entity` + aliases existentes
  - ServiceCommand: `--pkg`, `--entity`, `--repo-pkg`
  - RepositoryCommand: `--pkg`, `--entity`, `-t`
- 📝 **Documentação aprimorada**:
  - README.md atualizado com exemplos da nova sintaxe
  - Comparações entre sintaxe antiga e nova
  - Demonstração de economia de digitação (até 50%)

### Changed
- 🔄 **100% retrocompatível**: Todas as sintaxes antigas continuam funcionando
- 📊 **Produtividade melhorada**: Desenvolvimento até 50% mais rápido
- 🎨 **Help melhorado**: Comandos mostram tanto aliases quanto nomes completos

### Fixed
- 🐛 Melhorado parsing de campos no ModelCommand para suportar múltiplas fontes
- 🔧 Validação aprimorada de parâmetros combinados

## [2.0.0] - 2025-08-22

### Added
- ✨ Implementação completa de geração real de arquivos em todos os comandos
- 🎯 ControllerGenerator com template Mustache completo
  - Operações CRUD completas
  - Suporte a Swagger/OpenAPI annotations
  - Validações Jakarta
  - Endpoints RESTful com ResponseEntity
- 🔧 ServiceGenerator com opções flexíveis
  - Geração de interface + implementação ou só classe
  - Suporte a transações (@Transactional)
  - Integração com Repository
  - CRUD operations padrão
- 📦 RepositoryGenerator para diferentes tipos
  - JpaRepository, MongoRepository, ReactiveRepository
  - Queries customizadas opcionais
  - Suporte a paginação
- 🏗️ ProjectGenerator para projetos completos
  - Estrutura Maven completa
  - Templates para pom.xml, application.properties
  - Classe principal Spring Boot
  - Testes unitários básicos
  - Arquivos opcionais: Dockerfile, .gitignore, README.md
  - Suporte a múltiplos bancos (H2, MySQL, PostgreSQL)

### Fixed
- 🐛 Corrigido uso incorreto do FileUtils (writeFile → createFile)
- 🐛 Corrigido enum switch no RepositoryGenerator
- 🐛 Corrigido imports e sintaxe em todos os geradores

### Changed
- 🔄 **BREAKING**: Mudança completa na implementação dos geradores
- 📝 Templates Mustache reescritos para melhor funcionalidade
- 🎨 Melhoradas mensagens de log e feedback do usuário

### Deprecated
- Nenhuma funcionalidade depreciada nesta versão

### Removed
- Nenhuma funcionalidade removida nesta versão

### Security
- Nenhuma correção de segurança nesta versão

## [1.0.0] - 2025-08-22

### Added
- 🎉 Versão inicial do Spring Scaffold CLI
- 📋 Comando `model` para geração de entidades JPA
- 🎛️ Comando `controller` básico (apenas estrutura)
- ⚙️ Comando `service` básico (apenas estrutura)
- 🗄️ Comando `repository` básico (apenas estrutura)
- 🏗️ Comando `project` básico (apenas estrutura)
- 🎨 Sistema de templates com Mustache
- 📝 Logging configurado com Logback
- 🔧 Estrutura CLI com Picocli
- 📦 Build com Maven Shade Plugin

### Known Issues
- ⚠️ Comandos controller, service, repository e project apenas simulavam geração
- ⚠️ Templates incompletos para alguns comandos
- ⚠️ Parsing de campos complexos limitado

---

## Tipos de Mudanças
- `Added` para novas funcionalidades
- `Changed` para mudanças em funcionalidades existentes
- `Deprecated` para funcionalidades que serão removidas em breve
- `Removed` para funcionalidades removidas
- `Fixed` para correções de bugs
- `Security` para correções de segurança
