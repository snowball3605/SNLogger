# SNLogger
A simple colored logging utility for Java

## Introduction
This is a simple log output, which allows you to view the program's execution or check for errors.

## How to use?
Maven:
```
<dependency>
    <groupId>com.onmi-tech</groupId>
    <artifactId>SNLogger</artifactId>
    <version>1.0.1</version>
</dependency>
```

Gradle:
```
implementation group: 'com.onmi-tech', name: 'SNLogger', version: '1.0.1'
```

## Getting Started
```
SNLogger snLogger = new SNLogger(LogLevel.DEBUG); // Only logs reaching this level will be output
snLogger.debug("input debug");
snLogger.info("input info");
snLogger.warn("input warn");
snLogger.error("input error");
```
