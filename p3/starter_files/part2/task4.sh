echo '---task4--- START'
echo '*** first command ***'
python3 part2.py attack1 Bank.enc_pk t3ctext t4ctextTweaked
echo '*** second command ***'
python3 part2.py bankReceiveDeposit Bank.enc_sk t4ctextTweaked t4Received
echo '---task4--- END'
