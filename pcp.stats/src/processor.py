import loader
import graphics

class Processor(object):
    
    def __init__(self, runid=None):
        self.runid = runid
        self.runs = loader.Loader(runid).load()
        
    def summary(self):
        for run in self.runs:
            print "Run fixture %s on %s" % (run['run.id'], run['run.filename'])
            print " Graph N=%03d E=%03d P=%03d" % (run['graph.nodes'], run['graph.edges'], run['graph.partitions'])
            print " Time: %d millis (chi=%d) with %s" % (run['solution.time'], run['solution.chi'], run['solution.solver'])
            print " Preprocessing: %d millis" % run['preprocess.time']
            
            for name, cut in run['cuts'].iteritems():
                print " %s: %d cuts in %d millis" % (name, cut['count'], cut['ticks'])
            
            print

    def graphprops(self, datax, datay):
        x, y = zip(*[(run[datax], run[datay]) for run in self.runs])
        graphics.simplegraphic(x, y, datax, datay)
        
    def graphfuncs(self, datax, datay):
        x, y = zip(*[(datax(run), datay(run)) for run in self.runs])
        graphics.simplegraphic(x, y)