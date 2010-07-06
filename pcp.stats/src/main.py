import processor
import metrics

from latexprocessor import LatexProcessor

def simpletable(p):    
    p.simpletable(["graph.nodes", "preprocess.time","solution.time", "solution.chi"], ["path.enabled", 
                    "clique.enabled",
                    "blockColor.enabled",
                    "gprime.holes.enabled",
                    "holes.enabled",
                    "branch.prios.enabled",
                    "primal.enabled"], None, {'run.folder': '.\\..\\data\\rand075\\'})
    
def model_latextable(p):    
        p.process(
                ids=[metrics.FileName()],
                datas=['solution.gap','cuts.niters','solution.time'], 
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
                datafilter= {'strategy.adjacency': 'AdjacentsNeighbourhood', 'strategy.colorBound': 'UpperNodesSum', 'strategy.partition': 'PaintExactlyOne', 'strategy.objective': 'Equal', 'model.adjacentsNeighbourhood.useCliqueCover':'true'},
                runfilter= None,
                aggr= processor.avg
                    )

def dsatur_latextable(p):    
        p.process(
                ids=[metrics.FileName()],
                datas=['solution.chi','solution.found'], 
                series=['strategy.coloring', 'dsatur.partition.weight.size', 'dsatur.partition.weight.colorCount', 'dsatur.partition.weight.uncolored'],
                datafilter= {'dsatur.partition.weight.size':'1', 'dsatur.partition.weight.colorCount':'10000', 'dsatur.partition.weight.uncolored':'100'},
                runfilter= None,
                aggr= processor.avg
                )


if __name__ == '__main__':
    p = LatexProcessor('20100701DSATURS1M')
    dsatur_latextable(p)
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])

