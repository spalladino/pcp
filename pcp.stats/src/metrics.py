import re

def format_output(str):
    if not str: 
        return '-'
    try:
        f = float(str) 
        return "{0:.3f}".format(f) if int(f) != f else str
    except: 
        return str

class UbImprovement:
    def __call__(self, dict):
        return - int(dict["solution.chi"]) + int(dict["coloring.initial.chi"])
        
    def __str__(self):
        return "ub.improvement"

    def type(self):
        return "real"


class FileName:
    def __init__(self):
        pass
    
    def __call__(self, dict):
        return self.format_filename(dict["run.filename"].split(".")[0])
        
    def __str__(self):
        return "filename"

    def type(self):
        return "string"
    
    def format_filename(self, fname):
        dens = re.match('benchdens\\\\e([\\d]+)n([\\d]+)', fname)
        if dens: return "EW {0} N={1}".format(int(dens.group(1)) * 10, int(dens.group(2)))
        
        nodes = re.match('benchnodes\\\\n([\\d]+)', fname)
        if nodes: return "EW 50 N={0}".format(int(nodes.group(1)))

        parts = re.match('benchpart\\\\p([\\d]+)t([\\d]+)e([\\d]+)n([\\d]+)', fname)
        if parts: return "EW {0} N={1} P=({2}..{3})".format(int(parts.group(3))*10, int(parts.group(4)), int(parts.group(1)), int(parts.group(2)))
        
        holme = re.match('holme\\\\n([\\d]+)d([\\d]+)', fname)
        if holme: return "HK {0} N={1}".format(int(holme.group(2)) * 10, int(holme.group(1)))
        
        return fname

class PreprocessRemoved:
    
    def __init__(self, item):
        self.item = item
    
    def __call__(self, dict):
        before = float(dict['original.graph.' + self.item])
        after = float(dict['graph.' + self.item])
        val = (before-after)/before
        return "{0:.3f}".format(val)
    
    def __str__(self):
        return self.item + " removed"

    def type(self):
        return "float"


class BoundFound:
    
    def __init__(self):
        pass
    
    def __call__(self, dict):
        list = eval(dict['root.gaps'].replace('null', 'None'))
        val,pos = 0,0
        for i,x in enumerate(list):
            if x and x != val:
                val,pos = x,i
        return "{0}/{1}".format(pos, dict['cuts.niters'])
    
    def __str__(self):
        return "iter"

    def type(self):
        return "string"

class CutMetric:
    
    def __init__(self, cut, prop='count'):
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

class Ks:
    
    def __init__(self, prop):
        self.prop = prop
    
    def __call__(self, dict):
        return int(float(dict.get(self.prop)) / 1000)
    
    def __str__(self):
        return str(self.prop) + ' Ks'
    
    def type(self):
        return "real"
    
class FloatMetric:
    
    def __init__(self, prop, format="{0:.2f}"):
        self.prop = prop
        self.format = format
    
    def __call__(self, dict):
        return self.format_output(dict.get(self.prop)) or '-'
    
    def __str__(self):
        return self.prop
    
    def type(self):
        return "real"
    
    def format_output(self, str):
        if not str: 
            return '-'
        try:
            f = float(str) 
            return self.format.format(f)
        except: 
            return str

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