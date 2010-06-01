import loader
import config
import os
import metrics
import itertools
import data

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

    def latextable(self, ids=[], datas=[], series=[], runfilter=None, datafilter=None):
        fs = [metrics.metric(data) for data in datas]
        runkey = lambda run: map(lambda m: metrics.evalmetric(m, run), series)
        
        runsets = self.makerunsets(ids, runfilter, datafilter)
        runseries = self.makerunsets(series, runfilter, datafilter)
        
        print 'Data:'
        print '\\begin{itemize}'
        for d in datas:
            print '\item %s' % d
        print '\\end{itemize}'
        
        print 'Series:'
        print '\\begin{itemize}'
        count= 0
        for i,(key,runset) in enumerate(runseries):
            print '\item S%s: %s' % (i+1, ', '.join(['%s: %s' % (s,k) for s,k in zip(series,key)]))
            count += 1
        print '\\end{itemize}'
        
        print '\\begin{tabular}{|%s|}' % ('c' * len(ids) + (('|' + ('c' * (len(datas)))) * count))
        print '\\multicolumn{%s}{|c|}{Id} & ' % len(ids) + ' & '.join(['\\multicolumn{%s}{|c|}{S%s}' % (len(datas), i+1) for i in range(count)])
        print '\\\\'
        
        for key, runset in runsets:
            print ' & '.join([str(k) for k in key] \
                 + self.flatten([[str(f(run)).rjust(4) for f in fs] for run in sorted(runset, key=runkey)])) \
                 + '\n\\\\'  
        
        print '\\end{tabular}'

    def simpletable(self, datas, series=[], runfilter=None, datafilter=None):
        fs = [metrics.metric(data) for data in datas]
        
        runsets = self.makerunsets(series, runfilter, datafilter)
        
        print ', '.join(datas)
        for key, runset in runsets:
            print ', '.join(['%s: %s' % (s,k) for s,k in zip(series,key)])
            for run in runset:
                print ', '.join([str(f(run)).rjust(6) for f in fs])                


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

    def flatten(self, xss):
        l = []
        for xs in xss: 
            for x in xs: 
                l.append(x)
        return l