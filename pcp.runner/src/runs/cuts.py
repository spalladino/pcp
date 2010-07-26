import fetcher

from config import datadir
from common import create_runs
from common import update_copy


def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in') 
    

baseprops = {
             'cuts.enabled': 'true',
             'cuts.iterations.root.max': '500',
             'solver.maxTime': '900',
             'solver.kind': 'PcpCuttingPlanes',
             'branch.selection': '1',
             'branch.direction': '0',
             'branch.prios.enabled': 'true',
             'branch.prios.psize': '0',
             'branch.prios.psadjacent': '10',
             'branch.prios.colorindex': '-1',
             'branch.dynamic.dsatur.nodelb': '0.7',
             'branch.dynamic.fractional': 'false',
             'branch.dynamic.dsatur': 'true',
             'branch.singlevar': 'false',
             'branch.boundws': 'true',
             'solver.useCutCallback': 'true',
             'solver.useHeuristicCallback': 'true',
             'solver.useBranchingCallback': 'true',
             'pruning.enabled': 'false',
             'primal.enabled': 'true',
             'primal.onlyonup': 'true',
             'branch.enabled': 'true',
             
            'clique.colorsAsc': 'false',
            'clique.backtrackBrokenIneqs': 'false',
            'clique.backtrackLastCandidate': 'false',
            'blockColor.usePool': 'false',
            'cuts.iset.usePathsAlgorithm': 'true',
            'cuts.iset.useBreakingSymmetry': 'false'
                          
              }

cliqueruns = create_runs(update_copy(baseprops, {
                 'clique.enabled': 'true',
                 'path.enabled': 'false',
                 'gprime.path.enabled': 'false',
                 'blockColor.enabled': 'false',
                 }), [{
                    'clique.colorsAsc': 'false',
                    'clique.backtrackBrokenIneqs': 'false',
                    'clique.backtrackLastCandidate': 'true'
                  },{
                    'clique.colorsAsc': 'false',
                    'clique.backtrackBrokenIneqs': 'false',
                    'clique.backtrackLastCandidate': 'false'
                  },{
                    'clique.colorsAsc': 'false',
                    'clique.backtrackBrokenIneqs': 'true',
                    'clique.backtrackLastCandidate': 'false'
                  },{
                    'clique.colorsAsc': 'false',
                    'clique.backtrackBrokenIneqs': 'true',
                    'clique.backtrackLastCandidate': 'true'
                  },{
                    'clique.colorsAsc': 'true',
                    'clique.backtrackBrokenIneqs': 'false',
                    'clique.backtrackLastCandidate': 'true'
                  },{
                    'clique.colorsAsc': 'true',
                    'clique.backtrackBrokenIneqs': 'false',
                    'clique.backtrackLastCandidate': 'false'
                  },{
                    'clique.colorsAsc': 'true',
                    'clique.backtrackBrokenIneqs': 'true',
                    'clique.backtrackLastCandidate': 'false'
                  },{
                    'clique.colorsAsc': 'true',
                    'clique.backtrackBrokenIneqs': 'true',
                    'clique.backtrackLastCandidate': 'true'
                  },] )

familiesruns = create_runs(baseprops, [
                       {
                         'clique.enabled': 'true',
                         'path.enabled': 'true',
                         'gprime.path.enabled': 'true',
                         'blockColor.enabled': 'true',
                         'solver.useCplexCuttingPlanes': 'false',
                       }, 
                       {
                         'clique.enabled': 'true',
                         'path.enabled': 'true',
                         'gprime.path.enabled': 'true',
                         'blockColor.enabled': 'true',
                         'solver.useCplexCuttingPlanes': 'true',
                       }, 
                       {
                         'clique.enabled': 'false',
                         'path.enabled': 'false',
                         'gprime.path.enabled': 'false',
                         'blockColor.enabled': 'false',
                         'solver.useCplexCuttingPlanes': 'true',
                       }, 
                       {
                         'clique.enabled': 'true',
                         'path.enabled': 'false',
                         'gprime.path.enabled': 'false',
                         'blockColor.enabled': 'true',
                         'solver.useCplexCuttingPlanes': 'false',
                       }, 
                       {
                         'clique.enabled': 'true',
                         'path.enabled': 'false',
                         'gprime.path.enabled': 'false',
                         'blockColor.enabled': 'true',
                         'solver.useCplexCuttingPlanes': 'true',
                       }, 
                       {
                         'clique.enabled': 'true',
                         'path.enabled': 'true',
                         'gprime.path.enabled': 'false',
                         'blockColor.enabled': 'true',
                         'solver.useCplexCuttingPlanes': 'false',
                       }, 
                       {
                         'clique.enabled': 'true',
                         'path.enabled': 'true',
                         'gprime.path.enabled': 'false',
                         'blockColor.enabled': 'true',
                         'solver.useCplexCuttingPlanes': 'true',
                       },
                       ])


blockcolorruns = create_runs(update_copy(baseprops, {
                 'clique.enabled': 'false',
                 'path.enabled': 'false',
                 'gprime.path.enabled': 'false',
                 'blockColor.enabled': 'true',
                 }), [{
                        'blockColor.usePool': 'false'
                       }, 
                       {
                        'blockColor.usePool': 'true'
                        }])

greekruns = create_runs(update_copy(baseprops, {
                 'clique.enabled': 'false',
                 'path.enabled': 'true',
                 'holes.enabled': 'true',
                 'gprime.path.enabled': 'false',
                 'blockColor.enabled': 'false',
                 }), [{
                        'cuts.iset.usePathsAlgorithm': 'true',
                        'cuts.iset.useBreakingSymmetry': 'false'
                       }, 
                      {
                        'cuts.iset.usePathsAlgorithm': 'true',
                        'cuts.iset.useBreakingSymmetry': 'true'
                       },
                       {
                        'cuts.iset.usePathsAlgorithm': 'false',
                        'cuts.iset.useBreakingSymmetry': 'false'
                       }, 
                       {
                        'cuts.iset.usePathsAlgorithm': 'false',
                        'cuts.iset.useBreakingSymmetry': 'true'
                       },])