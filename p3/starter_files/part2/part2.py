import sys
if sys.version_info[0] < 3:
    raise Exception("\n\n\nThis program must be ran with Python 3. Try running with python3\n\n\n")

from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import ec
from cryptography.hazmat.primitives.kdf.hkdf import HKDF
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives._serialization import NoEncryption
from collections import namedtuple
from pathlib import Path
import argparse
import pickle
import json

Deposit = namedtuple('Deposit', ['source', 'dest','amount'])
CURVE = ec.SECP384R1()

# --------------------------------- HELPERS -----------------------------------
def pretty_print(string):

    if len(string) == 0:
        print('\t', "----------------------------------------------------------------------")
        print('\t', "empty!")
        print('\t', "----------------------------------------------------------------------")

    else:
        print('\t', "----------------------------------------------------------------------")
        n = 22
        all = [(string[i:i+n]) for i in range(0, len(string), n)]
        for item in all:
            print('\t', item)
        print("\t ==> length: " + str(len(string)))
        print('\t', "----------------------------------------------------------------------")
    return

def KeygenEnc():
    sk = ec.generate_private_key(CURVE)
    pk = sk.public_key() # PK= g^sk
    return (pk,sk)

def PKEnc(pk, m):
    #elgamal . i.e. turning a key exchange into an encryption schem
    r = ec.generate_private_key(CURVE)
    R = r.public_key()
    k = r.exchange(ec.ECDH(), pk) # the shared secret. (g^sk)^r
    key_stream = HKDF(
        algorithm=hashes.SHA256(),
        length=len(m),
        salt=None,
        info=b'fooo',
        ).derive(k)
    C = bytes([x ^ y for (x, y) in zip(m, key_stream)])
    #serialize
    Rbin=R.public_bytes(encoding=serialization.Encoding.X962,format=serialization.PublicFormat.CompressedPoint)
    ciphertext = (Rbin,C)
    ct = pickle.dumps(ciphertext)
    return ct

def PKDec(sk, ctbin):
    ct = pickle.loads(ctbin)
    Rbin,C = ct
    R= ec.EllipticCurvePublicKey.from_encoded_point(CURVE, Rbin)
    k = sk.exchange(ec.ECDH(), R)
    key_stream = derived_key = HKDF(
        algorithm=hashes.SHA256(),
        length=len(C),
        salt=None,
        info=b'fooo',
        ).derive(k)
    return  bytes([x ^ y for (x, y) in zip(C, key_stream)])

def KeygenSig():
    sk = ec.generate_private_key(CURVE)
    pk = sk.public_key() # PK= g^sk
    return (pk,sk)

def Sign(sk,m):
    return sk.sign(m,ec.ECDSA(hashes.SHA256()))

def VerifySig(pk,m,sig):
    try:
        pk.verify(sig,m,ec.ECDSA(hashes.SHA256()))
        return True
    except:
        return False

def serializePK(pk):
    return pk.public_bytes(encoding=serialization.Encoding.X962,format=serialization.PublicFormat.CompressedPoint)

def deserializePK(pkBin):
    return ec.EllipticCurvePublicKey.from_encoded_point(CURVE,pkBin)

def serializeSK(sk):
    return sk.private_bytes(encoding=serialization.Encoding.DER,format=serialization.PrivateFormat.PKCS8,encryption_algorithm=NoEncryption())

def deserializeSK(skBin):
    return serialization.load_der_private_key(skBin,password=None)

def keyGenTofileEnc(filename):
    pk,sk = KeygenEnc()
    pkbin = serializePK(pk)
    skbin = serializeSK(sk)
    with open(filename+".enc_pk","wb") as f:
        pickle.dump(pkbin,f)
    with open(filename+".enc_sk","wb") as f:
        pickle.dump(skbin,f)

def keyGenTofileSig(filename):
    pk,sk = KeygenSig()
    pkbin = serializePK(pk)
    skbin = serializeSK(sk)
    with open(filename+".sig_pk","wb") as f:
        pickle.dump(pkbin,f)
    with open(filename+".sig_sk","wb") as f:
        pickle.dump(skbin,f)

