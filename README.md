
<img src="src/main/resources/img/watermelon.png" width="200"/>

# Watermelon

### _A lightweight Selenium/Cucumber Framework_
This is a very lightweight test automation framework, thought to make writing Cucumber tests as painless as possible.
It comes with a full sample test-suite built on <a href="https://www.saucedemo.com">SauceLabs Test Website</a>. Seeing it in action with a somewhat functional website is better than a thousand words.

## How to script it
- Configuration: if config-<dev|preprod|prod>.yaml structure works for you, then you don't need to change anything beside the values. If you need different structure in the yaml, then you need to implement your own Configuration binding and register it with Guice in ConfigurationModule.
- Pages: All you need to do is to extend WebPage, and implement a constructor with Webdriver and WebdriverWait as parameters, both injected (see below)**.

```Java
	import com.watermelon.core.WebPage;

	public class MyPage extends WebPage {

		@Inject
		public MyPage(WebDriver driver, WebDriverWait wait) {
			super(driver, wait);
		}
	}
```
- Dependencies: don't use "new" anymore! Declare your dependencies with @Inject, either as field or in your constructor: If the class has empty constructor it will be instantiated and passed to your class by Guice. In the example below, Guice will create "page" as instance of MyPage and _inject_ it***. 

```Java
@Inject
private SomePage somePage; // This page will be instantiated as soon as it is used
```
- Steps (glue): extend BaseSteps. Beside that and what we said about injection, just write them as usual.

```Java
import com.watermelon.steps.BaseSteps;
public class MyPageSteps extends BaseSteps { 
	@Inject 
	private MyPage page;
    
	public MyPageSteps() {
        page.doSomething(); // MyPage will be instantiated here
    }
}
```
- Standard TestNG unit tests (no Gherkin) are also easily implemented (see WebTable test) 

** This is necessary to make PageFactory.initElements() work in the parent class.

*** Note that MyPage doesn't strictly have a constructor "without parameters", however since both WebDriver and WebDriverWait are injected it will still work.

## Nuts & Bolts

### Libraries

Watermelon is a  testing framework based on Cucumber and Java, with TestNG as underlying test engine and Selenium 4 for the browser automation.
The framework makes heavy use of Dependency Injection, using Guice.
 
- Cucumber
- TestNG 
- Assertj
- Selenium 4
- Guice
- Webdrivermanager

### Build 

- Bare compilation: mvn clean install -DskipTests
- Run Tests with Cluecumber reporting: mvn clean test cluecumber-report:reporting *parameters
> _Mandatory parameters_
>-Dstage=config-dev.yaml|config-preprod.yaml|config-prod.yaml  *
>-DtestSuite=test-suites/testng.yaml  **

* configuration files by environment
** TestNG suite file to be run. Three suite files (and relative runners) are included in this project, for cross-browser testing purposes: one for Chrome, one for Firefox and one for Edge. One more meta-suite file (testng.yaml) is also included to runn all three together (see suiteThreads parameter for running them in parallel).
> _Optional parameters_
> -DgithubToken=[github token] ***
> -DsuiteThreads=[# threads] ****

*** github token, mandatory for FireFox browser with WebDriverManager
**** number of threads, used for multibrowsing (multiple runners in parallel). Not adviced when scenarios are already run in parallel


### Components
#### Configuration
There are two main configuration objects:
- _Configuration:_ bean mapped on config-[env].yaml. It contains mainly information about the website's URL being tested and screenshot level (when to take the screenshots: 
	- ALWAYS: take screenshots regardless of the outcome
	- ONLY_FAILED: only take screenshot when the step/test is failed
- _DriverManager:_ reads the browser requirements from the suite config files and instantiates the relative webdriver (this is thread-safe). This class implements the Provider interface and it provides such instances for use with dependency injection.
>DriverManager works internally with WebDriverManager. downloading the webdriver implementation straight from the internet.

### _Dependency Injection_
##### Modules
_ConfigurationModule:_ provides a Configuration, a java.util.Locale and a ResourceBundle containing properties taken from **message.properties**
> Note that this is designed to work with i18n (internationalization). Providing a different properties for each language (e.g. message_en_GB.properties, messages_it_IT.properties...), changing Locale the correct properties will be automatically picked. Without locale suffix the file will be intended as default.

_DriverManagerModule:_ provides a webdriver, using the DriverManager provider, a JavascriptExecutor and a WebDriverWait.

_ModuleInjector:_ injects the aforementioned modules.
>Important: this injector **must** be in the classpath pointed by the runners, otherwise it will not be loaded.

####_Web elements_
These are some convenience wrappers for Selenium Webelement.
- _Checkbox:_ check/uncheck a checkbox element, depending on its state.
- _RadioButtonGroup:_ convenience methods to read and select the value of a radiobutton group
- _WebTable:_ utilities to select elements from an html table by rows and columns.

### Parallel Execution
There are two ways run tests in parallel:

- _Suite level:_ useful for crossbrowsing, in this project the three runners (Edge, Chrome and Firefox) can be run in parallel by setting 
the parameter suiteThreads (see Usage paragraph above) to 3.

- _Scenario level:_ this can be achieved setting **parallel = true** in the relative runner.

**Note that setting both parameters to multithreading can lead to unexpected results (multiple threads for multiple runners)**, therefore it is best to set _suiteThreads = 1_ if we want to run scenarios in parallel, and _parallel= false_ if we want to have runners in multithreading. 
