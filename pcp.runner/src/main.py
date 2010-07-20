from fixture import *
from config import *

import runs
import runs.branch as branch
import runs.heur as heur
import runs.model as model
import runs.preprocess as preproc

import fetcher
import files
import os
 
def main():
    files = preproc.files()
    print 'Processing: ', files

    Fixture().newrun(
                preproc.runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100720PREPROC2'
            )
    return


if __name__ == '__main__':
    main()
    
    
    
