import fetcher
import files as f

from config import datadir
from common import *

def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchpart', 'p[\\d]t[\\d]e05n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n140d0(1|2|3|4)\\.00[\\d]\\.in') + \
        f.dimacs

runs = [{
                       'solver.kind': 'Preprocessor'
                   }]