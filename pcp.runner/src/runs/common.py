def update_copy(d1, d2):
    d = d1.copy()
    d.update(d2)
    return d

def create_runs(shared, runs):
    return [update_copy(shared, run) for run in runs]