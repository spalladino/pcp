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
    
    files = bnc.filesvlow()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_vlow, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100908FINALVLOW2'
            )
    
    files = bnc.fileslow()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_low, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100908FINALLOW2'
            )
    
    files = bnc.fileshigh()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_high, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100908FINALHIGH2'
            )
                
                   
if __name__ == '__main__':
    main()
    
    
    
