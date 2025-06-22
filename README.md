#  Test Automation Framework - Syarah.com

A robust Java-based test automation framework built with **Cucumber**, **TestNG**, **Selenium WebDriver**, and **Extent Reports** for testing Syarah.com's car search and finance eligibility features.

##  What This Framework Does

This framework automates testing for two main features of Syarah.com:

1. **Car Search Functionality** - Search for cars by brand and year
2. **Finance Eligibility Application** - Apply for car financing with different criteria

The framework runs tests in a **two-phase execution** system where finance tests only run after car search tests complete successfully.

##  Tech Stack

- **Java 11+** - Core programming language
- **Maven** - Dependency management and build tool
- **Selenium WebDriver 4.22.0** - Browser automation
- **Cucumber 7.14.0** - BDD (Behavior Driven Development) framework
- **TestNG 7.8.0** - Test execution and parallelization
- **Extent Reports 5.0.9** - Beautiful HTML test reports
- **WebDriverManager 5.9.1** - Automatic WebDriver management

##  Prerequisites

Before you start, make sure you have:

- **Java JDK 11 or higher** installed
- **Maven 3.6+** installed
- **Chrome browser** installed (framework uses Chrome by default)
- **Git** for cloning the repository

### Quick Check
```bash
java -version
mvn -version
git --version
```

##  Getting Started

### 1. Clone the Repository

```bash
git clone 
cd SYTask
```

### 2. Project Structure

```
SYTask/
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   ├── base/           # Base test classes
│   │   │   ├── pages/          # Page Object Model classes
│   │   │   │   ├── care/       # Car search page objects
│   │   │   │   └── finance/    # Finance eligibility page objects
│   │   │   ├── runners/        # TestNG runners
│   │   │   ├── steps/          # Cucumber step definitions
│   │   │   └── utils/          # Utility classes
│   │   └── resources/
│   │       ├── features/       # Cucumber feature files
│   │       ├── config.properties
│   │       ├── extent-config.xml
│   │       └── testng.xml
├── pom.xml
└── README.md
```

### 3. Configuration

The framework uses `src/test/resources/config.properties` for configuration:

```properties
browser=chrome
baseUrl=https://preprod.syarah.com/en
testng.parallel=methods
testng.thread.count=2
prerequisite.thread.count=3
```

## Running Tests

### Option 1: Run All Tests (Recommended)
```bash
mvn clean test
```

This runs the complete test suite in two phases:
1. **Phase 1**: Car Search tests (prerequisites)
2. **Phase 2**: Finance Eligibility tests (runs only after Phase 1 completes)

### Option 2: Run Specific Test Categories

**Run only Car Search tests:**
```bash
mvn test -Dtest=CareSearchRunner
```

**Run only Finance tests:**
```bash
mvn test -Dtest=FinanceRunner
```

### Option 3: Run with Different Browser
```bash
mvn test -Dbrowser=firefox
```

## Test Reports

After test execution, you'll find reports in:

- **Extent Reports**: `target/extent-report/ExtentSpark.html`
- **TestNG Reports**: `target/surefire-reports/`
- **Cucumber Reports**: `target/cucumber-reports-care.html` and `target/cucumber-reports-finance.html`

### Viewing Reports
1. Open `target/extent-report/ExtentSpark.html` in your browser
2. Reports include screenshots, step details, and test results
3. Steps are collapsible for better readability

##  Available Test Features

### 1. Car Search Tests (`@care` tag)
- **Feature**: Search cars by brand and year
- **Scenarios**: 
  - Search Toyota cars from 2022-2025
- **Location**: `src/test/resources/features/care_search.feature`

### 2. Finance Eligibility Tests (`@finance` tag)
- **Feature**: Apply for car financing
- **Scenarios**:
  - **New Car - Government Sector** (`@SYT-NC-GOV`)
  - **New Car - Private Sector** (`@SYT-NC-PVT`)
  - **Used Car - Government Sector** (`@SYT-UC-GOV`)
  - **Used Car - Private Sector** (`@SYT-UC-PVT`)

Each scenario tests different eligibility criteria based on:
- Car type (new/used)
- Employment sector (government/private)
- Monthly income
- Financial obligations
- Social insurance status
- Job tenure

## Framework Features

###  Parallel Execution
- Tests run in parallel for faster execution
- Configurable thread counts per phase
- Thread-safe WebDriver management

###  Dependency Management
- Finance tests run only after car search tests complete
- Prevents test failures due to missing prerequisites

###  Screenshot Capture
- Automatic screenshots on test failures
- Screenshots attached to Extent Reports
- Proper file path management

###  Page Object Model
- Clean separation of test logic and page interactions
- Reusable page objects for maintainability

### Configuration Management
- Centralized configuration via properties file
- Environment-specific settings support


##  Adding New Tests

### 1. Create Feature File
Add new `.feature` files in `src/test/resources/features/`

### 2. Create Step Definitions
Add step definition classes in `src/test/java/steps/`

### 3. Create Page Objects
Add page object classes in `src/test/java/pages/`

### 4. Update TestNG Configuration
Add new runners to `src/test/resources/testng.xml` if needed

