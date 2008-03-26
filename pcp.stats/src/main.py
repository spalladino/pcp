from loader import *

def summary():
    runs = Loader().load()
    for run in runs:
        print "Run fixture %s on %s" % (run['run.id'], run['run.filename'])
        print " Graph N=%03d E=%03d P=%03d" % (run['graph.nodes'], run['graph.edges'], run['graph.partitions'])
        print " Time: %d millis (chi=%d) with %s" % (run['solution.time'], run['solution.chi'], run['solution.solver'])
        print " Preprocessing: %d millis" % run['preprocess.time']
        
        for name, cut in run['cuts'].iteritems():
            print " %s: %d cuts in %d millis" % (name, cut['count'], cut['ticks'])
        
        print

if __name__ == '__main__':
    summary()
