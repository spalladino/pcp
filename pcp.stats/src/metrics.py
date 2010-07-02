def format_output(str):
    if not str: 
        return '-'
    try:
        f = float(str) 
        return "{0:.2f}".format(f) if int(f) != f else str
    except: 
        return str

class FileName:
    def __init__(self):
        pass
    
    def __call__(self, dict):
        try: return dict["run.filename"].split(".")[0]
        except: return 'FILENAME_ERROR'
    
    def __str__(self):
        return "filename"
    
    def type(self):
        return "string"

class CutMetric:
    
    def __init__(self, cut, prop):
        self.cut = cut
        self.prop = prop
    
    def __call__(self, dict):
        try: return format_output(dict["cuts"][self.cut][self.prop])
        except: return '-'
    
    def __str__(self):
        return "%s %s" % (self.cut, self.prop)

    def type(self):
        return "string"

class PlainMetric:
    
    def __init__(self, prop):
        self.prop = prop
    
    def __call__(self, dict):
        return format_output(dict.get(self.prop)) or '-'
    
    def __str__(self):
        return self.prop
    
    def type(self):
        return "string"
    
class GapMetric:
    
    def __init__(self):
        pass
    
    def __call__(self, dict):
        return format_output( float(dict.get('solution.lb')) / float(dict.get('coloring.initial.chi')) ) or '-'
    
    def __str__(self):
        return 'gap.computed'
    
    def type(self):
        return "real"

class TypedMetric:
    def __init__(self, prop):
        self.prop, self.type = self.__nametype(prop)
    
    def __nametype(self, name):
        types = ["string", "int", "real"]
        sname = str(name)
        for type in types:
            if sname.startswith(type + ":"):
                return (sname.strip(type + ":"), type)
        return (sname, "string")
    
    def __call__(self, dict):
        return format_output(dict.get(self.prop)) or '-'
    
    def __str__(self):
        return self.prop
    
    def type(self):
        return self.type
    
def evalmetric(m, run):
    return metric(m)(run)

def metric(metric):
    return metric if callable(metric) else TypedMetric(metric)

def cutcount(cut):
    return CutMetric(cut, "count")        

def cuttime(cut):
    return CutMetric(cut, "ticks")