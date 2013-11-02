Feature: Yahoo feed

  Scenario: After asking a specific type of instrument prices from the yahoo-feed it downloads the price from yahoo and sends the result to the replyTo queue
    Given yahoo answers with
      | A,AAA,7/25/2013,1.1,2.2,3.3 |

    When an instrument price load command message is sent to the yahoo IN queue with "queue://reply" replyTo and content
      | { "shortNames": ["A"] } |
    Then the "queue://reply" queue should contain 1 messages
    And the message in the "queue://reply" queue should have "JMSType" header with the value "InstrumentPriceCommandMessage"

  Scenario: After asking 3 types of instrument prices from the yahoo-feed it downloads the prices from yahoo and sends all the 3 prices to the replyTo queue
    Given yahoo answers with
      | A,AAA,7/25/2013,1.1,2.2,3.3             |
      | B,BBB,7/25/2013,11.11,222.22,3.33       |
      | C,CCC,7/25/2013,111.111,222.222,333.333 |

    When an instrument price load command message is sent to the yahoo IN queue with "queue://reply" replyTo and content
      | { "shortNames": ["A", "B", "C"] } |
    Then the "queue://reply" queue should contain 3 messages

  Scenario: After asking 3 types of the prices from the yahoo-feed it downloads the prices from yahoo and sends all the 3 prices to the replyTo queue with the defined content
    Given yahoo answers with
      | A,AAA,7/25/2013,1.1,2.2,3.3             |
      | B,BBB,7/25/2013,11.11,222.22,3.33       |
      | C,CCC,7/25/2013,111.111,222.222,333.333 |

    When an instrument price load command message is sent to the yahoo IN queue with "queue://reply" replyTo and content
      | { "shortNames": ["A", "B", "C"] } |
    Then the message bodies in the "queue://reply" queue should exactly match with
      | {"provider":"YAHOO","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11}    |
      | {"provider":"YAHOO","shortName":"C","longName":"CCC","date":1374706800000,"price":111.111}  |
      | {"provider":"YAHOO","shortName":"A","longName":"AAA","date":1374706800000,"price":1.1}      |

  Scenario: If yahoo sends a price with more spaces the conversion should trim it
    Given yahoo answers with
      | F ,   AAA   , 7/25/2013  ,    1.1,  2.2 ,3.3 |

    When an instrument price load command message is sent to the yahoo IN queue with "queue://reply" replyTo and content
      | { "shortNames": ["F"] } |
    Then the message bodies in the "queue://reply" queue should exactly match with
      | {"provider":"YAHOO","shortName":"F","longName":"AAA","date":1374706800000,"price":1.1} |

  Scenario: If yahoo sends well-formed and not well-formed prices together then the correct prices should go to the replyTo queue while all the other price should go to the dead letter queue
    Given yahoo answers with
      | B,BBB,7/25/2013,11.11,222.22,3.33 |
      | C,AAA,NOT_VALID,1.1,2.2,3.3       |
      | A,AAA,7/25/2013,1.1,2.2           |
      | D,AAA,7/25/2013                   |

    When an instrument price load command message is sent to the yahoo IN queue with "queue://reply" replyTo and content
      | { "shortNames": ["A", "B", "C", "D"] } |
    Then the message bodies in the "queue://reply" queue should exactly match with
      | {"provider":"YAHOO","shortName":"B","longName":"BBB","date":1374706800000,"price":11.11} |
      | {"provider":"YAHOO","shortName":"A","longName":"AAA","date":1374706800000,"price":1.1}   |
    And the "queue://dead" queue should contain 2 messages