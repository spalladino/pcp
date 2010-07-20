from fixture import *
from config import *

import runs
import runs.branch as branch
import runs.heur as heur
import runs.model as model
import runs.preprocess as preproc
import runs.primal as primal

import fetcher
import files
import os
 
def main():
    files = primal.files()
    print 'Processing: ', files

    Fixture().newrun(
                primal.primalsettingsruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100720PRIMALSET'
            )
    return
    
    files = primal.files()
    print 'Processing: ', files

    Fixture().newrun(
                primal.primalruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100720PRIMAL'
            )
    

if __name__ == '__main__':
    main()
    
    
    
