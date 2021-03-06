import processor
import metrics
import aggregate

from latexprocessor import LatexProcessor
from gnuplotprocessor import *
from simpleprocessor import SimpleProcessor

def simpletable(p):    
    p.simpletable(["graph.nodes", "preprocess.time","solution.time", "solution.chi"], ["path.enabled", 
                    "clique.enabled",
                    "blockColor.enabled",
                    "gprime.holes.enabled",
                    "holes.enabled",
                    "branch.prios.enabled",
                    "primal.enabled"], None, {'run.folder': '.\\..\\data\\rand075\\'})
    
def model_latextable(p):    
        p.process(
                ids=[metrics.FileName()],
                datas=['solution.gap','cuts.niters','solution.time'], 
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
                datafilter= {'strategy.adjacency': 'AdjacentsNeighbourhood', 'strategy.colorBound': 'UpperNodesSum', 'strategy.partition': 'PaintExactlyOne', 'strategy.objective': 'Equal', 'model.adjacentsNeighbourhood.useCliqueCover':'true'},
                runfilter= None,
                aggr= aggregate.avg
                    )

def dsatur_latextable():    
        LatexProcessor('20100701DSATURS1M').process(
                ids=[metrics.FileName()],
                datas=[metrics.FloatMetric('solution.chi', "{0:.1f}"), metrics.FloatMetric('solution.found', "{0:.3f}")], 
                series=['strategy.coloring', 'dsatur.partition.weight.size', 'dsatur.partition.weight.colorCount', 'dsatur.partition.weight.uncolored'],
                datafilter= {'dsatur.partition.weight.size':'1', 'dsatur.partition.weight.colorCount':'10000', 'dsatur.partition.weight.uncolored':'100'},
                runfilter= None,
                aggr= aggregate.avg
                )

def model_bnb_latextable():    
        LatexProcessor('20100820MODELBNB').process(
                ids=[metrics.FileName()],
                datas=['solution.gap', 'solution.chi'], 
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )
        
