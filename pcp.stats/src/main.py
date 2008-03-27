import processor

if __name__ == '__main__':
    p = processor.Processor()
    p.summary()
    p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    p.graphprops("graph.edges", "solution.time", "edges-time.png")
    p.graphprops("graph.partitions", "solution.time", "parts-time.png")