'''
                # -------- YOUR CODE BELOW HERE --------  #
'''

###############################################################################
# Part II, Task 3
###############################################################################

def clientSendDeposit(deposit, bankPK):
    print("[client send deposit] - deposit plaintext:", deposit)
    print("[client send deposit] - bank public key:", bankPK)

    # serialized deposit being made (as an object)
    m=pickle.dumps(deposit)
    print("[client send deposit] - serialized deposit \u2193")
    pretty_print(m)

    # todo encrypt the serialized deposit 'm'
    # hint: use existing functions
    encrypted = PKEnc(bankPK, m)

    print("[client send deposit] - encrypted deposit \u2193")
    pretty_print(encrypted)
    return encrypted

def bankReceiveDeposit(ciphertext, bankSK):
    print("[bank receiving deposit] - deposit ciphertext \u2193")
    pretty_print(ciphertext)
    print("[bank receiving deposit] - bank secret key:", bankSK)

    # todo decrypt the ciphertext deposit 'ciphertext'
    # hint: use existing functions
    decrypted = PKDec(bankSK, ciphertext)

    # deserialize deposit object for printing
    if (len(decrypted) != 0):
        deposit = pickle.loads(decrypted)
        print("[bank receiving deposit] - decrypted deposit (from, to, amount) \u2193 ")
        pretty_print(deposit)

        # to return
        text = pickle.dumps(deposit)
        return text
    else:
        print("-- todo: decrypt the ciphertext --")

###############################################################################
# Part II, Task 4, attack1
###############################################################################

def attackDeposit(ciphertext, bankPK):
    '''
    Although Eve cannot decrypt the ciphertext, Eve may be able to modify
    the ciphertext meaningfully even without knowing the decryption key
    '''
    rbin,ciphertext=pickle.loads(ciphertext)#Hint

    size = len(ciphertext) #57
    print(size)

    BxE = bytes(a ^ b for a, b in zip(b'Bob', b'Eve')) #b'\x07\x19\x07'
    print(BxE)


    guessbin = bytearray(size)

    guessbin[:] = ciphertext

    #offset for where 'bob' is 
    offset = 46

    guessbin[offset] ^= BxE[0]
    guessbin[offset + 1] ^= BxE[1]
    guessbin[offset + 2] ^= BxE[2]
    
    attempt = guessbin
    attack=pickle.dumps((rbin,attempt))#Hint
    return attack

###############################################################################
# Part II, Task 5
###############################################################################
def clientSendSignedDeposit(deposit, clientSiginingSK, bankPK):
    m=pickle.dumps(deposit) # serialize deposit object

    print("[client send signed deposit] - deposit:", deposit)
    print("[client send signed deposit] - bank private key:", bankPK)

    # TO DO: Encrypt
    encrypted = PKEnc(bankPK, m)
    print("[client send signed deposit] - encrypted deposit \u2193")
    pretty_print(encrypted)


    print("sig",clientSiginingSK)
    # TO DO: Add signature to encrypted deposit
    signed = Sign(clientSiginingSK, encrypted)

    print("[client send signed deposit] - encrypted and signed deposit \u2193")
    pretty_print(signed)

    ciphertextSigned = {"Signer":"Charlie","Cipher":encrypted,"Signature":signed}
    return ciphertextSigned

