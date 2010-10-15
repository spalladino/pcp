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
import runs.bncmodels as bncm

import fetcher
import files as f
import os
 
def main():
    
    files = model.files90()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNB90')
    
    files = model.files85()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNB85'
            )
    
    files = model.files80()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNB80'
            )
    
    files = model.files60()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns_noinit, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNBNOINIT60'
            )
    
    files = model.files70()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns_noinit, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNBNOINIT70'
            )
    
    files = model.files70()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNB70'
            )
    
    files = model.files80()
    print 'Processing Model: ', files
    Fixture().newrun(
                model.modelruns_noinit, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101015MODELBNBNOINIT80')
                   
if __name__ == '__main__':
    main()
    
    
    
