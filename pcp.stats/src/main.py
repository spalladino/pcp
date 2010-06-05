import processor
import metrics

def simpletable(p):    
    p.simpletable(["graph.nodes", "preprocess.time","solution.time", "solution.chi"], ["path.enabled", 
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
                series=["solver.kind", "branch.selection"],
                datafilter= None,
                runfilter= None#(lambda x: x['run.filename'].startswith("benchnodes")) 
                    )


if __name__ == '__main__':
    p = processor.Processor('20100604200934')
    #p.summary()
    latextable(p)
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])

