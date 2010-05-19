import processor
import metrics

if __name__ == '__main__':
    p = processor.Processor()
    #p.summary()
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])
    
    p.latextable(ids=['run.filename'],
                 datas=["solution.time", "solution.chi"], 
                  series=["path.enabled", 
                    "clique.enabled",
                    "blockColor.enabled",
                    "gprime.holes.enabled",
                    "holes.enabled",
                    "branch.prios.enabled",
                    "primal.enabled"],
                  datafilter= {'run.folder': '.\\..\\data\\rand025\\'}
                  )