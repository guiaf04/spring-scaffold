#!/bin/bash

# Test script for Spring Scaffold CLI templates
# This script generates a complete project and validates that all templates work correctly

set -e  # Exit on any error

echo "🧪 Testing Spring Scaffold CLI Templates..."
echo "=========================================="

# Test directory
TEST_DIR="/tmp/spring-scaffold-test"
PROJECT_NAME="test-project"
PROJECT_PATH="$TEST_DIR/$PROJECT_NAME"

# Clean up any previous test
if [ -d "$TEST_DIR" ]; then
    echo "🧹 Cleaning up previous test..."
    rm -rf "$TEST_DIR"
fi

# Create test directory
mkdir -p "$TEST_DIR"
cd "$TEST_DIR"

echo "📁 Test directory: $TEST_DIR"
echo ""

# Build the CLI first
echo "🔨 Building Spring Scaffold CLI..."
cd /home/guilherme/IdeaProjects/spring-scaffold
mvn clean package -q
if [ $? -ne 0 ]; then
    echo "❌ Failed to build Spring Scaffold CLI"
    exit 1
fi
echo "✅ CLI built successfully"
echo ""

# Go back to test directory
cd "$TEST_DIR"

# Test 1: Generate a complete Spring Boot project
echo "🏗️  Test 1: Generating Spring Boot project..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar project \
    "$PROJECT_NAME" \
    --package "com.test.scaffold" \
    --database H2 \
    --dependencies "web,jpa,h2,validation,lombok,swagger" > /dev/null 2>&1

if [ ! -d "$PROJECT_PATH" ]; then
    echo "❌ Project directory not created"
    exit 1
fi
echo "✅ Project generated successfully"

# Enter project directory
cd "$PROJECT_PATH"

# Test 2: Generate model (using relative package)
echo "🏗️  Test 2: Generating model..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar model \
    "User" \
    "name:String" "email:String" "age:Integer" \
    --package "model" \
    --validation \
    --lombok > /dev/null 2>&1

if [ ! -f "src/main/java/com/test/scaffold/model/User.java" ]; then
    echo "❌ Model not generated"
    exit 1
fi
echo "✅ Model generated successfully"

# Test 3: Generate repository (using relative package)
echo "🏗️  Test 3: Generating repository..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar repository \
    "UserRepository" \
    --model "User" \
    --type JPA \
    --package "repository" > /dev/null 2>&1

if [ ! -f "src/main/java/com/test/scaffold/repository/UserRepository.java" ]; then
    echo "❌ Repository not generated"
    exit 1
fi
echo "✅ Repository generated successfully"

# Test 4: Generate service (using relative package)
echo "🏗️  Test 4: Generating service..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar service \
    "UserService" \
    --model "User" \
    --package "service" \
    --crud \
    --transactional > /dev/null 2>&1

if [ ! -f "src/main/java/com/test/scaffold/service/UserService.java" ]; then
    echo "❌ Service not generated"
    exit 1
fi
echo "✅ Service generated successfully"

# Test 5: Generate controller (using relative package)
echo "🏗️  Test 5: Generating controller..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar controller \
    "UserController" \
    --model "User" \
    --package "controller" \
    --crud \
    --swagger \
    --validation > /dev/null 2>&1

if [ ! -f "src/main/java/com/test/scaffold/controller/UserController.java" ]; then
    echo "❌ Controller not generated"
    exit 1
fi
echo "✅ Controller generated successfully"

# Test 6: Verify project structure
echo "🔍 Test 6: Verifying project structure..."
EXPECTED_FILES=(
    "pom.xml"
    "src/main/java/com/test/scaffold/TestProjectApplication.java"
    "src/main/resources/application.properties"
    "src/test/java/com/test/scaffold/TestProjectApplicationTests.java"
    "Dockerfile"
    ".gitignore"
    "README.md"
    "src/main/java/com/test/scaffold/model/User.java"
    "src/main/java/com/test/scaffold/repository/UserRepository.java"
    "src/main/java/com/test/scaffold/service/UserService.java"
    "src/main/java/com/test/scaffold/controller/UserController.java"
)

for file in "${EXPECTED_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo "❌ Missing file: $file"
        exit 1
    fi
done
echo "✅ All expected files present"

# Test 7: Verify Maven compilation
echo "🔨 Test 7: Testing Maven compilation..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "❌ Maven compilation failed"
    exit 1
fi
echo "✅ Maven compilation successful"

# Test 8: Verify test compilation and execution
echo "🧪 Test 8: Running unit tests..."
mvn test -q
if [ $? -ne 0 ]; then
    echo "❌ Unit tests failed"
    exit 1
fi
echo "✅ Unit tests passed"

# Test 9: Verify package creation
echo "📦 Test 9: Testing package creation..."
mvn package -q -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Package creation failed"
    exit 1
fi
echo "✅ Package created successfully"

# Test 10: Verify generated code quality (basic checks)
echo "🔍 Test 10: Verifying generated code quality..."

