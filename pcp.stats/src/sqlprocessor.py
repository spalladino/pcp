import loader
import config
import os
import metrics
import itertools
import data
import sqlite3

from processor import *

class SqlProcessor(Processor):
    
    def __init__(self, runid=None):
        Processor.__init__(self, runid)
        self.conn = sqlite3.connect(":memory:")
        self.tablename = "DATA%s" % self.runid

    def createtable(self, fields):
        create_cmd = "CREATE TABLE %s (%s)" % (self.tablename, ",".join(["'%s' %s" % self.nametype(n) for n in fields]))
        self.conn.execute(create_cmd)

    def filterruns(self, runfilter, datafilter):
        runs = self.runs
        if runfilter:
            runs = filter(runfilter, self.runs) 
        if datafilter:
            fdatafilter = lambda run: all([metrics.evalmetric(k, run) == val for k,val in datafilter.iteritems()])
            runs = filter(fdatafilter, runs)
        self.runs = runs

    def populatedb(self, fields):
        def makerow(run): return tuple([metrics.evalmetric(metric, run) for metric in fields])
        rows = [ makerow(run) for run in self.runs ]
        self.conn.executemany(("INSERT INTO %s VALUES (%s)" % (self.tablename, ",".join(["?"] * len(fields)))), rows)

    def querydb(self, series, data, sqlfilter):
        cmd = "SELECT * FROM %s %s" % (self.tablename, sqlfilter)
        return self.conn.execute(cmd)
        
    def selectcmd(self, fields="*"):
        return "SELECT %s FROM %s " % (self.fieldslist(fields), self.tablename)
    
    def fieldslist(self, fields=[], func=None):
        if fields and len(fields) > 0:
            return ", ".join(['"%s"' % field for field in fields])
        return "%s(*)" % func if func else "*"
    
    def getdata(self, datafields, key, serie):
        cmd = self.selectcmd()
