[![Build Status](https://api.travis-ci.org/racodond/sonar-json-plugin.svg?branch=master)](https://travis-ci.org/racodond/sonar-json-plugin)
[![AppVeyor Build Status](https://ci.appveyor.com/api/projects/status/imfckm45thk6vvh4/branch/master?svg=true)](https://ci.appveyor.com/project/racodond/sonar-json-plugin/branch/master)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.codehaus.sonar-plugins.json:json)](https://sonarqube.com/dashboard?id=org.codehaus.sonar-plugins.json%3Ajson)

# SonarQube JSON Analyzer

## Description
This [SonarQube](http://www.sonarqube.org) plugin analyzes [JSON](http://json.org/) files and:

 * Computes metrics: lines of code, statements, etc.
 * Checks various guidelines to find out potential bugs and code smells through more than [12 checks](http://sonarqube.racodond.com/coding_rules#languages=json)
 * Provides the ability to write your own checks

A live example is available [here](http://sonarqube.racodond.com/dashboard/index?id=json-sample-project).


## Usage
1. [Download ad install](http://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade) SonarQube
1. Install the JSON plugin either by a [direct download](https://github.com/racodond/sonar-json-plugin/releases) or through the [Update Center](http://docs.sonarqube.org/display/SONAR/Update+Center). See [plugin compatibility with SonarQube versions](http://docs.sonarqube.org/display/PLUG/Plugin+Version+Matrix).
1. [Install your favorite scanner](http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis) (SonarQube Scanner, Maven, Ant, etc.)
1. [Analyze your code](http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis)


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


## Contributing
Any contribution is more than welcome!
 
You feel like:
* Adding a new check? Just [open an issue](https://github.com/racodond/sonar-json-plugin/issues/new) to discuss the value of your check. Once validated, code, don't forget to add a lot of unit tests and open a PR.
* Fixing some bugs or improving existing checks? Just open a PR.
