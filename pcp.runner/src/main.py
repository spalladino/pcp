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
    
    files = bnc.fileshigh()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_high, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100906FINALHIGH'
            )
                
                   
if __name__ == '__main__':
    main()
    
    
    
