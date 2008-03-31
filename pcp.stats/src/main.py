import processor
import metrics

if __name__ == '__main__':
    p = processor.Processor()
    p.summary()
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"], fname= "nodescuts.png")
