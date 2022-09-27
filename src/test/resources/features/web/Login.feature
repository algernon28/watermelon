@login
Feature: Login
  Test the login with different combinations

  Background: 
    Given I am on the login page

  Scenario Outline: Login with correct credentials
    When I enter username as '<username>' and password as '<password>'
    Then I land on the Products page

    Examples: 
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |

  @negative
  Scenario Outline: Login with wrong credentials
    When I enter username as '<username>' and password as '<password>'
    Then I expect validation message as '<message>' is displayed

    Examples: 
      | username                | password      | message                |
      | locked_out_user         | secret_sauce  | login.lockedout        |
      | locked_out_user         | wrongpassword | login.wrong            |
      | standard_user           | wrongpassword | login.wrong            |
      | performance_glitch_user | wrongpassword | login.wrong            |
      | problem_user            | wrongpassword | login.wrong            |
      |                         |               | login.missing.username |
      |                         | anypassword   | login.missing.username |
      | standard_user           |               | login.missing.password |
      | anyusername             |               | login.missing.password |
      