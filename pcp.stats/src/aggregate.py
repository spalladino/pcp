class Avg:
    def __init__(self, minval=0.0):
        self.minval = minval
        
    def __call__(self, list):
        try:
            if len(list) == 0: return 0.0
            return "{0:.2f}".format(float(sum(filter(self.__gtminval,map(tryfloat,list)))) / float(len(list)))
        except:
            "ERR"
            
    def __gtminval(self, val):
        return val >= self.minval

def concat(list):
    if len(list) > 0: return str(list)
    else: return str(list[0])
   
def first(list):
    return str(list[0])
    
def avg(list):
    try:
        if len(list) == 0: return 0.0
        return "{0:.2f}".format(float(sum(map(tryfloat,list))) / float(len(list)))
    except:
        "ERR"
        
def avgstd(list):
    try:
        if len(list) == 0: return 0.0
        avg = float(sum(map(tryfloat,list))) / float(len(list))
        std = (float(sum([(tryfloat(x)-avg)**2 for x in list])) / float(len(list)))**0.5
        return "{0:.2f} ({1:.2f})".format(avg, std)
    except:
        "ERR"

def tryfloat(s):
    try: return float(s)
    except: return 0.0