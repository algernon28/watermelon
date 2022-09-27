@cart  @checkout
Feature: Checkout

  Background: 
    Given I am logged as user
      | username      | password     |
      | standard_user | secret_sauce |

  Scenario Outline: add and remove products from cart
    And I add '<products>' as products
    And I enter the cart page
    And the cart badge shows <num>
    Then the items in the cart are <num>
    But I remove '<remove>'
    Then the items in the cart are <items left>

    Examples: 
      | products                                  | num | remove                | items left |
      | Sauce Labs Backpack,Sauce Labs Bike Light |   2 | Sauce Labs Bike Light |          1 |

  Scenario Outline: checkout products
    And I add '<products>' as products
    And I check out the products
    And I fill in my info
      | first name | last name | zip code |
      | Donald     | Duck      |      113 |
    And the overview page is displayed
    And the item prices '<prices>' are correct
    And the '<payment>' as payment and '<shipping>' as shipping details are correct
    And the '<subtotal>' as subtotal, '<tax>' as tax, '<total>' as total details are correct
    Then I confirm the transaction and a confirmation page is displayed

    Examples: 
      | products                                  | prices     | payment          | shipping                    | subtotal | tax  | total |
      | Sauce Labs Backpack,Sauce Labs Bike Light | 29.99,9.99 | SauceCard #31337 | FREE PONY EXPRESS DELIVERY! |    39.98 | 3.20 | 43.18 |
