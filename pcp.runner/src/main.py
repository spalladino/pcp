from fixture import *
from config import *

import fetcher
import files
import os

datadir = ".\\..\\..\\data\\"

if __name__ == '__main__':
    files = fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in')   
    print 'Processing: ', files
   
    Fixture().newrun(
            [
                {
                    'strategy.symmetry': 'MinimumNodeLabelVerticesNumber',
                },
                
            ], 
            
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100623215001',
                initial = 600
            )
    
