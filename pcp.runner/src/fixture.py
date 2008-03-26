import os.path
import pickle
import yaml
import time
import gc

from runner import *
from config import *
from datetime import datetime

curr_path = basedir + runsdir + 'current.runs'
cfgs_path = basedir + runsdir + 'current.cfgs'

def resume():
    return Fixture().resume()

def newrun(runs):
    return Fixture().newrun(runs)

class Fixture:

    def __init__(self):
        self.runs = []
        self.runid = None
        self.successful = 0
    
        
    def newrun(self, runs, runid=datetime.now().strftime("%Y%m%d%H%M%S")):
        self.runs = runs
        self.runid = runid
        self.successful = 0
        self.init_status()
        self.execute()
    
    
    def resume(self):
        if self.load_status():
            return self.execute() 
        return False
    
    
    def execute(self):
        if not os.path.isdir(basedir + runsdir + self.runid.strip()):
            os.mkdir(basedir + runsdir + self.runid.strip())
            
        for run in self.runs:
            runner = Runner()
            out, err = runner.run(self.runid, self.successful + 1, run)
            self.add_success_status()
            time.sleep(sleeptime)
            gc.collect()
        
        self.clear_status()
        return True
            

    def load_status(self):
        
        if not os.path.isfile(curr_path) or not os.path.isfile(cfgs_path):
            return False
        
        with open(cfgs_path, 'r') as current:
            self.runs = yaml.load(current)
        
        with open(curr_path, 'r') as current:
            self.runid = current.readline()
            while (current.readline() == 'OK\n'):
                self.successful += 1
            
            self.runs = self.runs[self.successful : -1]
            if current.readline() == 'END\n':
                return False
        
        return True
        
        
    def init_status(self):
        with open(curr_path, 'w') as current:
            current.write(self.runid)
            current.write("\n")
        
        with open(cfgs_path, 'w') as current:
            yaml.dump(self.runs, current)
        
        
    def clear_status(self):
        os.remove(curr_path)
        os.remove(cfgs_path)
        
        
    def add_success_status(self):
        self.successful += 1
        with open(curr_path, 'a') as current:
            current.write("OK\n")
            
            
    def end_status(self):
        with open(curr_path, 'a') as current:
            current.write("END\n")
            
    def clear_status(self):
        os.remove(curr_path)
            