from fixture import *
from config import *

import runs
import fetcher
import files
import os

if __name__ == '__main__':
    files = runs.fetch_heur_files()   
    print 'Processing: ', files
   
    Fixture().newrun(
                runs.dsaturruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100701DSATURS1M',
            )
    
    
    
    

