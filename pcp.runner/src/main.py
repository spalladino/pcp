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
    
    files = bnc.files90()
    print 'Processing primal frequency on BNC: ', files
    Fixture().newrun(
                bnc.primal_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110131BNCPRIMALFREQ')
    
    files = f.dimacs
    print 'Processing dimacs with cplex using branch and cut standard and multiple configs for PCP: ', files
    Fixture().newrun(
                bnc.hopefully_final_runs_dimacs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110131BNCDIMACSPCPVSCPLEX')
    
    files = branch.files()
    print 'Processing Branch Static 4: ', files
    Fixture().newrun(
                branch.branchstaticmanualruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110131BRANCHSTATIC4')
 
def old():
    
    files = branch.files()
    print 'Processing Branch Static 3: ', files
    Fixture().newrun(
                branch.branchstaticmanualruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20101215BRANCHSTATIC3')
    
    return
    
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
    
    
    
