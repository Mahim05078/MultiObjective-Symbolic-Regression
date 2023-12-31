# Libraries
import numpy as np 
import pandas as pd
import sklearn.datasets
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import scale
from copy import deepcopy

from SRSolution import AddNode,SubNode, MulNode, DivNode, LogNode, SinNode, CosNode
from API import pyNSGPEstimator as nsgaII


np.random.seed(42)

# Load regression dataset 
data_url = "http://lib.stat.cmu.edu/datasets/boston"
raw_df = pd.read_csv(data_url, sep="\s+", skiprows=22, header=None)
X = np.hstack([raw_df.values[::2, :], raw_df.values[1::2, :2]])
y = raw_df.values[1::2, 2]
# X, y = sklearn.datasets.load_boston( return_X_y=True )

# Take a dataset split
X_train, X_test, y_train, y_test = train_test_split( X, y, test_size=0.5, random_state=42 )

# Prepare NSGA-II settings
nsgp = nsgaII(pop_size=512, max_generations=50, verbose=True, max_tree_size=50, 
	crossover_rate=0.8, mutation_rate=0.1, op_mutation_rate=0.1, min_depth=2, initialization_max_tree_height=6, 
	tournament_size=2, use_linear_scaling=True, use_erc=True, use_interpretability_model=True,
	penalize_duplicates=True,
	functions = [ AddNode(), SubNode(), MulNode(), DivNode(), LogNode(), SinNode(), CosNode() ])

# Fit like any sklearn estimator
nsgp.fit(X_train,y_train)

# Obtain the front of non-dominated solutions (according to the training set)
front = nsgp.get_front()
print('len front:',len(front))
for solution in front:
	print(solution.GetReadableExpression())
	print("MSE="+ str(solution.objectives[0]) + ";l="+ str(solution.objectives[1]))

# You can also use sympy to simplify the formulas :) (if you use PowNode, replace ^ to ** before use)
'''
from sympy import simplify
for solution in front:
	simplified = simplify(solution.GetHumanExpression())
	print(simplified)
'''
