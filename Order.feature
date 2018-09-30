Feature: Order

  @EP-21838 @Critical @eComm @Fulfillment @Order @usDomain
  Scenario: purchase .us domain order
    Given xxxyyy
    When create order
    Then order state is "DRAFT"
    When rate order to get price
    Then order total is greater than 0
    When purchase order
    Then order state is "PAID"


  @EP-21841 @Critical @eComm @Fulfillment @Order @usDomain
  Scenario: purchase .us domain upgrade order
    Given xxxyyy
    When create order
    Then order state is "DRAFT"
    When rate order to get price
    Then order total is greater than 0
    When purchase order
    Then order state is "PAID"
    When wait for Sub and Ent background services to create Sub and Ent resources and update uris
    And create upgrade order
    And rate upgrade order to get price
    Then order total is greater than 0
    When purchase upgrade order
    Then order state is "PAID"
