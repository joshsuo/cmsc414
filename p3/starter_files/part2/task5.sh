echo '---task5--- START'
echo '*** first command ***'
python3 part2.py clientSendSignedDeposit Charlie Bob 40 Bank.enc_pk t5ctext Charlie.sig_sk
echo '*** first command ***'
python3 part2.py bankReceiveDepositSigned Bank.enc_sk t5ctext t5Received accounts.json
echo '---task5--- END'
