import fetcher

from config import datadir

def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchnodes', 'n((60)|(80)|(100)|(120)|(140)|(160)|(180)|(200))\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchpart', 'p[\\d]t[\\d]e05n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n140d0(1|2|3|4)\\.00[\\d]\\.in')


dsaturruns = [
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '1',
                'dsatur.partition.weight.colorCount': '10000',
                'dsatur.partition.weight.uncolored': '100'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '10000',
                'dsatur.partition.weight.colorCount': '1',
                'dsatur.partition.weight.uncolored': '100'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '1',
                'dsatur.partition.weight.colorCount': '100',
                'dsatur.partition.weight.uncolored': '10000'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '10000',
                'dsatur.partition.weight.colorCount': '100',
                'dsatur.partition.weight.uncolored': '1'
              },
                            {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '100',
                'dsatur.partition.weight.colorCount': '1',
                'dsatur.partition.weight.uncolored': '10000'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '100',
                'dsatur.partition.weight.colorCount': '10000',
                'dsatur.partition.weight.uncolored': '1'
              },
              {
                'strategy.coloring': 'DSaturEasyNodeRandomized'
              },
              {
                'strategy.coloring': 'DSaturEasyNode'
              },
             ]