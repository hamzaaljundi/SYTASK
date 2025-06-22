@finance
Feature: Finance Eligibility Application
  As a user, I want to apply for car finance
  So that I can check my eligibility based on various criteria

  Background:
    Given I open the finance eligibility form page

  @SYT-NC-GOV @finance
  Scenario Outline: New Car - Government Sector Eligibility
    When I select car type as "new" and employment sector as "government"
    And I enter monthly income as "<income>"
    And I set financial obligations to "<hasObligations>" with amount "<amount>"
    And I enter phone number as "<phoneNumber>"
    And I click on Start Now button
    And I enter OTP as "<otp>"
    Then the eligibility status title should be "<expectedTitle>" and URL path should be "<expectedUrlPath>"
    Examples:
      | income | hasObligations | amount | phoneNumber | otp    | expectedTitle                 | expectedUrlPath        |
      | 15000  | no             | 0      | 0511111111  | 123456 | You are eligible for financing!| /finance-eligibility   |
      | 5000   | yes            | 3000   | 0511111111  | 123456 | You may not be eligible for financing| /disqualified |

  @SYT-NC-PVT @finance
  Scenario Outline: New Car - Private Sector Eligibility
    When I select car type as "new" and employment sector as "private"
    And I set salary bank account to "<hasBankAccount>"
    And I set job tenure to "<hasJobTenure>"
    And I set social insurance (3+ months) to "<hasInsurance>"
    And I enter monthly income as "<income>"
    And I set financial obligations to "<hasObligations>" with amount "<amount>"
    And I enter phone number as "<phoneNumber>"
    And I click on Start Now button
    And I enter OTP as "<otp>"
    Then the eligibility status title should be "<expectedTitle>" and URL path should be "<expectedUrlPath>"
    Examples:
      | hasBankAccount | hasJobTenure | hasInsurance | income | hasObligations | amount | phoneNumber | otp    | expectedTitle                          | expectedUrlPath        |
      | yes            | yes          | yes          | 15000  | no             | 0      | 0511111111  | 123456 | You are eligible for financing!| /finance-eligibility   |
      | no             | no          | yes          | 15000  | no             | 0      | 0511111111  | 123456 | You are eligible for financing! | /finance-eligibility   |

  @SYT-UC-GOV @finance
  Scenario Outline: Used Car - Government Sector Eligibility
    When I select car type as "used" and employment sector as "government"
    And I enter monthly income as "<income>"
    And I set financial obligations to "<hasObligations>" with amount "<amount>"
    And I enter phone number as "<phoneNumber>"
    And I click on Start Now button
    And I enter OTP as "<otp>"
    Then the eligibility status title should be "<expectedTitle>" and URL path should be "<expectedUrlPath>"
    Examples:
      | income | hasObligations | amount | phoneNumber | otp    | expectedTitle                 | expectedUrlPath        |
      | 12000  | no             | 0      | 0511111111  | 123456 | You are eligible for financing! | /finance-eligibility   |
      | 4000   | yes            | 2000   | 0511111111  | 123456 | You may not be eligible for financing | /disqualified |

  @SYT-UC-PVT @finance
  Scenario Outline: Used Car - Private Sector Eligibility
    When I select car type as "used" and employment sector as "private"
    And I set social insurance registration to "<isRegistered>"
    And I enter monthly income as "<income>"
    And I set financial obligations to "<hasObligations>" with amount "<amount>"
    And I enter phone number as "<phoneNumber>"
    And I click on Start Now button
    And I enter OTP as "<otp>"
    Then the eligibility status title should be "<expectedTitle>" and URL path should be "<expectedUrlPath>"
    Examples:
      | isRegistered | income | hasObligations | amount | phoneNumber | otp    | expectedTitle                          | expectedUrlPath        |
      | yes          | 10000  | no             | 0      | 0511111111  | 123456 | You are eligible for financing!        | /finance-eligibility   |
      | no           | 10000  | no             | 0      | 0511111111  | 123456 | You may not be eligible for financing | /disqualified   |