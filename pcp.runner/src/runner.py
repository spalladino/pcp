from subprocess import *
from config import *

class Runner:
    
    def __init__(self):
        pass
    
    def run(self, runid=None, runindex=None, main="pcp.Main", props={}):
        if runid: 
            props["run.id"] = runid
            props["data.folder"] = basedir + runsdir + runid + "/"
        
        if runindex:
            props["data.filename"] = '%03d.run' % runindex
        
        props["data.generate"] = "true"
        
        path = ";".join([basedir + path for path in classpaths])
        defs = " ".join(["-D%s=%s" % (key, value) for key, value in props.iteritems() ])
        
        command = "java %s -classpath %s %s" % (defs, path, main)
        
        p = Popen(command, stdout=PIPE, cwd=basedir+"/pcp")
        return p.communicate()