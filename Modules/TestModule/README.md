# Test Module

## About

A module used for development testing and should not be used on live servers. This allows us to test new feature or changes to module manipulation without touching core modules. This is however a good example to look at if you are wanting to create your own module.

## Commands

 * `/test` - A command to test command handling inside modules

## Structure

### Directories

| Directory          | Description         |
|--------------------|---------------------|
|`src/main/java`     | Source Directory    |
|`src/main/resources`| Resources Directory |

### Classes

| Class                                          | Description                        |
|------------------------------------------------|------------------------------------|
|`uk.codingbadgers.testmodule.TestModule`        |Main class for the module           |
|`uk.codingbadgers.testmodule.TestCommandHandler`|Handler code for the `/test` command|
