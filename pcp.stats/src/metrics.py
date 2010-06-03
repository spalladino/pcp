def format_output(str):
    if not str: 
        return '-'
    try:
        f = float(str) 
        return "{0:.3f}".format(f) if f > 5 else str
    except: 
        return str

class CutMetric:
    
    def __init__(self, cut, prop):
        self.cut = cut
        self.prop = prop
    
    def __call__(self, dict):
        try: return format_output(dict["cuts"][self.cut][self.prop])
        except: return '-'
    
    def __str__(self):
        return "%s %s" % (self.cut, self.prop)

class PlainMetric:
    
    def __init__(self, prop):
        self.prop = prop
    
    def __call__(self, dict):
        return format_output(dict.get(self.prop)) or '-'
    
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