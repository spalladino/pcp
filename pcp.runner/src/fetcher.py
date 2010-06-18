import re, os

class Fetcher:
    def __init__(self, basedir):
        self.basedir = basedir
    
    def fetch_files(self, dir, *patterns):
        return [os.path.join(dir,file) for file in os.listdir(os.path.join(self.basedir, dir)) for r in patterns if re.match(r, file)]