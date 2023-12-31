import numpy as np


class ExpressionTree:	# Base class for encoding solution

	def __init__(self):
		self.objectives = []
		self.parent = None
		self.arity = 0	# arity is the number inputs/ leaves of the internal node
		self.rank = 0
		self.crowding_distance = 0
		self._children = []
		self.ls_a = 0.0
		self.ls_b = 1.0
		self.is_not_arithmetic = False

	def ParetoDominates(self, other):
		dominates = False 
		for i in range(len(self.objectives)):
			if self.objectives[i] > other.objectives[i]:
				return False
			if self.objectives[i] < other.objectives[i]:
				dominates = True 

		return dominates

	def GetSubtree( self ):
		result = []
		self._GetSubtreeRecursive(result)
		return result

	def GetReadableExpression( self ):
		result = [ '' ]	
		self._GetHumanExpressionRecursive(result)
		return result[0]

	def AppendChild( self, N ):
		self._children.append(N)
		N.parent = self

	def DetachChild( self, N ):
		assert(N in self._children)
		for i, c in enumerate(self._children):
			if c == N:
				self._children.pop(i)
				N.parent = None
				break
		return i

	def InsertChildAtPosition( self, i, N ):
		self._children.insert( i, N )
		N.parent = self

	def GetOutput( self, X ):
		return None

	def GetDepth(self):
		n = self
		d = 0
		while (n.parent):
			d = d+1
			n = n.parent
		return d

	def GetHeight(self):
		subtree = self.GetSubtree()
		curr_depth = self.GetDepth()
		leaves = [x for x in subtree if x.arity == 0]
		max_h = 0
		for l in leaves:
			d = l.GetDepth()
			if d > max_h:
				max_h = d
		return max_h - curr_depth


	def _GetSubtreeRecursive( self, result ):
		result.append(self)
		for c in self._children:
			c._GetSubtreeRecursive( result )
		return result


	def _GetHumanExpressionRecursive(self, result):
		args = []
		for i in range(self.arity):
			self._children[i]._GetHumanExpressionRecursive( result )
			args.append( result[0] )
		result[0] = self._GetHumanExpressionSpecificNode( args )
		return result


	def _GetHumanExpressionSpecificNode( self, args ):
		raise NotImplementedError('_GetHumanExpressionSpecificNode is not implemented for base class ExpressionTree')


	def Count_n_nacomp(self, count=None):
		if count == None:
			count = 0
		if (self.is_not_arithmetic):
			count += 1
			count_args = []
			for c in self._children:
				count_args.append(c.Count_n_nacomp(count))
				count = max(count_args)
			return count
		else:
			if self.arity > 0:
				count_args = []
				for c in self._children:
					count_args.append(c.Count_n_nacomp(count))
				count = max(count_args)
			return count
