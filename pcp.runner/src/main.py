from fixture import *
from config import *

import runs
import fetcher
import files
import os

if __name__ == '__main__':
    files = runs.fetch_branch_files()   
    print 'Processing: ', files
   
    Fixture().newrun(
                runs.branchstaticruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100707BRANCHSTATIC2',
            )
    
    
    
    

