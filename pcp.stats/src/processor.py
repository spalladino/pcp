import loader
import config
import os
import metrics
import itertools
import data

from itertools import imap, groupby
from operator import itemgetter
#from matplotlib import pyplot

class Processor(object):
    
    def __init__(self, runid=None):
        self.loader = loader.Loader(runid) 
        self.runs = self.loader.load()
        self.runid = self.loader.runid

    def makerunsets(self, series, runfilter, datafilter):
        runkey = lambda run: map(lambda m: metrics.evalmetric(m, run), series)
        
        runs = self.runs
        if runfilter:
            runs = filter(runfilter, self.runs) 
        if datafilter:
            fdatafilter = lambda run: all([metrics.evalmetric(k, run) == val for k,val in datafilter.iteritems()])
            runs = filter(fdatafilter, runs)
        
        if len(series) == 0:
            runsets = [(["run"], runs)]
        else:
            runsets = itertools.groupby(sorted(runs, key=runkey), runkey)
        
        return runsets

        
    def summary(self):
        for run in self.runs:
            print "Run fixture %s on %s (%s)" % (run['run.id'], run['run.filename'], run['data.filename'])
            print " Graph N=%03d E=%03d P=%03d" % (run['graph.nodes'], run['graph.edges'], run['graph.partitions'])
            print " Time: %d millis (chi=%d) with %s" % (run['solution.time'], run.get('solution.chi') or 0, run.get('solver.kind') or '?')
            if run.has_key('preprocess.time'): print " Preprocessing: %d millis" % run['preprocess.time']
            
            if 'cuts' in run:
                for name, cut in run['cuts'].iteritems():
                    print " %s: %d cuts in %d millis" % (name, cut['count'], cut['ticks'])
            
            print

    def graph(self, datax, datay, fname=None, series=[]):
        fx, fy = metrics.metric(datax), metrics.metric(datay)

        pyplot.clf()
        pyplot.xlabel(str(fx)), pyplot.ylabel(str(fy))
        
        runsets = self.makerunsets(series, runfilter)
        
        for key, runset in runsets:
            x, y = zip(*[(fx(run), fy(run)) for run in runset])
            pyplot.plot(x, y, '-o', label=str(key))

        pyplot.legend(loc=0)        
        
        if fname: pyplot.savefig(os.path.join(config.fullrunsdir, self.runid, fname))
        else: pyplot.show()

def unique_justseen_sorted(iterable, key=None):
    return unique_justseen(sorted(runset, key=key), key=key)

def groupby_sorted(iterable, key=None):
    return groupby_store(sorted(iterable, key=key), key)

def groupby_store(data, keyfunc=None):
    groups = []
    uniquekeys = []
    data = sorted(data, key=keyfunc)
    for k, g in groupby(data, keyfunc):
        groups.append(list(g))
        uniquekeys.append(k)
    return zip(uniquekeys,groups)

def unique_justseen(iterable, key=None):
    "List unique elements, preserving order. Remember only the element just seen."
    return imap(next, imap(itemgetter(1), groupby(iterable, key)))

def flatten(xss):
    l = []
    for xs in xss: 
        for x in xs: 
            l.append(x)
    return l

def concat(list):
    if len(list) > 0: return str(list)
    else: return str(list[0])
   
def first(list):
    return str(list[0])
    
def avg(list):
    try:
        if len(list) == 0: return 0.0
        return float(sum(map(float,list), 0.0)) / float(len(list))
    except:
        "ERR"

def tryfloat(s):
    try: return float(s)
    except: return 0.0
    
    