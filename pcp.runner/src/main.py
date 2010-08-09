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
import files
import os
 
def main():

    files = bnc.files()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.pruning_noub_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100805PRUNINGNOUB'
            )
    
    files = bnc.files()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.emph_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100805EMPH'
            )
    
    files = bnc.files()
    print 'Processing: ', files

    Fixture().newrun(
                bnc.prob_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100805PROBING'
            )
    
if __name__ == '__main__':
    main()
    
    
    
