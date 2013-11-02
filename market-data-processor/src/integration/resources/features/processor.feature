Feature: Price processor

  Scenario: If an instrument price command message arrives the price should be persisted to the the database
    Given instrument price command messages arrive with following content
      | {"provider":"A","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11} |

    When the client asks for the following instrument price
      | {"provider":"A","shortName":"B"} |
    Then the response message bodies should exactly match with
      | {"headers":{"errorCode":200},"price":{"provider":"A","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}} |
    And the response message should have "JMSType" header with the value "InstrumentPriceResponseMessage"

  Scenario: If an instrument price request message arrives and the price doesn't exists we should trigger yahoo to download price
    When the client asks for the following instrument price
      | {"provider":"A","shortName":"C"} |
    Then the message bodies in the "queue://A.in" queue should exactly match with
      | {"shortNames":["C"]} |
    And the message in the "queue://A.in" queue should have "JMSType" header with the value "InstrumentPriceLoadCommandMessage"

  Scenario: If an instrument price update arrives it should be persisted to the the database
    Given instrument price command messages arrive with following content
      | {"provider":"A","shortName":"D","longName":"DDD","date":1374706800000,"price":11.11} |

    When instrument price command messages arrive with following content
      | {"provider":"A","shortName":"E","longName":"EEE","date":1374706811111,"price":22.22} |
    And the client asks for the following instrument price
      | {"provider":"A","shortName":"E"} |
    Then the response message bodies should exactly match with
      | {"headers":{"errorCode":200},"price":{"provider":"A","shortName":"E","longName":"EEE","date":1374706811111,"price":22.22}} |
    And the response message should have "JMSType" header with the value "InstrumentPriceResponseMessage"

  Scenario: If we have many provider specific instrument prices we should be able to retrieve all at once
    Given instrument price command messages arrive with following content
      | {"provider":"C","shortName":"A","longName":"AAA","date":1374706800000,"price":11.11} |
      | {"provider":"C","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11} |

    When instrument price list request message arrives with the following content
      | {"provider":"C"} |
    Then the response message bodies should exactly match with
      | {"headers":{"errorCode":200},"prices":[{"provider":"C","shortName":"A","longName":"AAA","date":1374706800000,"price":11.11},{"provider":"C","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}]} |
    And the response message should have "JMSType" header with the value "InstrumentPriceListResponseMessage"