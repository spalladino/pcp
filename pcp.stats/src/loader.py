import config
import os
import glob
import yaml
import data

class Loader(object):

    def __init__(self, runid=None):
        if not runid:
            dirs = [os.path.join(config.fullrunsdir, path) for path in os.listdir(config.fullrunsdir) if os.path.isdir(os.path.join(config.fullrunsdir, path))]
            #dirs.sort(key= lambda x: os.path.getmtime(x))
            dirs.sort()
            runid = os.path.basename(dirs[-1])
        print "Processing Runid " + str(runid)
        self.runid = runid
        self.datadir = config.fullrunsdir + str(runid)

    def getrunid(self):
        return self.runid

    def load(self):
        return data.getobject(os.path.join(self.datadir, ('run.%s.pkl' % self.runid)), self.__do_load)
        
    def __do_load(self):
        files = [os.path.join(self.datadir, path) for path in os.listdir(self.datadir)]
        runs = []
        
        for file in files:
            root, ext = os.path.splitext(file)
            if os.path.isfile(file) and ext == '.run':
                with open(file, 'r') as f:
                    runs.append(yaml.load(f))
                    
        return runs
