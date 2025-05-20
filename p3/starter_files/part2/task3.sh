echo '---task3--- START'
echo '*** first command ***'
python3 part2.py clientSendDeposit Charlie Bob 50 Bank.enc_pk t3ctext 
echo '*** second command ***'
python3 part2.py bankReceiveDeposit Bank.enc_sk t3ctext t3Received
echo '---task3--- END'
