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
    
    files = bnc.fileshk()
    print 'Processing HK1: ', files

    Fixture().newrun(
                bnc.hopefully_final_runs_low, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101008BNCHK'
            )
    
    files = bnc.fileshk()
    print 'Processing HK2: ', files
    
    Fixture().newrun(
                bnc.hopefully_final_runs_low_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101008BNCHK2'
            )
    
    files = f.dimacs
    print 'Processing DIMACS: ', files
    
    Fixture().newrun(
                bnc.hopefully_final_runs_low_2, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101008BNCDIMACS2'
            )            
                   
if __name__ == '__main__':
    main()
    
    
    
