@care
Feature: Car Search Functionality
  As a user, I want to search for cars by brand and year
  So that I can find suitable vehicles matching my preferences

  Background:
    Given I open the car search page

  @SYT-CS-BRAND-YEAR @search @care
  Scenario Outline: Search cars by brand and year range
    When I select Toyota as the brand
    And I set the year range from "<startYear>" to "<endYear>"
    Then the search results should be filtered correctly

    Examples:
      | startYear | endYear |
      | 2022      | 2025    |
