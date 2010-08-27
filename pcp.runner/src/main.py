from fixture import *
from config import *

import runs
import runs.branch as branch
import runs.heur as heur
import runs.model as model
import runs.preprocess as preproc
import runs.primal as primal
import runs.cuts as cuts
import runs.bnc as bnc
import runs.bncmodels as bncm

import fetcher
import files as f
import os
 
def main():
    
   
    files = bncm.files_lowdens()
    print 'Processing: ', files

    Fixture().newrun(
                bncm.lowdens_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100826BNCMLOW2'
            )
    
    
    files = bncm.files_highdens()
    print 'Processing: ', files

    Fixture().newrun(
                bncm.highdens_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100826BNCMHIGH2'
            )
    
    
if __name__ == '__main__':
    main()
    
    
    
