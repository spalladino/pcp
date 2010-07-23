from fixture import *
from config import *

import runs
import runs.branch as branch
import runs.heur as heur
import runs.model as model
import runs.preprocess as preproc
import runs.primal as primal
import runs.cuts as cuts

import fetcher
import files
import os
 
def main():
    
    Fixture().resume()
    
    files = cuts.files()
    print 'Processing: ', files

    Fixture().newrun(
                cuts.familiesruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100723CUTSFAMILIES'
            )
    
if __name__ == '__main__':
    main()
    
    
    
