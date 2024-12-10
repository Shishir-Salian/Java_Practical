Program 1

This Java code defines and implements a simple Bank system using an interface and a class. Below is a description of the code:

    Interface Definition (Bankinterface):
        The interface defines four methods:
            setbalance(float balance): A setter method for setting the bank balance.
            setinterestrate(float interestrate): A setter method for setting the interest rate.
            getbalance(): A getter method for retrieving the bank balance.
            getinterestrate(): A getter method for retrieving the interest rate.
        These methods enforce a contract that any implementing class must provide definitions for them.

    Class Definition (Bank):
        The Bank class implements the Bankinterface.
        It defines two instance variables:
            interestrate: A float to store the interest rate.
            balance: A float to store the balance.
        Implements the four methods from the interface:
            getbalance() and getinterestrate() return the values of balance and interestrate.
            setbalance(float balance) and setinterestrate(float interestrate) set the values of the respective instance variables.

    Main Method:
        Creates three Bank objects (bankA, bankB, and bankC), each representing a bank account.
        For each bank:
            Sets the balance and interest rate using the setter methods.
            Prints the balance and interest rate using the getter methods.
        Displays the data for each bank on the console.

    Output Example:
        For bankA, the balance is set to 10000 and the interest rate to 7.0.
        Similarly, bankB and bankC have their respective balances and interest rates.
        The code outputs the balance and interest rate of each bank to the console.

![image](https://github.com/user-attachments/assets/252661f1-c231-4440-8e77-d38125d3278f)

Program 2

This Java program calculates the amount of water that can be trapped between blocks of varying heights after rainfall, demonstrating the concepts of abstraction and inheritance using interfaces and abstract classes.
Code Description

    Interface (Waterconservationsystem):
        Declares the method calculatetrappedwater(int[] blockheights), which calculates the trapped water based on block heights.

    Abstract Class (Rainyseasonconservation):
        Implements the Waterconservationsystem interface.
        Declares the method calculatetrappedwater(int[] blockheights) as abstract, leaving its implementation to subclasses.

    Concrete Class (Cityblockconservation):
        Extends the Rainyseasonconservation abstract class and provides the implementation for the calculatetrappedwater method.
        Implementation Details:
            Calculates the water trapped using the Two-Pointer Technique:
                Leftmax array: Stores the maximum height of blocks to the left of each block.
                Rightmax array: Stores the maximum height of blocks to the right of each block.
                For each block, the trapped water is calculated as:
                Trapped Water at Block=min⁡(Left Max,Right Max)−Block Height
                Trapped Water at Block=min(Left Max,Right Max)−Block Height
                The total trapped water is the sum of water trapped at each block.

    Main Method:
        Accepts user input for:
            The number of blocks.
            Heights of the blocks.
        Uses an instance of Cityblockconservation to calculate the trapped water.
        Prints the result to the console.

![image](https://github.com/user-attachments/assets/bf39ac53-23d7-431c-85bc-0a8d3bf78446)

