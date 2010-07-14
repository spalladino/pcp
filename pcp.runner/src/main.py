from fixture import *
from config import *

import runs
import fetcher
import files
import os

def main():
    Fixture().resume()
    
    files = list(runs.fetch_branch_files())
    print 'Processing: ', files

    Fixture().newrun(
                runs.branchboundingruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100714BRANCHDSATURBOUNDS'
            )
    
    return
    
   
    Fixture().newrun(
                runs.branchsosruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100713BRANCHSOS'
            )


if __name__ == '__main__':
    main()
    
    
    
