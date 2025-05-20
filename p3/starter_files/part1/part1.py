import binascii
import sys
import argparse
import glob

def todo(c1binary, c2binary, offset, guessbinary):
    '''
    this function should show you if your guess is correct

    :c1binary:      plaintext1 XOR key
    :c2binary:      plaintext2 XOR key
    :offset:        where you want your guess to start
    :guessbinary:   your guess of plaintext, which becomes ciphertext

    [return]        output, a bytearray or a bytes object
    '''

    print("[incoming ciphertext 1 in binary] \u2193")
    print(c1binary, "\n")
    print("[incoming ciphertext 2 in binary] \u2193")
    print(c2binary, "\n")
    print("[incoming offset]", offset, "\n")
    print("[incoming guess in binary] \u2193")
    print(guessbinary, "\n")

    print('todo: return output showing if guess is correct\n')

    #c1 XOR c2 = m1 XOR m2

    size = len(c1binary)
    guess = bytearray(size)

    for i in range(len(guessbinary)):
        if offset + i < size:
            guess[offset + i] = guessbinary[i]

    output = bytearray(len(c1binary))

    for i in range(size):
        output[i] = c1binary[i] ^ c2binary[i] ^ guess[i]

    return output


def helper(f1,f2,guess,offset):
    ''' do not modify '''
    ctext1 = f1.readlines()[0]
    ctext2 = f2.readlines()[0]

    c1binary = binascii.unhexlify(ctext1)
    c2binary = binascii.unhexlify(ctext2)
    assert(len(c1binary) == len(c2binary))

    # convert guess to an ascii binaryary string. I.e. a byte array
    guessbinary = guess.encode('ascii')

    # your todo
    output = todo(c1binary, c2binary, offset, guessbinary)
    print("[YOUR ANSWER]", output)

if __name__ == '__main__':
    ''' do not modify '''

    # "It is the fundamental emotion that stands at the cradle of true art and true science"

    parser = argparse.ArgumentParser()
    parser.add_argument('-default', '-d', nargs='?', help = 'runs program with defaults supplied (simple setup)', choices=['yes', 'no'])
    parser.add_argument('-ciphertext1', '-c1', nargs='?', help = 'path to file for ciphertext1')
    parser.add_argument('-ciphertext2','-c2', nargs='?', help = 'path to file for ciphertext2')
    parser.add_argument('-guess', '-g', nargs='?', help = 'guess')
    parser.add_argument('-offset', '-o', nargs='?', help = 'where guess starts',type=int)
    args = parser.parse_args()

    if (args.ciphertext1==None and args.ciphertext2==None and args.default==None and args.guess==None and args.offset==None):
        print(" -->for help:\n\tpython3 part1.py -h")
        print(" -->run me like this:\n\t", "python3 part1.py -default yes\n", "-->or like this:\n\t", "python3 part1.py -c1 '<path>' -c2 '<path>' -g 'hi there' -o 0")
        sys.exit()

    # default options for simple testing
    first = ''
    second = ''
    if (args.default == 'yes'):
        # ciphertext 1 = first ciphertext
        # ciphertext 2 = second ciphertext
        # guess = '!'
        # offset = 0
        for f in glob.glob('ciphers/**/*.txt', recursive=True):
            if "_1.txt" in f:
                first = f
                print("[+] using as first cipher-->", first)
            if "_2.txt" in f:
                second = f
                print("[+] using as second cipher-->", second)
        if (first == '' or second == ''):
            print("! error, please see spec for where to place your personal cipher file !")
            sys.exit()
        print("\n")
        args.guess = '!'
        args.offset = 0
        with open(first) as f1:
            with open(second) as f2:
                helper(f1,f2,args.guess,args.offset)

    # more specific input
    else:
        with open(args.ciphertext1) as f1:
            with open(args.ciphertext2) as f2:
                print("[+] using as first cipher-->", f1)
                print("[+] using as first cipher-->", f2)
                helper(f1,f2,args.guess,args.offset)
