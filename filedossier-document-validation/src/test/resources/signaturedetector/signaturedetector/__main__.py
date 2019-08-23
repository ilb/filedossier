import os
import sys
import select
import json

import document_signature_detector as ds


def main():
    if os.name == 'nt':
        #fp = open("../examples/request.json")
        #j_doc = json.load(fp)
        #fp.close()
        j_doc = json.load(sys.stdin)
    else:
        if select.select([sys.stdin], [], [], 0.0)[0]:
            j_doc = json.load(sys.stdin)
        else:
            raise Exception('Pipe is empty.')

    detector = ds.DocumentSignatureDetector(j_doc)
    print(json.dumps(detector.find_signatures()))


if __name__ == '__main__':
    main()