def branch_latextable():    
        LatexProcessor('20100707BRANCHSTATIC2').process(
                ids=[metrics.FileName()],
                datas=['solution.gap', 'solution.gapfound', 'solution.nnodes'], 
                series=['branch.prios.enabled', 'branch.prios.psadjacent', 'branch.prios.colorindex'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )

def branch_dyn_latextable():    
        branchdyn2 = '20100713BRANCHDYN2'
        branchdyn3 = '20100715BRANCHDYN3'
        branchdyn5 = '20100719BRANCHDYN5'
        
        LatexProcessor(branchdyn5).process(
                ids=[metrics.FileName()],
                datas=['solution.gap', metrics.Ks('solution.gapfound'), metrics.Ks('solution.nnodes')], 
                series=['branch.dynamic.fractional', 'branch.dynamic.dsatur', 'branch.direction', 'branch.dynamic.fractional.most', 'branch.dynamic.dsatur.conseccolors', 'solver.useCplexPrimalHeuristic'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )

def branch_sos_latextable():    
        branchbounds = '20100713BRANCHSOS'
        LatexProcessor(branchbounds).process(
                ids=[metrics.FileName()],
                datas=['solution.gap', 'solution.gapfound', 'solution.nnodes'], 
                series=['model.useSOS', 'branch.prios.enabled', 'branch.prios.psadjacent', 'branch.prios.colorindex'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )

def branch_bounds_latextable():    
        branchbounds = '20100714BRANCHDSATURBOUNDS'
        LatexProcessor(branchbounds).process(
                ids=[metrics.FileName()],
                datas=['solution.gap', 'solution.gapfound', 'solution.nnodes'], 
                series=['branch.singlevar', 'branch.boundws'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )

def preprocess_latextable():    
        file = '20100720PREPROC2'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=[metrics.PreprocessRemoved('partitions'), metrics.PreprocessRemoved('nodes'), metrics.PreprocessRemoved('edges')], 
                series=['solver.kind'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )

def primal_latextable():    
        file = '20100720PRIMAL'
        filestatic = '20100723PRIMALSTATIC2'
        
        LatexProcessor(filestatic).process(
                ids=[metrics.FileName()],
                datas=['solution.gap', metrics.Ks('solution.gapfound'), metrics.Ks('solution.nnodes')],  
                series=[ 'branch.dynamic.dsatur','primal.enabled', 'solver.useCplexPrimalHeuristic', 'primal.onlyonup', 'branch.direction'],
                datafilter= {'branch.dynamic.dsatur': 'false'},
                runfilter= None,
                aggr= aggregate.avg 
                )

def primal_settings_latextable():    
        file = '20100720PRIMALSET'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.gap', metrics.Ks('solution.gapfound'), metrics.Ks('solution.nnodes')],  
                series=['primal.enabled', 'solver.useCplexPrimalHeuristic', 'primal.onlyonup', 'branch.direction', 'primal.everynodes'],
                datafilter= {'branch.direction': '0'},
                runfilter= None,
                aggr= aggregate.avg 
                )
        
def cuts_clique_latextable():    
        file = '20100722CLIQUECUTS'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.gap','cuts.niters','solution.time'],   
                series=['clique.colorsAsc', 'clique.backtrackBrokenIneqs', 'clique.backtrackLastCandidate'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )
        
def cuts_blockcolor_latextable():    
        file = '20100726BLOCKCOLOR2'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.gap','cuts.niters','solution.time'],   
                series=['blockColor.enabled','blockColor.usePool'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )        
        
def cuts_iset_latextable():    
        file = '20100722PATHHOLECUTS'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.gap','cuts.niters','solution.time'],   
                series=['cuts.iset.usePathsAlgorithm', 'cuts.iset.useBreakingSymmetry'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )

def cuts_families_latextable():
        file = '20100726CUTSFAMILIES2'
        time_data = ['solution.gap','cuts.niters','solution.time']
        number_data = [metrics.CutMetric('BlockColor'), metrics.CutMetric('Clique'), metrics.CutMetric('Hole'), metrics.CutMetric('Path'), metrics.CutMetric('GPHole'), metrics.CutMetric('GPPath')]
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=number_data,   
                series=['blockColor.enabled','clique.enabled', 'path.enabled', 'gprime.path.enabled','solver.useCplexCuttingPlanes'],
                datafilter= {'blockColor.enabled': 'true', 'path.enabled': 'true', 'gprime.path.enabled': 'true'},
                runfilter= None,
                aggr= aggregate.avg 
                )

def bnc_pruning_latextable():
        file = '20100805PRUNINGNOUB'
        file2 = '20100803PRUNINGWITHUB'
        LatexProcessor(file2).process(
                ids=[metrics.FileName()],
                datas=['solution.leafheur.count','solution.nnodes','solution.time','solution.gap'],   
                series=['pruning.enabled', 'pruning.remaining', 'primal.useub'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )

def bnc_probing_latextable():
        file = '20100813PROBING'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.nnodes','solution.time','solution.gap'],   
                series=['solver.probing'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )
        
def bnc_emph_latextable():
        file = '20100813EMPH'
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.nnodes','solution.time','solution.gap'],   
                series=['solver.mipEmphasis'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )

def bnc_models_latextable():
        filel = '20100826BNCMLOW2'
        fileh = '20100902BNCMHIGH2FIXED'
        
        LatexProcessor(filel).process(
                ids=[metrics.FileName()],
                datas=['solution.chi','solution.nnodes','solution.time','solution.gap'],   
                datafilter= None,
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective', 'solver.probing', 'solver.mipEmphasis'],
                runfilter= None,
                aggr= aggregate.avg 
                )
        
        LatexProcessor(fileh).process(
                ids=[metrics.FileName()],
                datas=['solution.chi','solution.nnodes','solution.time','solution.gap'],   
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective', 'solver.probing', 'solver.mipEmphasis'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg 
                )

def bnc_models_fix_latextable():
        fileh = '20100901BNCMODELHIGHFIXVN'
        LatexProcessor(fileh).process(
                ids=[metrics.FileName()],
                datas=['solution.chi','solution.nnodes','solution.time','solution.gap'],   
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective', 'solver.probing', 'solver.mipEmphasis'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.concat 
                )
        
        fileh = '20100826BNCMHIGH2'
        LatexProcessor(fileh).process(
                ids=[metrics.FileName()],
                datas=['data.filename', 'run.filename'],   
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective', 'solver.probing', 'solver.mipEmphasis'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.concat 
                )
        
def final_1_latextable():
    
        series = ['solver.kind', 'solver.useCplexPrimalHeuristic', 'solver.useCplexPreprocess', 'solver.useCplexCuttingPlanes', 'model.variables.boundOnDegree', 'model.variables.boundOnPartitionIndex', 'model.variables.fixClique',]
        datas=['solution.chi','solution.nnodes','solution.time','solution.gap']
        
        files = ['20100908FINALVLOW2', '20100908FINALLOW2', '20100908FINALHIGH2']
        
        for file in files:
            LatexProcessor(file).process(
                    ids=[metrics.FileName()],
                    datas=datas,   
                    series=series,
                    datafilter= None,
                    runfilter= None,
                    aggr= aggregate.avg 
                    )

def final_2_latextable():
    
        series = ['solver.kind', 'solver.useCplexPrimalHeuristic', 'solver.useCplexPreprocess', 'solver.useCplexCuttingPlanes', 'model.variables.boundOnDegree', 'model.variables.boundOnPartitionIndex', 'model.variables.fixClique',]
        datas=['solution.chi','solution.nnodes','solution.time','solution.gap']
        
        files = ['20100908FINALVLOW2', '20100908FINALLOW2', '20100908FINALHIGH2']
        
        for file in files:
            LatexProcessor(file).process(
                    ids=[metrics.FileName()],
                    datas=datas,   
                    series=series,
                    datafilter= None,
                    runfilter= None,
                    aggr= aggregate.avg 
                    )

def final_cplex_latextable():
        series = ['solver.kind', 'model.variables.boundOnDegree', 'model.variables.boundOnPartitionIndex', 'model.variables.fixClique',]
        datas=['solution.gap', 'solution.time']
        
        #files = ['20100920BNC90VLOW', '20100920BNC90LOW', '20100920BNC90HIGH']
        #files = ['20101006BNC90VLOW2', '20101006BNC90LOW2', '20101006BNC90HIGH2', '20101008BNCDIMACS2']
        files = ['20110512D60FIXEDPARTS']
        
        for file in files:
            LatexProcessor(file).process(
                    ids=[metrics.FileName()],
                    datas=datas,   
                    series=series,
                    datafilter= None,
                    runfilter= None,
                    aggr= aggregate.avg 
                    )
            
def final_dimacs_latextable():
    
        series = ['solver.kind', 'solver.useCplexPrimalHeuristic', 'solver.useCplexPreprocess', 'solver.useCplexCuttingPlanes', 'model.variables.boundOnDegree', 'model.variables.boundOnPartitionIndex', 'model.variables.fixClique']
        datas=['solution.chi','solution.time','solution.gap']
        
        files = ['20110427LOWDENSDIMACS','20110427LOWDENSDIMACSCONT']
        files = ['20110427HIGHDENSDIMACS']
        
        for file in files:
            LatexProcessor(file).process(
                    ids=[metrics.FileName()],
                    datas=datas,   
                    series=series,
                    datafilter= None,
                    runfilter= None,
                    aggr= aggregate.concat 
                    )

def dimacs_data_latextable():
    
        series = []
        datas=['graph.nodes','graph.density','graph.edges']
        
        files = ['20110427LOWDENSDIMACS','20110427LOWDENSDIMACSCONT','20110427HIGHDENSDIMACS']
        
        for file in files:
            LatexProcessor(file).process(
                    ids=[metrics.FileName()],
                    datas=datas,   
                    series=series,
                    datafilter= None,
                    runfilter= None,
                    aggr= aggregate.concat 
                    )


def models_graphs_symmetry():
    GnuPlotGapsProcessor('20100629MODELS').process(
        ids=[metrics.FileName()],
        datas=['data.filename', 'solution.gap','cuts.niters','solution.time'], 
        series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
        #datafilter= None,
        datafilter= {'strategy.adjacency': 'AdjacentsNeighbourhood', 'strategy.colorBound': 'UpperNodesSum', 'strategy.partition': 'PaintExactlyOne', 'strategy.objective': 'Equal', 'model.adjacentsNeighbourhood.useCliqueCover':'true'},
        runfilter= None,
        aggr= aggregate.concat
    )
    
def models_graphs_adjacency():
    #idx 2
    GnuPlotGapsProcessor('20100629MODELS').process(
        ids=[metrics.FileName()],
        datas=['data.filename', 'solution.gap','cuts.niters','solution.time'], 
        series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
        datafilter= { 'strategy.symmetry': 'UseLowerLabelFirst', 'strategy.colorBound': 'UpperNodesSum', 'strategy.partition': 'PaintExactlyOne', 'strategy.objective': 'Equal', 'model.adjacentsNeighbourhood.useCliqueCover':'true'},
        runfilter= None,
        aggr= aggregate.concat
    )
    
def models_graphs_colorbound():
    #idx 1
    GnuPlotGapsProcessor('20100629MODELS').process(
        ids=[metrics.FileName()],
        datas=['data.filename', 'solution.gap','cuts.niters','solution.time'], 
        series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
        datafilter= { 'strategy.adjacency': 'AdjacentsNeighbourhood', 'strategy.symmetry': 'UseLowerLabelFirst', 'strategy.partition': 'PaintExactlyOne', 'strategy.objective': 'Equal', 'model.adjacentsNeighbourhood.useCliqueCover':'true'},
        runfilter= None,
        aggr= aggregate.concat
    )
    
def models_new_bnb_latextable():
    
    files = ['20101015MODELBNB70', '20101015MODELBNB80', '20101015MODELBNB85', '20101015MODELBNB90', '20101015MODELBNBNOINIT60', '20101015MODELBNBNOINIT70', '20101015MODELBNBNOINIT80']
    for file in files:
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.time', 'solution.gap'], 
                series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )
        print "\n\n\\clearpage\n\n"
        
def model_new_bnb_latextable():
    
    LatexProcessor('20101015MODELBNBNOINIT60').process(
            ids=[metrics.FileName()],
            datas=['solution.time'], 
            series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
            datafilter= None,
            runfilter= None,
            aggr= aggregate.avg
            )
        
    
def branch_static_manual_latextable():    
    file = '20101215BRANCHSTATIC3'
    
    LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['solution.gap', 'solution.gapfound', 'solution.nnodes'], 
            series=['branch.prios.enabled', 'branch.prios.psadjacent', 'branch.prios.colorindex'],
            datafilter= None,
            runfilter= None,
            aggr= aggregate.avg
            )
    
def bnc_primal_freq_latextable():    
    file = '20110131BNCPRIMALFREQ'
    
    LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['solution.gap', 'solution.time', 'solution.nnodes'], 
            series=['primal.enabled', 'primal.everynodes', 'primal.onlyonup', 'primal.runoncutcallback', 'cuts.onlyonup'],
            datafilter= None,
            runfilter= None,
            aggr= aggregate.avg
            )

