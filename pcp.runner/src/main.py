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

import fetcher
import files as f
import os
 
def main():

    files = f.dimacs
    print 'Processing: ', files

    Fixture().newrun(
                bnc.emph_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100818EMPHDIMACS'
            )
    
    files = bnc.files()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.prob_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100818PROBDIMACS'
            )
    
if __name__ == '__main__':
    main()
    
    
    
