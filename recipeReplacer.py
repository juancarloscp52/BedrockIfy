#!/usr/bin/python3

""" Recipe Replacer for Minecraft 1.20.5
Created at Apr 30, 2024 by lonefelidae16 <kow161206@gmail.com>

Usage:
> recipeReplacer.py [--help | -h] [--test | -t] /path/to/target/recipe.json

"""

import json
import sys
import argparse

parser = argparse.ArgumentParser(
         prog='RecipeReplacer',
         description='Make recipe.json compatible with MC1.20.5')
parser.add_argument('filename')
parser.add_argument('-t', '--test', action='store_true', help='show result JSON to STDOUT, no changes to the specified file')
args = parser.parse_args()

if __name__ == '__main__':

    struct = {}
    replaced = False
    with open(args.filename, 'r') as target_file:
        struct = json.load(target_file)
        if not 'result' in struct:
            exit()
        if 'item' in struct['result']:
            struct['result']['id'] = struct['result']['item']
            del struct['result']['item']
            replaced = true
        elif type(struct['result']) is str:
            _result = struct['result']
            struct['result'] = {}
            struct['result']['id'] = _result
            replaced = true

    if replaced:
        if args.test:
            print(json.dumps(struct, indent=2))
        else:
            with open(args.filename, 'w') as target_file:
                json.dump(struct, target_file, indent=2)
    else:
        print(args.filename + ": no changes made")
