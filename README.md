![Cognifide logo](docs/cognifide-logo.png)

[![Gradle Status](https://gradleupdate.appspot.com/Cognifide/gradle-aem-multi/status.svg?random=456)](https://gradleupdate.appspot.com/Cognifide/gradle-aem-multi/status)
[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/Cognifide/gradle-aem-multi.svg?label=License)](http://www.apache.org/licenses/)

# AEM Multi-Project Example

<br>
<p align="center">
  <img src="docs/logo.png" alt="Gradle AEM Plugin Logo"/>
</p>
<br>

## Screenshot

<p align="center">
  <img src="docs/gradle-aem-multi-build.gif" alt="Gradle AEM Multi Build"/>
</p>

## Description

This project could be used to start developing **project** based on AEM.

To start developing **application** based on AEM it is recommended to use [Gradle AEM Single](https://github.com/Cognifide/gradle-aem-single) instead.

Documentation for AEM plugin is available in project [Gradle AEM Plugin](https://github.com/Cognifide/gradle-aem-plugin).

## Important notice 

Gradle AEM Plugin 6.x serie and upper will **no longer support Groovy DSL** and **stands on Kotlin DSL** coming with Gradle 5.0.

Documentation for:

* [migrating Groovy DSL to Kotlin DSL](https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin) (official Gradle docs).
* [previous 5.x serie](https://github.com/Cognifide/gradle-aem-multi/tree/groovy) (navigates to branch `groovy`),

## Table of Contents

* [Quickstart](#quickstart)
* [Environment](#environment)
* [Structure](#structure)
* [Features](#features)
* [Building](#building)
* [Tips &amp; tricks](#tips--tricks)
* [Running tests](#running-tests)
* [Attaching debugger](#attaching-debugger)
* [Extending build](#extending-build)

## Quickstart

1. Fork project using command:

    ```bash
    git clone git@github.com:Cognifide/gradle-aem-multi.git && cd gradle-aem-multi && gradlew fork
    ```

    and specify properties:

    ![Fork Props Dialog](docs/fork-default-dialog.png)
    
    and wait until project is forked then enter configured target directory.

2. Setup user specific project configuration using command:

    ```bash
    gradlew props
    ```
    
    and specify properties:

    ![Fork Props Dialog](docs/fork-props-dialog.png)

3. Setup local AEM instances with dependencies and AEM dispatcher (see [prerequisites](https://github.com/Cognifide/gradle-aem-plugin/tree/develop#environment-configuration)) then build application using command:

    ```bash
    aem/hosts
    gradlew setup
    ```
    
    and wait till complete AEM environment will be ready to use.
  
4. Develop continuously application using command:

    ```bash
    gradlew
    ```
    
    or to just deploy AEM application (without running anything else):
    
    ```bash
    gradlew :aem:assembly:full:packageDeploy
    ```

## Prerequisites

Tested on:

* Java 1.8
* Gradle 5.4.1
* Adobe AEM 6.5
* Docker 2.0.0.3

## Structure

Project is divided into subpackages (designed with reinstallabilty on production environments in mind):

* *aem/assembly/full* - non-reinstallable complete all-in-one package with application and contents (combination of subpackages: all). Useful to deploy all code by installing single package in a project stage when application is not live.
* *aem/assembly/app* - reinstallable assembly package that contains only application code, not content (combination of subpackages: *common*, *sites*). Useful to deploy application code only in a project stage when application is live and content should remain untouched on production server.

* *aem/common* - OSGi bundle with integrations of libraries needed by other bundles and global AEM extensions (dialogs, form controls etc). Only code unrelated to any site / AEM platform wide.
* *aem/sites* - AEM sites module extension consisting of site specific code like: OSGi bundle with business logic, AEM components, templates, design.
* *aem/site.demo* - consists of extra AEM pages that presents features of application (useful for testing). Helps application testers and developers in QA/UAT application feature tests.
* *aem/site.live* - contains minimal set of pages needed initially to rollout new site(s) using installed application. Helps content authors to start working with application.

## Features

* Integrated [Fork Plugin](https://github.com/neva-dev/gradle-fork-plugin) / project generator based on live archetypes.
* Interoperable Java and [Kotlin](https://kotlinlang.org) code examples.
* Integrated popular UI build toolkit: [NodeJS](https://nodejs.org/en/), [Yarn](https://yarnpkg.com) and [Webpack](https://webpack.github.io/) for advanced assets bundling (modular JS, ECMAScript6 transpilation, SCSS compilation with [PostCSS](http://postcss.org), code style checks etc).
* Integrated SCSS compilation on AEM side using [AEM Sass Compiler](https://github.com/mickleroy/aem-sass-compiler).
* Integrated popular AEM testing toolkit: [wcm.io Testing](http://wcm.io/testing).
* Example configuration for [embedding OSGi bundles into CRX package](aem/common/build.gradle.kts) (`embedPackage`).
* Example configuration for [installing dependant CRX packages on AEM](aem/gradle/environment.gradle.kts) before application deployment (`instanceSatisfy`).

## Environment

Project is configured to have local environment which consists of:

* native AEM instances running on local file system, 
* virtualized Apache HTTP Server with AEM Dispatcher module running on Docker ([official httpd image](https://hub.docker.com/_/httpd)).

Assumptions:

* AEM author available at [http://localhost:4502](http://localhost:4502)
* AEM publish available at [http://localhost:4503](http://localhost:4503)
* Apache web server with Virtual hosts configured for domains:
  * http://example.com -> which maps to `/content/example/live` content root on publish
  * http://demo.example.com -> which maps to `/content/example/demo` content root on publish
  * http://author.example.com -> which is proxy to the author instance

## Building

1. Use command `gradlew` so that Gradle in version according to project will be downloaded automatically.
2. Deploy application:
    * Full assembly and run all tests
        * `gradlew` <=> `:develop`
    * Only assembly packages:
        * `gradlew :aem:assembly:full:packageDeploy`
        * `gradlew :aem:assembly:app:packageDeploy`
    * Only single package: [
        * `gradlew :aem:sites:packageDeploy`,
        * `gradlew :aem:common:packageDeploy`,
        * `gradlew :aem:site.live:packageDeploy`,
        * `gradlew :aem:site.demo:packageDeploy`.

Build might look complicated, but to make a AEM development a breeze it just covers many things to be done  within single task execution like `setup` or `develop`.
Graphical visualisation of task graph for `resetup` task:

<br>
<p align="center">
  <img src="docs/resetup-graph.png" alt="Resetup task graph"/>
</p>
<br>

Task `setup` will:

* set up AEM instances (author & publish)
* set up AEM environment (run HTTPD service on Docker) and install AEM dispatcher module
* build AEM application (compose assembly CRX package from many)
* migrate AEM application (for projects already deployed on production to upgrade JCR content in case of changed application behavior)
* clean AEM environment (restart HTTPD service then clean AEM dispatcher caches)
* check AEM environment (quickly check responsiveness of deployed application)
* run integration tests
* run functional tests

To sum up, all things needed by developer are fully automated in one place / Gradle build. 
Still all separate concerns like running tests, only building application, only running tests, could be used separately by running particular Gradle tasks.

## Tips & tricks

* To run some task only for subproject, use project path as a prefix, for instance: `gradlew :aem:site.demo:sync`.
* According to [recommendations](https://docs.gradle.org/current/userguide/gradle_daemon.html), Gradle daemon should be: 
    * enabled on development environments,
    * disabled on continuous integration environments.
* To see more descriptive errors or want to skip some tasks, see command line [documentation](https://docs.gradle.org/current/userguide/command_line_interface.html).

## Running tests 

### IntelliJ

Certain unit tests may depend on the results of running gradle tasks. One such example is the testing of OSGi Services using [OSGi Mocks](https://sling.apache.org/documentation/development/osgi-mock.html) where in order to run a test, the SCR metadata must be available for a class. Running a test like this in IntelliJ results in errors because the IDE is not aware of the Bundle plugin.

This can be worked around by configuring IntelliJ to delegate test execution to Gradle. In order to set this up, go to _Settings > Build, Execution, Deployment > Gradle > Runner_ and set your IDE to delegate IDE build/run actions to Gradle. Alternatively, you can use a dropdown menu to use a specific runner or to decide on a test-by-test basis.

## Attaching debugger

1. Execute build with options `-Dorg.gradle.debug=true --no-daemon`, it will suspend,
2. Attach debugger on port 5005,
3. Suspension will be released and build should stop at breakpoint.

## Extending build

For defining new tasks directly in build see:

 * [Build Script Basics](https://docs.gradle.org/current/userguide/tutorial_using_tasks.html)
 * [More about Tasks](https://docs.gradle.org/current/userguide/more_about_tasks.html)

The easiest way to implement custom plugins and use them in project is a technique related with _buildSrc/_ directory.
For more details please read [documentation](https://docs.gradle.org/current/userguide/organizing_build_logic.html#sec:build_sources).
