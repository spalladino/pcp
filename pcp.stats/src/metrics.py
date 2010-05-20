class CutMetric:
    
    def __init__(self, cut, prop):
        self.cut = cut
        self.prop = prop
    
    def __call__(self, dict):
        return dict["cuts"][self.cut][self.prop]
    
    def __str__(self):
        return "%s %s" % (self.cut, self.prop)

class PlainMetric:
    
    def __init__(self, prop):
        self.prop = prop
    
    def __call__(self, dict):
        return dict.get(self.prop) or 'None'
    
    def __str__(self):
        return self.prop

def evalmetric(m, run):
    return metric(m)(run)

def metric(metric):
    return metric if callable(metric) else PlainMetric(metric)

def cutcount(cut):
    return CutMetric(cut, "count")        

def cuttime(cut):
    return CutMetric(cut, "ticks")