def primal_static_3_ub():
     file = '20110219PRIMALSTATIC3'
     LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['coloring.initial.chi',metrics.UbImprovement(), 'solution.gap'], 
            series=['primal.enabled', 'solver.useCplexPrimalHeuristic', 'primal.dsatur.coloring', 'coloring.primal.maxTime'],
            datafilter= {'primal.dsatur.coloring': 'DSaturEasyNode', 'coloring.primal.maxTime': '200'},
            runfilter= None,
            aggr= aggregate.concat
            )
     
def primal_dsaturbranch_ub():
     file = '20110313DSATURBRANCH'
     LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['solution.gap', 'solution.time', 'solution.nnodes', 'solution.primalheur.time', 'solution.primalheur.success.count', 'solution.primalheur.unsuccess.count' ], 
            series=['primal.enabled', 'solver.useCplexPrimalHeuristic', 'primal.dsatur.coloring', 'coloring.primal.maxTime'],
            datafilter= None,
            runfilter= None,
            aggr= aggregate.avg
            )
     
def bnc_pruning():
     #files = ['20110228PRUNINGPRIMALNOUBP1', '20110228PRUNINGPRIMALNOUB2', '20110228PRUNINGP1', '20110228PRUNING2' ] 
     files = ['20110308PRUNINGREMAINING'] 
     for file in files:
         print "\n\n"
         LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.gap', 'solution.time', 'solution.nnodes', 'solution.leafheur.count','solution.primalheur.success.count', 'solution.primalheur.unsuccess.count' ], 
                series=['pruning.enabled', 'pruning.frac', 'pruning.useub', 'primal.useub'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )

