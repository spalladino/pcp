import fetcher

from config import datadir
from common import create_runs


def files():
    #return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n90\\.00(0|1|2)\\.in')
    return fetcher.Fetcher(datadir).fetch_files('benchdens', '((e06n80)|(e02n90)|(e0(4|8)n100))\\.00(0|1|2)\\.in')

primalruns = create_runs({
                 'solver.kind': 'PcpBranchAndCut',
                 
                 'solver.useCutCallback': 'true',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'true',
                 'branch.enabled': 'true',
                 'cuts.enabled': 'false',
                  
                 'solver.useCplexPrimalHeuristic': 'false',
                 'solver.maxTime': '900',
                 
                 'branch.selection': '1',
                 'branch.direction': '0',

                 'branch.prios.enabled': 'true',
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                 'branch.dynamic.dsatur.nodelb': '0.7',
                 'branch.dynamic.fractional': 'false',
                 'branch.dynamic.dsatur': 'true',
                 'branch.dynamic.dsatur.conseccolors': 'true'
                 
               }, [

                {
                 'branch.dynamic.dsatur': 'false',
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.onlyonup': 'true'
                },
                {
                 'branch.dynamic.dsatur': 'true',
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.onlyonup': 'true'
                },
                {
                 'branch.dynamic.dsatur': 'false',
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'primal.onlyonup': 'true'
                },
                {
                 'branch.dynamic.dsatur': 'true',
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'primal.onlyonup': 'true'
                },
                {
                 'branch.dynamic.dsatur': 'true',
                 'primal.enabled': 'false',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'primal.onlyonup': 'true'
                },
                {
                 'branch.dynamic.dsatur': 'false',
                 'primal.enabled': 'false',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'primal.onlyonup': 'true'
                },
                
                ])


primalstaticruns = create_runs({
                 'solver.kind': 'PcpBranchAndCut',
                 
                 'solver.useCutCallback': 'true',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'true',
                 'branch.enabled': 'true',
                 'cuts.enabled': 'false',
                  
                 'solver.maxTime': '900',
                 
                 'branch.selection': '1',
                 'branch.direction': '0',

                 'branch.prios.enabled': 'true',
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                 'branch.dynamic.dsatur.nodelb': '0.7',
                 'branch.dynamic.dsatur.conseccolors': 'true',
                 
                 'branch.dynamic.fractional': 'false',
                 'branch.dynamic.dsatur': 'false',
                 'primal.onlyonup': 'false',
                 'primal.everynodes': '1',
               }, [

                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                }, 
                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'true',
                },
                {
                 'primal.enabled': 'false',
                 'solver.useCplexPrimalHeuristic': 'true',
                },
                ])


primalsettingsruns = create_runs({
                 'solver.kind': 'PcpBranchAndCut',
                 
                 'solver.useCutCallback': 'true',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'true',
                 'branch.enabled': 'true',
                 'cuts.enabled': 'false',
                  
                 'solver.useCplexPrimalHeuristic': 'false',
                 'solver.maxTime': '900',
                 
                 'branch.selection': '1',
                 'branch.direction': '0',

                 'branch.prios.enabled': 'true',
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                 'branch.dynamic.dsatur.nodelb': '0.7',
                 'branch.dynamic.fractional': 'false',
                 'branch.dynamic.dsatur': 'true',
                 'branch.dynamic.dsatur.conseccolors': 'true'
                 
               }, [

                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.onlyonup': 'true',
                 'branch.direction': '0',
                },
                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.onlyonup': 'true',
                 'branch.direction': '1',
                },
                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.everynodes': '1',
                 'primal.onlyonup': 'false'
                },
                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.everynodes': '2',
                 'primal.onlyonup': 'false'
                },
                {
                 'primal.enabled': 'true',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'primal.everynodes': '4',
                 'primal.onlyonup': 'false'
                },
                ])