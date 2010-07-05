import metrics
import itertools
import data

from processor import *

class LatexProcessor(Processor):

    def __init__(self, runid=None):
        Processor.__init__(self, runid)
        
    def process(self, ids=[], datas=[], series=[], runfilter=None, datafilter=None, aggr=concat):
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
        print '\hline'
        print '\\multicolumn{%s}{|c|}{Id} & ' % len(ids) + ' & '.join(['\\multicolumn{%s}{|c|}{S%s}' % (len(datas), i+1) for i in range(count)])
        print '\\\\'
        
        print ' & ' + ' & '.join(datas * count)
        print '\\\\'
        
        print '\hline'
        
        for key, runset in runsets:
            print ' & '.join([str(k).replace('\\', '') for k in key] \
                 + flatten([[str(aggr(map(f,rungroup))).rjust(4) for f in fs] for k,rungroup in groupby_sorted(runset, key=runkey)])) \
                 + '\n\\\\'  
        
        print '\hline \n \\end{tabular}'
