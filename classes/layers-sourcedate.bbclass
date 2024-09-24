#
# Calculates the newest commit date of all layers as unix timestamp
#
# Copyright (C) 2024 BISDN GmbH
# Author: Jonas Gorski <jonas.gorski@bisdn.de>
#
# Licensed under the MIT license, see COPYING.MIT for details
#
# Usage: add INHERIT += "layers-sourcedate" to your conf file

def get_layer_sourcedate_epoch(sourcedir):
    import subprocess

    # layers may not be in the root of the git reporsitory, so first get the
    # toplevel of the layer's git repostory
    p = subprocess.run(['git', 'rev-parse', '--show-toplevel'], cwd=sourcedir, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    if p.returncode != 0:
        bb.debug(1, "%s is not a valid git repository: %s" % (gitpath, p.stdout.decode('utf-8')))
        return int(0)

    topdir = p.stdout.decode('utf-8').rstrip()

    # now check for a .git
    gitpath = os.path.join(topdir, ".git")
    if not os.path.isdir(gitpath):
        return int(0)

    p = subprocess.run(['git', '--git-dir', gitpath, 'rev-parse', 'HEAD'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    if p.returncode != 0:
        bb.debug(1, "%s does not have a valid HEAD: %s" % (gitpath, p.stdout.decode('utf-8')))
        return int(0)

    p = subprocess.run(['git', '-c', 'log.showSignature=false', '--git-dir', gitpath, 'log', '-1', '--pretty=%ct'],
                       check=True, stdout=subprocess.PIPE)
    return int(p.stdout.decode('utf-8'))

def get_layers_sourcedate_epoch(d):
    layers = (d.getVar("BBLAYERS") or "").split()
    sourcedates = [get_layer_sourcedate_epoch(i) for i in layers]

    return max(sourcedates)
