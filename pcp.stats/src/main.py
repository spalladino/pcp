import processor
import metrics

def simpletable(p):    
    p.simpletable(["solution.nnodes", "solution.time", "solution.gap"], ["path.enabled", 
                    "clique.enabled",
                    "blockColor.enabled",
                    "gprime.holes.enabled",
                    "holes.enabled",
                    "branch.prios.enabled",
                    "primal.enabled"], None, {'run.folder': '.\\..\\data\\rand075\\'})
    
def latextable(p):    
        p.latextable(
                ids=['run.filename'],
                datas=["solution.time", "solution.nnodes", "solution.gap"], 
                series=["branch.prios.enabled", 'branch.dynamic.fractional', 'branch.dynamic.dsatur'],
                datafilter= None,
                 
                    )

if __name__ == '__main__':
    p = processor.Processor()
    latextable(p)
    #p.summary()
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])

