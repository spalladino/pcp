from subprocess import *
from config import *

class Runner:
    
    def __init__(self):
        pass
    
    def run(self, runid=None, props={}):
        if runid: 
            props["run.id"] = runid
            props["data.filename"] = str(runid) + ".data"
        
        path = ";".join([basedir + path for path in classpaths])
        defs = " ".join(["-D%s=%s" % (key, value) for key, value in props.iteritems() ])
        
        command = "java %s -classpath %s pcp.Main" % (defs, path)
        
        p = Popen(command, stdout=PIPE, cwd=basedir+"/pcp")
        return p.communicate()


if __name__ == '__main__':
    out, err = Runner().run()
    print out
    print err
