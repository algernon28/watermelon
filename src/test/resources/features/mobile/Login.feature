@login
Feature: Login
  Test the login with different combinations

  Background: 
    Given I am on the mobile login page

  Scenario Outline: Login with correct credentials
    When I enter username as '<username>' and password as '<password>'
    Then I land on the Products page

    Examples: 
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |
