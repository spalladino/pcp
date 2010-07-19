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
                runs.branchdynruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100716BRANCHDYN4'
            )
    return


if __name__ == '__main__':
    main()
    
    
    
