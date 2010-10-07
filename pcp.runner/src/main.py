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
                bnc.hopefully_final_runs_vlow_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101006BNC90VLOW2'
            )
    
    files = bnc.fileslow90()
    print 'Processing low: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_low_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101006BNC90LOW2'
            )
    
    files = bnc.fileshigh90()
    print 'Processing high: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_high_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101006BNC90HIGH2'
            )
    
    files = bnc.fileshk()
    print 'Processing HK1: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_low, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101006BNCHK'
            )
    
    files = bnc.fileshk()
    print 'Processing HK2: ', files
    
    Fixture().newrun(
                bnc.hopefully_final_runs_low_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101006BNCHK2'
            )
    
    files = f.dimacs
    print 'Processing DIMACS: ', files
    
    Fixture().newrun(
                bnc.hopefully_final_runs_low_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101006BNCDIMACS2'
            )            
                   
if __name__ == '__main__':
    main()
    
    
    
