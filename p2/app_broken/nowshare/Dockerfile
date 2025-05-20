FROM python:3.7.2-alpine
EXPOSE 5000
WORKDIR /code
COPY ./app .
RUN pip install -r requirements.txt

CMD FLASK_DEBUG=1 python -m flask run --host=0.0.0.0 