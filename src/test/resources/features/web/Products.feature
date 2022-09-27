@products
Feature: Products

  Background: 
    Given I am logged as user
      | username      | password     |
      | standard_user | secret_sauce |
    And I land on the Products page
    And the number of products presented is 6

  Scenario: Check product page
    Then the cart badge shows nothing

  Scenario: Sort the items
    Then I sort the products
      | sort value | sort text           |
      | az         | Name (A to Z)       |
      | za         | Name (Z to A)       |
      | lohi       | Price (low to high) |
      | hilo       | Price (high to low) |

  @cart
  Scenario: Add and remove products from the cart
    And I add products
      | Sauce Labs Backpack,Sauce Labs Bike Light |
    Then the cart badge shows 2
    But I remove products
      | Sauce Labs Backpack |
    Then the cart badge shows 1

  @cart
  Scenario: Cart is empty
    And the cart badge shows nothing
    And I enter the cart page
    Then the cart is empty

  @cart
  Scenario: To cart page and back
    And the cart badge shows nothing
    And I enter the cart page
    And I continue shopping
    Then I land on the Products page
    And the cart badge shows nothing
    And the number of products presented is 6
