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
                datas=['solution.gap','solution.time'], 
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective', 'model.adjacentsNeighbourhood.useCliqueCover'],
                datafilter= {'strategy.symmetry': 'UseLowerLabelFirst', 'strategy.objective': 'Equal'},
                runfilter= None,
                aggr= processor.avg
                    )

def dsatur_latextable(p):    
        p.process(
                ids=[metrics.FileName()],
                datas=['solution.chi','solution.time','solution.found'], 
                series=['strategy.coloring', 'dsatur.partition.weight.size', 'dsatur.partition.weight.colorCount', 'dsatur.partition.weight.uncolored'],
                datafilter= None,
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

