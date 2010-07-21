import processor
import metrics
import aggregate

from latexprocessor import LatexProcessor

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

def dsatur_latextable(p):    
        p.process(
                ids=[metrics.FileName()],
                datas=['solution.chi','solution.found'], 
                series=['strategy.coloring', 'dsatur.partition.weight.size', 'dsatur.partition.weight.colorCount', 'dsatur.partition.weight.uncolored'],
                datafilter= {'dsatur.partition.weight.size':'1', 'dsatur.partition.weight.colorCount':'10000', 'dsatur.partition.weight.uncolored':'100'},
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

if __name__ == '__main__':
    branch_dyn_latextable()
    #p.graphprops("graph.nodes", "solution.time", "nodes-time.png")
    #p.graphprops("graph.edges", "solution.time", "edges-time.png")
    #p.graphprops("graph.partitions", "solution.time", "parts-time.png")
    #p.graph("graph.nodes", "solution.time", series= ["holes.maxPerColor"], fname= "nodestime.png")
    #p.graph("graph.nodes", metrics.cutcount("Holes"), series= ["holes.maxPerColor"])

