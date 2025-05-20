echo '---task6--- START'
echo '*** first command ***'
python3 part2.py attack2 Bank.enc_pk  t5ctext t6AttackedCiphertext Eve.sig_sk  
echo '*** second command ***'
python3 part2.py bankReceiveDepositSigned Bank.enc_sk t6AttackedCiphertext t6Received accounts.json
echo '---task6--- START'
