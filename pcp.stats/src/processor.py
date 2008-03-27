import loader
import graphics
import config
import os

class Processor(object):
    
    def __init__(self, runid=None):
        self.loader = loader.Loader(runid) 
        self.runs = self.loader.load()
        self.runid = self.loader.runid
        
    def summary(self):
        for run in self.runs:
            print "Run fixture %s on %s" % (run['run.id'], run['run.filename'])
            print " Graph N=%03d E=%03d P=%03d" % (run['graph.nodes'], run['graph.edges'], run['graph.partitions'])
            print " Time: %d millis (chi=%d) with %s" % (run['solution.time'], run['solution.chi'], run['solution.solver'])
            print " Preprocessing: %d millis" % run['preprocess.time']
            
            for name, cut in run['cuts'].iteritems():
                print " %s: %d cuts in %d millis" % (name, cut['count'], cut['ticks'])
            
            print

    def graphprops(self, datax, datay, fname=None):
        x, y = zip(*[(run[datax], run[datay]) for run in self.runs])
        graphics.simplegraphic(x, y, datax, datay)
        self.handlegraph(fname)
        
    def graphfuncs(self, datax, datay, fname=None):
        x, y = zip(*[(datax(run), datay(run)) for run in self.runs])
        graphics.simplegraphic(x, y)
        self.handlegraph(fname)
        
    def handlegraph(self, fname=None):
        if fname: graphics.save(os.path.join(config.fullrunsdir, self.runid, fname))
        else: graphics.show()