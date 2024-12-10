This Java program models a scenario where a robber calculates the maximum amount of money they can rob from various types of houses without robbing two adjacent houses. The program consists of:

    Abstract Class Robber:
        Defines abstract methods for calculating the maximum money that can be robbed from different house layouts (RowHouses, RoundHouses, SquareHouse, MultiHouseBuilding).
        Includes a concrete method, MachineLearning, for demonstration purposes.

    Concrete Class JAVAProfessionalRobber:
        Implements all abstract methods from Robber.
        Logic for RowHouses uses dynamic programming to maximize the robbed amount without adjacent constraints.
        Logic for RoundHouses adapts by splitting the circular array into two linear problems and taking the maximum result.
        Reuses the logic for RowHouses for SquareHouse and MultiHouseBuilding.

    Main Class Rober:
        Provides an interactive console-based menu where users select the type of house.
        Takes the number of houses and their respective money values as input.
        Computes and displays the maximum money that can be robbed for the selected house type.
        Offers the option to make additional selections or exit the program.

        ![image](https://github.com/user-attachments/assets/4e663e53-222c-49f4-af8e-b6b34b283f2b)
