import networkx
import os
import random

from networkx import *

class Generator(object):
    
    def __init__(self, folder, name=None, base=1):
        self.folder = folder
        self.name = name
        self.base = base

    def with_name(self, name):
        return Generator(self.folder, name, self.base)
    
    # http://en.wikipedia.org/wiki/Barabasi-Albert_model
    def barabasi_albert(self, nodes, density=0.2, minpsize=2, maxpsize=2):
        g = barabasi_albert_graph(nodes, int(nodes * density))
        p = self.partitionate(g, minpsize, maxpsize)
        self.write(g,p,nodecnt=nodes,density=density,minpsize=minpsize,maxpsize=maxpsize,algorithm='barabasi_albert')

    def holme_kim(self, nodes, density=0.2, minpsize=2, maxpsize=2):
        g = powerlaw_cluster_graph(nodes, int(nodes * density), density)
        p = self.partitionate(g, minpsize, maxpsize)
        self.write(g,p,nodecnt=nodes,density=density,minpsize=minpsize,maxpsize=maxpsize,algorithm='holme_kim')
    
    def partitionate(self, graph, minpsize, maxpsize):
        nodes = graph.nodes()
        partitions = []
        while len(nodes) > 0:
            size = random.randint(minpsize,maxpsize)
            partition, nodes = nodes[:size], nodes[size:] 
            partitions.append(partition)
        return partitions
    
    def write(self, graph, partitions, **params):
        with open(os.path.join(self.folder, self.name + '.in'), 'w') as f:
            
            for k,v in params.items():
                f.write("c %s %s\n" % (str(k), str(v)))
            f.write("p %s %d %d %d \n" % (self.name, len(graph.nodes()), len(graph.edges()), len(partitions)))
            
            for p in partitions:
                f.write("q %s \n" % ' '.join([self.node(x) for x in p]))
            
            for (u,v) in graph.edges():
                f.write("e %s %s \n" % (self.node(u),self.node(v)))
                
    
    def node(self, node):
        return str(node+self.base)
    