# Check for English text (no Portuguese)
if grep -r "buscando\|salvando\|excluindo\|gerado\|criado\|configuração" src/ 2>/dev/null; then
    echo "❌ Found Portuguese text in generated code"
    exit 1
fi

# Check for proper imports
if ! grep -q "import org.springframework" src/main/java/com/test/scaffold/TestProjectApplication.java; then
    echo "❌ Missing Spring imports in main application"
    exit 1
fi

# Check for Swagger annotations if enabled
if ! grep -q "@Tag\|@Operation" src/main/java/com/test/scaffold/controller/UserController.java; then
    echo "❌ Missing Swagger annotations in controller"
    exit 1
fi

echo "✅ Generated code quality checks passed"

# Test 11: Demonstrate automatic package detection
echo "🎯 Test 11: Testing automatic package detection..."
AUTO_PACKAGE_DIR="/tmp/demo-auto-package"
ECOMMERCE_PROJECT="my-ecommerce"
ECOMMERCE_PATH="$AUTO_PACKAGE_DIR/$ECOMMERCE_PROJECT"

# Clean up any previous auto package test
if [ -d "$AUTO_PACKAGE_DIR" ]; then
    rm -rf "$AUTO_PACKAGE_DIR"
fi

# Create auto package test directory
mkdir -p "$AUTO_PACKAGE_DIR"
cd "$AUTO_PACKAGE_DIR"

echo "📦 Testing automatic package detection with e-commerce project..."

# Generate e-commerce project
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar project \
    "$ECOMMERCE_PROJECT" \
    --package "com.loja.ecommerce" \
    --database H2 \
    --dependencies "web,jpa,h2,validation,lombok,swagger" > /dev/null 2>&1

if [ ! -d "$ECOMMERCE_PATH" ]; then
    echo "❌ E-commerce project directory not created"
    exit 1
fi

cd "$ECOMMERCE_PATH"

# Test with relative package names (demonstrating automatic detection)
echo "   🏷️  Generating Product model with relative package..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar model \
    "Product" \
    "name:String" "price:BigDecimal" "description:String" "category:String" \
    --package "model" \
    --validation \
    --lombok > /dev/null 2>&1

if [ ! -f "src/main/java/com/loja/ecommerce/model/Product.java" ]; then
    echo "❌ Product model not generated with automatic package detection"
    exit 1
fi

echo "   💾 Generating ProductRepository with relative package..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar repository \
    "ProductRepository" \
    --model "Product" \
    --type JPA \
    --package "repository" > /dev/null 2>&1

if [ ! -f "src/main/java/com/loja/ecommerce/repository/ProductRepository.java" ]; then
    echo "❌ ProductRepository not generated with automatic package detection"
    exit 1
fi

echo "   ⚙️  Generating ProductService with default package (no --package)..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar service \
    "ProductService" \
    --model "Product" \
    --crud \
    --transactional > /dev/null 2>&1

if [ ! -f "src/main/java/com/loja/ecommerce/service/ProductService.java" ]; then
    echo "❌ ProductService not generated with default package detection"
    exit 1
fi

echo "   🌐 Generating ProductController with default package (no --package)..."
java -jar /home/guilherme/IdeaProjects/spring-scaffold/target/spring-scaffold.jar controller \
    "ProductController" \
    --model "Product" \
    --crud \
    --swagger \
    --validation > /dev/null 2>&1

if [ ! -f "src/main/java/com/loja/ecommerce/controller/ProductController.java" ]; then
    echo "❌ ProductController not generated with default package detection"
    exit 1
fi

# Test compilation of auto-detected packages
echo "   🔨 Testing compilation with auto-detected packages..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed with auto-detected packages"
    exit 1
fi

echo "✅ Automatic package detection tests passed"

# Cleanup auto package test
rm -rf "$AUTO_PACKAGE_DIR"

# Cleanup main test
echo ""
echo "🧹 Cleaning up test files..."
cd /
rm -rf "$TEST_DIR"

echo ""
echo "🎉 All template tests passed successfully!"
echo "   ✅ Project generation"
echo "   ✅ Model generation"
echo "   ✅ Repository generation"
echo "   ✅ Service generation"
echo "   ✅ Controller generation"
echo "   ✅ File structure validation"
echo "   ✅ Maven compilation"
echo "   ✅ Unit tests"
echo "   ✅ Package creation"
echo "   ✅ Code quality checks"
echo "   ✅ Automatic package detection"
echo ""
echo "🚀 All templates are working correctly!"
echo ""
echo "📋 AUTOMATIC PACKAGE DETECTION SUMMARY:"
echo "   🎯 Base package auto-detection: ✅"
echo "   🏷️  Relative packages (e.g., 'model'): ✅"
echo "   📦 Default packages when not specified: ✅"
echo "   🔗 Cross-component package coordination: ✅"
echo "   ⚡ Enhanced developer productivity: ✅"
