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
    
    files = bnc.filesn90d60fixedpart()
    print 'Processing fixed parts 60 Density: ', files
    Fixture().newrun(
                bnc.highdens_final_vs_cplex, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110512D60FIXEDPARTS')

def old():

    files = f.dimacs
    print 'Processing Final DIMACS Low Density: ', files
    Fixture().newrun(
                bnc.lowdens_final, 
                files = files[9:],
                dirs = [ ".\\..\\data\\" ],
                runid = '20110427LOWDENSDIMACSCONT')
    
    files = f.dimacs
    print 'Processing Final DIMACS High Density: ', files
    Fixture().newrun(
                bnc.highdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110427HIGHDENSDIMACS')
    
    files = bnc.fileslowhk90()
    print 'Processing Final Holme Kim BNC Low Density: ', files
    Fixture().newrun(
                bnc.lowdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110427LOWDENSFINALHK')
    
    files = bnc.fileshighhk90()
    print 'Processing Final Holme Kim BNC High Density: ', files
    Fixture().newrun(
                bnc.highdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110427HIGHDENSFINALHK')


   
    files = bnc.fileslow90()
    print 'Processing Final BNC Low Density: ', files
    Fixture().newrun(
                bnc.lowdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110419LOWDENSFINAL')
    
    files = bnc.fileshigh90()
    print 'Processing Final BNC High Density: ', files
    Fixture().newrun(
                bnc.highdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110419HIGHDENSFINAL')
   
    files = bnc.fileslow90()
    print 'Processing BNC Low Density with More Pruning: ', files
    Fixture().newrun(
                bnc.lowdens_final_pruning_more, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110421LOWDENSPRUNINGMORE')
    
    files = bnc.fileshigh90()
    print 'Processing BNC High Density with More Pruning: ', files
    Fixture().newrun(
                bnc.highdens_final_pruning_more, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110421HIGHDENSPRUNINGMORE')


    files = bnc.fileslow90()
    print 'Processing Final BNC Low Density: ', files
    Fixture().newrun(
                bnc.lowdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110419LOWDENSFINAL')
    
    files = bnc.fileshigh90()
    print 'Processing Final BNC High Density: ', files
    Fixture().newrun(
                bnc.highdens_final, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110419HIGHDENSFINAL')
    
    files = bncm.files_highdens()
    print 'Processing bnc cuts for high dens S3: ', files
    Fixture().newrun(
                bncm.highdens_cuts_s3, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110414HIGHDENSCUTSS3')

    files = bncm.files_highdens()
    print 'Processing bnc cuts for high dens S5: ', files
    Fixture().newrun(
                bncm.highdens_cuts_s5, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110414HIGHDENSCUTSS5')

    files = [bncm.files_highdens()[-1]]
    print 'Processing bnc extra for high dens: ', files
    Fixture().newrun(
                bncm.highdens_runs_ext, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110414BNCHIGHDENSEXT2')        


        
    files = bncm.files_lowdens()
    print 'Processing bnc cuts for low dens: ', files
    Fixture().newrun(
                bncm.lowdens_cuts, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110412LOWDENSCUTS')

    files = primal.files()
    print 'Processing Primal DSatur branch Runs with less time: ', files
    Fixture().newrun(
                primal.primaldsaturbranchruns_lesstime, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110321PRIMALDSATURBRANCHLESSTIME')  
    
    files = bncm.files_highdens()
    print 'Processing Alternative BNC Runs High density: ', files
    Fixture().newrun(
                bncm.highdens_runs_phlfreq, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110323BNCMODELHIGHPHLFREQ')
    
    files = bncm.files_lowdens()
    print 'Processing Alternative BNC Runs Low density: ', files
    Fixture().newrun(
                bncm.lowdens_runs_phlfreq, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110323BNCMODELLOWPHLFREQ')
    
    files = bncm.files_highdens()
    print 'Processing Alternative BNC Runs High density: ', files
    Fixture().newrun(
                bncm.highdens_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110313BNCMODELHIGH')
    
    files = bncm.files_lowdens()
    print 'Processing Alternative BNC Runs Low density: ', files
    Fixture().newrun(
                bncm.lowdens_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110313BNCMODELLOW')
    
    files = primal.files()
    print 'Processing Primal DSatur branch Runs: ', files
    Fixture().newrun(
                primal.primaldsaturbranchruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110313PRIMALDSATURBRANCH')
        
    files = bnc.files90()
    print 'Processing BNC Pruning Runs: ', files
    Fixture().newrun(
                bnc.pruning_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110308PRUNINGREMAINING')
    
    files = bnc.files()
    print 'Processing BNC NoUB Pruning Runs: ', files
    Fixture().newrun(
                bnc.pruning_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110228PRUNINGPRIMALNOUB2')
    
    
    
    
    files = bnc.filesp1()
    print 'Processing BNC NoUB Pruning Runs P1: ', files
    Fixture().newrun(
                bnc.pruning_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110228PRUNINGPRIMALNOUBP1')
    
    files = bnc.filesp1()
    print 'Processing BNC Pruning Runs P1: ', files
    Fixture().newrun(
                bnc.pruning_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110228PRUNINGP1')
    
    files = bnc.files()
    print 'Processing BNC Pruning Runs: ', files
    Fixture().newrun(
                bnc.pruning_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110228PRUNING2')
    
    files = primal.files()
    print 'Processing Primal Static Runs: ', files
    Fixture().newrun(
                primal.primalstaticruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110219PRIMALSTATIC3')
    
    files = branch.files()
    print 'Processing Branch Static 4: ', files
    Fixture().newrun(
                branch.branchstaticmanualruns, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110131BRANCHSTATIC4')
    
    files = f.dimacs
    print 'Processing dimacs with cplex using branch and cut standard and multiple configs for PCP: ', files
    Fixture().newrun(
                bnc.hopefully_final_runs_dimacs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110131BNCDIMACSPCPVSCPLEX')
    
 
    
    files = bnc.files90()
    print 'Processing primal frequency on BNC: ', files
    Fixture().newrun(
                bnc.primal_runs, 
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20110131BNCPRIMALFREQ')
    
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
    
    
    
