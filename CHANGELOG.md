# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.0] - 2025-08-22

### Added
- ✨ **New intuitive syntax**: Positional parameters for model command
  - Support for `spring-scaffold model User name:String email:String age:Integer`
  - Flexible combination between positional parameters and `-f` option
- ⚡ **Short aliases** for all commands:
  - `--pkg` for `--package`
  - `--deps` for `--dependencies` 
  - `--entity` for `--model`
  - `--db` for `--database`
  - Single-letter shortcuts: `-p`, `-m`, `-t`, `-d`, `-g`, `-s`, `-j`
- 🎯 **Usability improvements** in all commands:
  - ModelCommand: `--pkg`, `--entity`, `--data`, `--valid` + positional parameters
  - ProjectCommand: `--pkg`, `--deps`, `--db`, `-p`, `-g`, `-s`, `-j`, `-d`
  - ControllerCommand: `--pkg`, `--entity` + existing aliases
  - ServiceCommand: `--pkg`, `--entity`, `--repo-pkg`
  - RepositoryCommand: `--pkg`, `--entity`, `-t`
- 📝 **Enhanced documentation**:
  - Updated README.md with new syntax examples
  - Comparisons between old and new syntax
  - Demonstration of typing savings (up to 50%)
- 🌍 **Internationalization**:
  - All main documentation now in English
  - Portuguese documentation moved to `docs/pt-BR/`
  - Code and commit messages standardized to English

### Changed
- 🔄 **100% backward compatible**: All old syntax continues to work
- 📊 **Improved productivity**: Up to 50% faster development
- 🎨 **Better help**: Commands show both aliases and full names

### Fixed
- 🐛 Improved field parsing in ModelCommand to support multiple input sources
- 🔧 Enhanced validation of combined parameters

## [2.0.0] - 2025-08-22

### Added
- ✨ Complete implementation of real file generation in all commands
- 🎯 ControllerGenerator with complete Mustache template
  - Complete CRUD operations
  - Swagger/OpenAPI annotations support
  - Jakarta validations
  - RESTful endpoints with ResponseEntity
- 🔧 ServiceGenerator with flexible options
  - Interface + implementation generation or single class
  - Transaction support (@Transactional)
  - Repository integration
  - Standard CRUD operations
- 📦 RepositoryGenerator for different types
  - JpaRepository, MongoRepository, ReactiveRepository
  - Optional custom queries
  - Pagination support
- 🏗️ ProjectGenerator for complete projects
  - Complete Maven structure
  - Templates for pom.xml, application.properties
  - Spring Boot main class
  - Basic unit tests
  - Optional files: Dockerfile, .gitignore, README.md
  - Multi-database support (H2, MySQL, PostgreSQL)

### Fixed
- 🐛 Fixed incorrect FileUtils usage (writeFile → createFile)
- 🐛 Fixed enum switch in RepositoryGenerator
- 🐛 Fixed imports and syntax in all generators

### Changed
- 🔄 **BREAKING**: Complete change in generator implementations
- 📝 Rewritten Mustache templates for better functionality
- 🎨 Improved log messages and user feedback

### Deprecated
- No deprecated functionality in this version

### Removed
- No removed functionality in this version

### Security
- No security-related changes in this version

## [1.0.0] - 2025-08-22

### Added
- 🎉 Initial CLI structure implementation
- 📋 Basic model generation functionality
- 🏗️ Foundation for Spring Boot scaffolding
- 🔧 Picocli integration for command-line interface
- 📝 Mustache template engine setup
- 🛠️ Maven build configuration

### Known Issues
- ⚠️ Commands other than model only simulated file generation
- 🔍 Limited template functionality
- 📋 Basic validation and error handling

---

For complete version history and migration guides, see individual release notes in the `releases/` directory.