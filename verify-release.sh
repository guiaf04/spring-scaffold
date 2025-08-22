#!/bin/bash

# Spring Scaffold CLI v2.1.0 Release Verification Script
# This script verifies that the v2.1.0 release is ready

echo "🚀 Spring Scaffold CLI v2.1.0 Release Verification"
echo "=================================================="

# Check JAR exists and size
if [ -f "target/spring-scaffold.jar" ]; then
    JAR_SIZE=$(ls -lh target/spring-scaffold.jar | awk '{print $5}')
    echo "✅ JAR exists: target/spring-scaffold.jar ($JAR_SIZE)"
else
    echo "❌ JAR not found: target/spring-scaffold.jar"
    exit 1
fi

# Test basic functionality
echo ""
echo "🧪 Testing basic functionality..."
java -jar target/spring-scaffold.jar --version 2>/dev/null || {
    echo "❌ JAR version check failed"
    exit 1
}
echo "✅ JAR version check passed"

# Test new v2.1.0 syntax (positional parameters)
echo ""
echo "🧪 Testing new v2.1.0 syntax (positional parameters)..."
TEMP_DIR=$(mktemp -d)
cd "$TEMP_DIR"

java -jar "$OLDPWD/target/spring-scaffold.jar" model TestUser name:String email:String --pkg com.test.entity &>/dev/null

if [ -f "src/main/java/com/test/entity/TestUser.java" ]; then
    echo "✅ Positional parameters syntax working"
    cd "$OLDPWD"
    rm -rf "$TEMP_DIR"
else
    echo "❌ Positional parameters syntax failed"
    cd "$OLDPWD"
    rm -rf "$TEMP_DIR"
    exit 1
fi

# Test alias syntax
echo ""
echo "🧪 Testing alias syntax..."
TEMP_DIR=$(mktemp -d)
cd "$TEMP_DIR"

java -jar "$OLDPWD/target/spring-scaffold.jar" model User name:String --pkg com.example.entity &>/dev/null

if [ -f "src/main/java/com/example/entity/User.java" ]; then
    echo "✅ Alias syntax working (--pkg)"
    cd "$OLDPWD"
    rm -rf "$TEMP_DIR"
else
    echo "❌ Alias syntax failed"
    cd "$OLDPWD"
    rm -rf "$TEMP_DIR"
    exit 1
fi

# Check release notes exist
echo ""
echo "📝 Checking release documentation..."
if [ -f "releases/v2.1.0.md" ]; then
    echo "✅ Release notes exist: releases/v2.1.0.md"
else
    echo "❌ Release notes missing: releases/v2.1.0.md"
    exit 1
fi

# Check version in pom.xml
POM_VERSION=$(grep -E '<version>.*</version>' pom.xml | head -1 | sed 's/.*<version>\(.*\)<\/version>.*/\1/' | tr -d ' ')
if [ "$POM_VERSION" = "2.1.0" ]; then
    echo "✅ Version in pom.xml: $POM_VERSION"
else
    echo "❌ Version mismatch in pom.xml: $POM_VERSION (expected: 2.1.0)"
    exit 1
fi

echo ""
echo "🎉 All checks passed! Spring Scaffold CLI v2.1.0 is ready for release"
echo ""
echo "📋 Release Summary:"
echo "  - JAR: target/spring-scaffold.jar ($JAR_SIZE)"
echo "  - Version: $POM_VERSION"
echo "  - New features tested: ✅ Positional parameters, ✅ Aliases"
echo "  - Release notes: ✅ releases/v2.1.0.md"
echo ""
echo "🚀 Next steps:"
echo "  1. Go to: https://github.com/guiaf04/spring-scaffold/releases"
echo "  2. Create new release with tag: v2.1.0"
echo "  3. Upload: target/spring-scaffold.jar"
echo "  4. Copy release notes from: releases/v2.1.0.md"
echo ""