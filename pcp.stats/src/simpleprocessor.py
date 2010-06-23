import metrics
import itertools
import data

from processor import *

class SimpleProcessor(Processor):

    def __init__(self, runid=None):
        Processor.__init__(self, runid)
        
    def process(self, datas, series=[], runfilter=None, datafilter=None):
        fs = [metrics.metric(data) for data in datas]
        
        runsets = self.makerunsets(series, runfilter, datafilter)
        
        print ', '.join(datas)
        for key, runset in runsets:
            print ', '.join(['%s: %s' % (s,k) for s,k in zip(series,key)])
            for run in runset:
                print ', '.join([str(f(run)).rjust(6) for f in fs])                