FROM php:8.1.11RC1-zts-buster
USER root

RUN apt-get update -y && apt-get install -y libmcrypt-dev

RUN curl -1sLf 'https://dl.cloudsmith.io/public/symfony/stable/setup.deb.sh' | bash && \
apt install symfony-cli -y

RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

WORKDIR /app
COPY . /app

RUN composer install

EXPOSE 8000
CMD symfony server:start

