import processor
import metrics

from latexprocessor import LatexProcessor
from latexsqlprocessor import LatexSqlProcessor

def simpletable(p):    
    p.simpletable(["graph.nodes", "preprocess.time","solution.time", "solution.chi"], ["path.enabled", 
                    "clique.enabled",
                    "blockColor.enabled",
                    "gprime.holes.enabled",
                    "holes.enabled",
                    "branch.prios.enabled",
                    "primal.enabled"], None, {'run.folder': '.\\..\\data\\rand075\\'})
    
def latextable(p):    
        p.process(
                ids=[metrics.FileName()],
                datas=['solution.lb'], 
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective', 'model.adjacentsNeighbourhood.useCliqueCover'],
                datafilter= None,
                runfilter= None,
                aggr= processor.concat
                    )


if __name__ == '__main__':
    p = LatexProcessor('20100623215001')
    #p.summary()
    latextable(p)
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])

