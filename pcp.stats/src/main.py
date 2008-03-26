import processor

if __name__ == '__main__':
    p = processor.Processor()
    p.summary()
    p.graphprops("graph.nodes", "solution.time")
