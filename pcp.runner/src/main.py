from fixture import *
from config import *

import runs
import fetcher
import files
import os

if __name__ == '__main__':
    
    files = list(runs.fetch_branch_files())
    print 'Processing: ', files
   
    Fixture().newrun(
                runs.branchdynruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100713BRANCHDYN2'
            )
    
    Fixture().newrun(
                runs.branchsosruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100713BRANCHSOS'
            )

