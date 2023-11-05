# MultiObjective-Symbolic-Regression

Please Navigate to the folder ***/SRPython/symbolicPython***
All the source code is there.

# Code Walkthrough

## ExpressionTree.py
Contains the base class for encoding the solution and necessary util functions.

## SRSolution.py
Models the internal nodes or mathematical operators and constant leaves of the tree. 

## Operators.py
Contains the cross-over and Mutation functions for Exp trees

## Evaluate.py
The calculation of obejctive functions are defined in this file

## NSGA_II.py
This is a raw python based implementation of the Fast NSGA 

## Selection.py
Tournament selection strategy implementation

## API.py
Provides a high level API that resembles SKLearn APIs

## Run.py
Evolutionary algorithm is started by invoking the high level API which ececutes the *Run* method inside NSGA-II 

