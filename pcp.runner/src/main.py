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
    
    files = bnc.filesvlow90()
    print 'Processing vlow: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_vlow, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100920BNC90VLOW'
            )
    
    files = bnc.fileslow90()
    print 'Processing low: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_low, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100920BNC90LOW'
            )
    
    files = bnc.fileshigh90()
    print 'Processing high: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_high, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100920BNC90HIGH'
            )
                
                   
if __name__ == '__main__':
    main()
    
    
    
