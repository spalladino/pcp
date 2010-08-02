import matplotlib.pyplot as plt
import networkx as nx

def draw(g):
	plt.clf()
	nx.draw_circular(g)
	plt.show()
	