def bankReceiveSignedDeposit(ciphertextSigned, bankSK, accounts):
    print("[bank receiving signed deposit] - who signed it \u2193")
    pretty_print(ciphertextSigned['Signer'])
    print("[" + ciphertextSigned['Signer'] + "'s public key is] \u2193\n", accounts[ciphertextSigned['Signer']])

    print("[bank receiving signed deposit] - signature \u2193")
    pretty_print(ciphertextSigned['Signature'])

    print("[bank receiving signed deposit] - encrypted and signed deposit \u2193")
    pretty_print(ciphertextSigned['Cipher'])

    signature = ciphertextSigned['Signature']
    ciphertext = ciphertextSigned['Cipher']
    signer = ciphertextSigned['Signer']

    # TO DO decrypt the ciphertext
    # hint (you've done this before)
    decrypted = PKDec(bankSK, ciphertext)
    pretty_print(decrypted)

    # TO DO: Verify if signature is still valid, if yes return the deposit if no return None
    # hint (use existing functions)
    pk = bankSK.public_key()
    verification = VerifySig(accounts[signer], ciphertext, signature)

    # if verified, return pickle.dumps(deposit)
    # if not verified, return None
    if (verification):
        deposit = pickle.loads(decrypted)
        print("[bank receiving signed deposit] - decrypted signed deposit (from, to, amount) \u2193 ")
        pretty_print(deposit)

        # to return
        text = pickle.dumps(deposit)
        return text
    else:
        print("[bank receiving signed deposit] - verification FAILED")
        return None

###############################################################################
# Part II, Task 6, attack2
###############################################################################
def attackSignature(ciphertextSigned, clientSignedSK, bankPK):
    '''
    Eve takes the output from clientSendSignedDeposit(), uses a secret
    signature key, strips Charlie's original signature,
    and replaces that signature with Eve's signature
    '''

    print("ctsigned", ciphertextSigned)

    signature = ciphertextSigned['Signature']
    print("sig", signature)
    #print(len(signature))
    print("eve sig", serializeSK(clientSignedSK))

    ciphertext = ciphertextSigned['Cipher']
    #print("cipher", ciphertext)
    signer = ciphertextSigned['Signer']
    #print("sign", signer)

    rbin,ct=pickle.loads(ciphertext)

    attempt = attackDeposit(ciphertext, bankPK)
    #attack=(rbin,attempt) 
    #attack=pickle.dumps(attack)#Hint

    # TO DO update ciphertextSigned
    eveSK = Sign(clientSignedSK, attempt)
    ciphertextSigned['Signer'] = "Eve"
    ciphertextSigned['Signature'] = eveSK
    ciphertextSigned['Cipher'] = attempt

    return ciphertextSigned