def primal_dsatur_branch_runs():
    file = '20110313PRIMALDSATURBRANCH'
    LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['coloring.initial.chi',metrics.UbImprovement(), 'solution.gap'], 
            series=['primal.enabled', 'solver.useCplexPrimalHeuristic', 'primal.dsatur.coloring', 'coloring.primal.maxTime', 'coloring.primal.maxSolutions'],
            datafilter= {'primal.dsatur.coloring': 'DSaturEasyNode'},
            runfilter= None,
            aggr= aggregate.concat
            )

         
def cuts_iters_runs():
    files = ['20110412LOWDENSCUTS','20110414HIGHDENSCUTSS3','20110414HIGHDENSCUTSS5']
    
    for file in files:
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.chi', 'solution.gap', 'solution.time'], 
                series=['cuts.iterations.root.max', 'cuts.iterations.nodes.max', 'cuts.local','cuts.maxdepth'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.avg
                )
                   

def model_bnc_highdens_runs():
    file = '20110313BNCMODELHIGH'
    file = '20110414BNCHIGHDENSEXT'
    
    LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['solution.chi', 'solution.gap', 'solution.time'], 
            series=['strategy.colorBound','strategy.adjacency','strategy.symmetry'],
            datafilter= None,
            runfilter= None,
            aggr= aggregate.concat
            )   
    
