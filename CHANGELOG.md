# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/spec/v2.0.0.html).

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
