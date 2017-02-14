SonarQube JSON Analyzer
=======================

[![Build Status](https://api.travis-ci.org/racodond/sonar-json-plugin.svg?branch=master)](https://travis-ci.org/racodond/sonar-json-plugin)
[![AppVeyor Build Status](https://ci.appveyor.com/api/projects/status/imfckm45thk6vvh4/branch/master?svg=true)](https://ci.appveyor.com/project/racodond/sonar-json-plugin/branch/master)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.codehaus.sonar-plugins.json:json)](https://sonarqube.com/overview?id=org.codehaus.sonar-plugins.json%3Ajson)
[![Twitter](https://img.shields.io/badge/Twitter-@racodond-blue.svg)](https://twitter.com/racodond)


## Description
This plugin enables code QA analysis of [JSON files](http://json.org/) within [SonarQube](http://www.sonarqube.org):

 * Computes metrics: lines of code, statements, etc.
 * Performs more than [12 checks](http://sonarqube.racodond.com/coding_rules#languages=json)
 * Provide the ability to write your own checks


## Demo

 * [Demo project](http://sonarqube.racodond.com/dashboard/index?id=json-sample-project)

## Usage
1. [Download ad install](http://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade) SonarQube
2. Install the JSON analyzer either by a [direct download](https://github.com/racodond/sonar-json-plugin/releases) or through the [Update Center](http://docs.sonarqube.org/display/SONAR/Update+Center).
3. [Install your favorite analyzer](http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis) (SonarQube Scanner, Maven, Ant, etc.) and analyze your code. Note that starting at version 2.0, Java 8 is required to run an analysis.

Plugin versions and compatibility with SonarQube versions: [http://docs.sonarqube.org/display/PLUG/Plugin+Version+Matrix](http://docs.sonarqube.org/display/PLUG/Plugin+Version+Matrix)

## Available Checks

#### Generic

 * BOM should not be used for UTF-8 files
 * File names should comply with a naming convention
 * Files should contain an empty new line at the end
 * Tabulation characters should not be used

#### Puppet

 * "author" should match the required value in Puppet "metadata.json" files
 * "license" should be valid in Puppet "metadata.json" files
 * "license" should match the required value in Puppet "metadata.json" files
 * "version" should be a semantic version in Puppet "metadata.json" files
 * Deprecated keys should be removed from Puppet "metadata.json" files
 * Duplicated dependencies should be removed from Puppet "metadata.json" files
 * Puppet "metadata.json" files should define all the required keys

#### Templates

 * Regular expression on key


## Custom Checks

You're thinking of new valuable rules? Version 2.0 or greater provides an API to write your own custom checks.
A sample plugin with detailed explanations is available [here](https://github.com/racodond/sonar-json-custom-rules-plugin).
If your custom rules may benefit the community, feel free to create a pull request in order to make the rule available in the JSON analyzer.

You're thinking of new rules that may benefit the community but don't have the time or the skills to write them? Feel free to create an [issue](https://github.com/racodond/sonar-json-plugin/issues) for your rules to be taken under consideration.


## Troubleshooting

If a JSON file is containing some heavily nested objects (more than a hundred nested levels), you may face a `StackOverflowError` looking like:
```
Exception in thread "main" java.lang.StackOverflowError
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.convertChildren(SyntaxTreeCreator.java:128)
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.visitNonTerminal(SyntaxTreeCreator.java:119)
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.visit(SyntaxTreeCreator.java:72)
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.visitNonTerminal(SyntaxTreeCreator.java:89)
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.visit(SyntaxTreeCreator.java:72)
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.convertChildren(SyntaxTreeCreator.java:129)
	at com.sonar.sslr.impl.typed.SyntaxTreeCreator.visitNonTerminal(SyntaxTreeCreator.java:119)
	...
```

Increasing the JVM stack size should fix your issue.

If you are running your analysis with:

 * The SonarQube Scanner, set the `SONAR_SCANNER_OPTS` environment variable to `-Xss10m` for instance
 * Maven, set the `MAVEN_OPTS` environment variable to `-Xss10m` for instance

and rerun your analysis.
