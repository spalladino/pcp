import metrics
import itertools
import data
import aggregate

from processor import *

class GnuPlotProcessor(Processor):
    
    def process(self, ids=[], datas=[], series=[], runfilter=None, datafilter=None, aggr=aggregate.concat):
        runsets = list(self.makerunsets(ids, runfilter, datafilter, True))
        runseries = list(self.makerunsets(series, runfilter, datafilter, True))
        runkey = lambda run: map(lambda m: metrics.evalmetric(m, run), series)
        
        for i,(key,runserie) in enumerate(runseries):
            print '#Serie %s: %s' % (i+1, ', '.join(['%s: %s' % (s,k) for s,k in zip(series,key)]))

        for i,(key,runset) in enumerate(runsets):
            print '#Runset %s: %s' % (i+1, ', '.join(['%s: %s' % (s,k) for s,k in zip(ids,key)]))
        
        for runset in runsets:
            self.handle_runset(runset, runseries, datas, aggr, runkey)
    
class GnuPlotGapsProcessor(GnuPlotProcessor):
    
    def handle_runset(self, runset, runseries, datas, aggr, runkey):
        print '#Handling runset ', runset[0]
        all_gaps = []
        
        for i, (k,rungroup) in enumerate(groupby_sorted(runset[1], key=runkey)):
            print '#Serie %s: %s' % (i+1, k)
            gaps = list(self.timed_gaps(rungroup))
            all_gaps.append(gaps)
            if len(gaps) == 0:
                print '0.0 \t 0.0'
            for time,gap in gaps:
                print '%s \t %s' % (time,gap)            
            print '\n\n'  
        
        #print '\nAll gaps for runset ', runset[0]
        maxlen = max([len(l) for l in all_gaps])
        for l in all_gaps:
            if len(l) < maxlen:
                l.extend([(None, None)] * (maxlen - len(l)))
        
        #print zip(*all_gaps)
        for gaps in zip(*all_gaps):
            #print '\t'.join(['%s \t %s' % (time,gap) for time,gap in gaps])
            pass
                  
        
    def timed_gaps(self, rungroup):
        idx = 4
        def not_none(x): return x != None
        gaps = filter(not_none, rungroup[idx]['root.gaps'])
        if len(gaps) == 0: return []
        gaptime = float(rungroup[idx]['solution.time']) / float(len(gaps))
        times = [float(i) * gaptime for i in range(len(gaps))]
        return zip(times, gaps)