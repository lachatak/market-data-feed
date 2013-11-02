Feature: Price Rest processor

  Scenario: The client should be able to retrieve a specific instrument price related to a provider
    Given the processor data base is empty
    And if the processor gets a request message it should send back the following response and JMSType
      |request                          |response                                                                                                                     |JMSType              |
      |{"provider":"A","shortName":"B"} |{"headers":{"errorCode":200},"price":{"provider":"A","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}}   |InstrumentPriceResponseMessage |

    When the client asks for the instrument price "B" from the provider "A"
    Then the response message body should be
      | {"headers":{"errorCode":200},"price":{"provider":"A","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}} |
    And the response message should have "CamelHttpResponseCode" header with the value "200"

  Scenario: If the client tries to retrieve a nonexistent instrument price then HTTP 404 should be given
    Given the processor data base is empty
    And if the processor gets a request message it should send back the following response and JMSType
      |request                          |response                        |JMSType                        |
      |{"provider":"A","shortName":"B"} |{"headers":{"errorCode":404}}   |InstrumentPriceResponseMessage |

    When the client asks for the instrument price "B" from the provider "A"
    Then the response message should have "CamelHttpResponseCode" header with the value "404"

  Scenario: If we have many provider specific instrument prices we should be able to retrieve all at once
    Given the processor data base is empty
    And if the processor gets a request message it should send back the following response and JMSType
      |request          |response                                                                                                                                                                                                             |JMSType                            |
      |{"provider":"C"} |{"headers":{"errorCode":200},"prices":[{"provider":"C","shortName":"A","longName":"AAA","date":1374706800000,"price":11.11},{"provider":"C","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}]}   |InstrumentPriceListResponseMessage |

    When the client asks for the all instrument prices from the provider "C"
    Then the response message body should be
      | {"headers":{"errorCode":200},"prices":[{"provider":"C","shortName":"A","longName":"AAA","date":1374706800000,"price":11.11},{"provider":"C","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}]} |
    And the response message should have "CamelHttpResponseCode" header with the value "200"