# ----------------------- DO NOT MODIFY  ------------------------ #
if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    subparsers =  parser.add_subparsers(dest='command')
    subparsers.required = True
    csd = subparsers.add_parser('clientSendDeposit', help = "run client task 1")
    csdSigned = subparsers.add_parser('clientSendSignedDeposit',help = "run client task2")
    # common arguments for client commands
    for p in [csd,csdSigned]:
        p.add_argument("src",help="deposit source")
        p.add_argument("dest",help="deposit destination")
        p.add_argument("amt",help="deposit amount",type=int)
        p.add_argument("bankPK",help="path/to/bankPK")
        p.add_argument('out', help="name of file to write ciphertext to")
    #signed csd takes an extra argument, so add it
    csdSigned.add_argument("clientSK",help=" path to client secret key for signing")
    brd = subparsers.add_parser("bankReceiveDeposit", help = 'run bank task1')
    brdSigned = subparsers.add_parser("bankReceiveDepositSigned", help = 'run bank task2')
    #common arguments for bank commands
    for p in [brd,brdSigned]:
        p.add_argument("bankSK",help="path/to/bankSK")
        p.add_argument("ciphertext",help = "path/to/ciphertext")
        p.add_argument('out', help='name of file to write output of bank to')
    #again need an extra argument
    brdSigned.add_argument("accounts",help = "path/to/accountFile.json")
    attack1=subparsers.add_parser("attack1",help="run first attack")
    attack2=subparsers.add_parser("attack2",help="run second attack")
    #common arguments for attack commands
    for p in [attack1,attack2]:
        p.add_argument('bankPK', help='path/to/bankPK')
        p.add_argument("ct",help="path/to/ciphertext to attack")
        p.add_argument('out', help="output file name")
    #attack two takes an extra arugment so add it
    attack2.add_argument('sk',help = "path/to/ Eve's secret key for signing")


    keygenSig = subparsers.add_parser("keygenSig", help = 'generate a signature public, private and key')
    keygenEnc = subparsers.add_parser("keygenEnc", help = 'generate a signature public, private and key')

    keygenSig.add_argument('filename', help='will write keys to filename.sig_pk an filename.sig_sk')
    keygenEnc.add_argument('filename', help='will write keys to filename.enc_pk an filename.enc_sk')


    args = parser.parse_args()


    if args.command == "keygenSig":
        keyGenTofileSig(args.filename)
    elif args.command == "keygenEnc":
        keyGenTofileEnc(args.filename)
    elif args.command == "clientSendDeposit":
        deposit = Deposit(args.src, args.dest, args.amt)
        with open(args.bankPK,'rb') as fPK:
            with open(args.out,'wb') as out:
                pkBin= pickle.load(fPK)
                bankPK = deserializePK(pkBin)
                ciphertext = clientSendDeposit(deposit, bankPK)
                pickle.dump(ciphertext,out)
        #FIXME DO something with this, save it somewhere. Maybe need to take a destination as an argument
    elif args.command == 'clientSendSignedDeposit':
        deposit = Deposit(args.src, args.dest, args.amt)
        with open(args.bankPK,'rb') as fPK:
            with open(args.clientSK, 'rb') as clientSK:
                with open(args.out,'wb') as out:
                    pkBin= pickle.load(fPK)
                    bankPK = deserializePK(pkBin)
                    clientSignedSKbin=pickle.load(clientSK)
                    clientSK = deserializeSK(clientSignedSKbin)
                    ciphertext = clientSendSignedDeposit(deposit, clientSK, bankPK)
                    pickle.dump(ciphertext,out)

    elif args.command == 'attack1':
        with open(args.bankPK, 'rb') as bPK:
            with open(args.ct,'rb') as ct:
                with open(args.out,'wb') as out:
                    ct=pickle.load(ct)
                    pkbin=pickle.load(bPK)
                    bPK=deserializePK(pkbin)
                    modcipher = attackDeposit(ct, bPK)
                    pickle.dump(modcipher,out)
    elif args.command == 'attack2':
        with open(args.bankPK, 'rb') as bPK:
            with open(args.sk, 'rb') as cSK:
                with open(args.ct,'rb') as ct:
                    with open(args.out,'wb') as out:
                        ct=pickle.load(ct)
                        pkbin=pickle.load(bPK)
                        bPK=deserializePK(pkbin)
                        cskbin=pickle.load(cSK)
                        cSK=deserializeSK(cskbin)
                        modcipher = attackSignature(ct, cSK, bPK)
                        pickle.dump(modcipher,out)
    elif args.command == "bankReceiveDeposit":
         with open(args.bankSK,'rb') as fSK:
            with open(args.ciphertext,'rb') as ctextf:
                with open(args.out, 'wb') as out:
                    skBin = pickle.load(fSK)
                    bankSK = deserializeSK(skBin)
                    ciphertext = pickle.load(ctextf)
                    deposit = bankReceiveDeposit(ciphertext,bankSK)
                    pickle.dump(deposit, out)
    elif args.command == 'bankReceiveDepositSigned':
        with open(args.bankSK,'rb') as fSK:
            with open(args.ciphertext,'rb') as ctextf:
                with open(args.accounts) as accounts:
                    with open(args.out, 'wb') as out:
                        names_to_pk_files = json.load(open(args.accounts))
                        accounts = {k: deserializePK(pickle.load(open(v,'rb'))) for (k,v) in names_to_pk_files.items()}
                        skBin = pickle.load(fSK)
                        bankSK = deserializeSK(skBin)
                        ciphertext = pickle.load(ctextf)
                        deposit = bankReceiveSignedDeposit(ciphertext,bankSK, accounts)
                        pickle.dump(deposit, out)

    else:
        print("Command out of bounds!")
