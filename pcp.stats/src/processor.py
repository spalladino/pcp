import loader
import graphics
import config
import os
import metrics
import itertools

from matplotlib import pyplot

class Processor(object):
    
    def __init__(self, runid=None):
        self.loader = loader.Loader(runid) 
        self.runs = self.loader.load()
        self.runid = self.loader.runid
        
    def summary(self):
        for run in self.runs:
            print "Run fixture %s on %s" % (run['run.id'], run['run.filename'])
            print " Graph N=%03d E=%03d P=%03d" % (run['graph.nodes'], run['graph.edges'], run['graph.partitions'])
            print " Time: %d millis (chi=%d) with %s" % (run['solution.time'], run.get('solution.chi') or 0, run.get('solution.solver') or '?')
            if run.has_key('preprocess.time'): print " Preprocessing: %d millis" % run['preprocess.time']
            
            if 'cuts' in run:
                for name, cut in run['cuts'].iteritems():
                    print " %s: %d cuts in %d millis" % (name, cut['count'], cut['ticks'])
            
            print

    def simpletable(self, datas, series=[]):
        fs = [metrics.metric(data) for data in datas]
        
        def runkey(run): 
            return map(lambda m: metrics.evalmetric(m, run), series)
        
        if len(series) == 0: runsets = [(["run"], self.runs)]
        else: runsets = itertools.groupby(sorted(self.runs, key=runkey), runkey)
        
        for key, runset in runsets:
            print str(key)
            for run in runset:
                print ', '.join([str(f(run)) for f in fs])                

    def graph(self, datax, datay, fname=None, series=[]):
        fx, fy = metrics.metric(datax), metrics.metric(datay)
        
        pyplot.clf()
        pyplot.xlabel(str(fx)), pyplot.ylabel(str(fy))
        
        def runkey(run): 
            return map(lambda m: metrics.evalmetric(m, run), series)
        
        if len(series) == 0: 
            runsets = [(["run"], self.runs)]
        else: 
            runsets = itertools.groupby(sorted(self.runs, key=runkey), runkey)
        
        for key, runset in runsets:
            x, y = zip(*[(fx(run), fy(run)) for run in runset])
            pyplot.plot(x, y, '-o', label=str(key))

        pyplot.legend(loc=0)        
        self.handlegraph(fname)
        
    def handlegraph(self, fname=None):
        if fname: pyplot.savefig(os.path.join(config.fullrunsdir, self.runid, fname))
        else: pyplot.show()