def final_bnc_runs():
    files = ['20110419LOWDENSFINAL', '20110419HIGHDENSFINAL']
    fileshk = ['20110427LOWDENSFINALHK','20110427HIGHDENSFINALHK']
    
    for file in fileshk:
        LatexProcessor(file).process(
                ids=[metrics.FileName()],
                datas=['solution.chi', 'solution.lb'], 
                series=['strategy.colorBound','strategy.adjacency','strategy.symmetry'],
                datafilter= None,
                runfilter= None,
                aggr= aggregate.concat
                )        
    
def model_bnc_lowdens_runs():
    file = '20110313BNCMODELLOW'
    file = '20110410BNCMODELLOWEXT'
    LatexProcessor(file).process(
            ids=[metrics.FileName()],
            datas=['solution.gap','solution.time','solution.nnodes'], 
            series=['branch.dynamic.dsatur', 'primal.enabled','solver.probing','strategy.colorBound','strategy.partition','strategy.symmetry'],
            datafilter= None,
            runfilter= None,
            aggr= aggregate.avg
            )    

                        
def test_simple():
    SimpleProcessor('20100629MODELS').process(
        datas=[metrics.FileName(), 'data.filename', 'solution.gap','cuts.niters','solution.time'], 
        series=['strategy.partition', 'strategy.adjacency', 'strategy.symmetry', 'strategy.colorBound', 'strategy.objective'],
    )
    
if __name__ == '__main__':
    dimacs_data_latextable()
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])

