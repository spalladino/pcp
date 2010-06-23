from processor import *
from sqlprocessor import *

class LatexSqlProcessor(SqlProcessor):

    def __init__(self, runid=None):
        SqlProcessor.__init__(self, runid)

    def printHeader(self, ids, datas, series, runseries):
        print 'Data:'
        print '\\begin{itemize}'
        for d in datas:
            print '\item %s' % d
        
        print '\\end{itemize}'
        print 'Series:'
        print '\\begin{itemize}'
        count = 0
        for i, (key, runset) in enumerate(runseries):
            print '\item S%s: %s' % (i + 1, ', '.join(['%s: %s' % (s, k) for (s, k) in zip(series, key)]))
            count += 1
        
        print '\\end{itemize}'
        print '\\begin{tabular}{|%s|}' % ('c' * len(ids) + (('|' + ('c' * (len(datas)))) * count))
        print '\hline'
        print '\\multicolumn{%s}{|c|}{Id} & ' % len(ids) + ' & '.join(['\\multicolumn{%s}{|c|}{S%s}' % (len(datas), i + 1) for i in range(count)])
        print '\\\\'
        print '\hline'

    def printFooter(self):
        print '\hline \n \\end{tabular}'

    def keys(self, ids):
        cmd = self.selectcmd(ids) + " GROUP BY " + self.fieldslist(ids)
        return self.conn.execute(cmd)
    
    def process(self, ids=[], datas=[], series=[], runfilter=None, datafilter=None):
        fs = [metrics.metric(data) for data in datas]
        runseries = self.makerunsets(series, runfilter, datafilter)
        fields = ids + datas + series
        
        self.createtable(fields)
        self.populatedb(fields)
        
        keys = self.keys(ids)
        
        self.printHeader(ids, datas, series, runseries)
        
        for key in keys:
            pass
#        for key, runset in runsets:
#            print ' & '.join([str(k).replace('\\', '') for k in key] \
#                 + flatten([[str(f(run)).rjust(4) for f in fs] for run in unique_justseen_sorted(runset, key=runkey)])) \
#                 + '\n\\\\'  
        
        self.printFooter()
        