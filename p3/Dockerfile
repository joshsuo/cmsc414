FROM ubuntu:20.04

RUN apt-get -y update
RUN apt-get -y upgrade

RUN apt install -y python3-pip
RUN python3 -m pip install cryptography==3.4.6
RUN python3 -m pip install PyNaCl==1.4.0

USER root