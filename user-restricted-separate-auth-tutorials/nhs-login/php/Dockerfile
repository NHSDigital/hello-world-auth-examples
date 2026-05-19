FROM php:8.1.11RC1-zts-buster
USER root

RUN apt-get update -y && apt-get install -y libmcrypt-dev

# RUN curl -1sLf 'https://dl.cloudsmith.io/public/symfony/stable/setup.deb.sh' | bash && \
RUN curl --proto '=https' --tlsv1.2 -sSfL \
        'https://dl.cloudsmith.io/public/symfony/stable/setup.deb.sh' \
        -o setup-symfony.sh \
    && chmod +x setup-symfony.sh \
    && ./setup-symfony.sh \
    && apt-get update \
    && apt-get install -y symfony-cli

#RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer
# Step 1: Download installer
RUN php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"

# Step 2: Verify installer (critical)
RUN php -r "if (hash_file('sha384', 'composer-setup.php') === '<EXPECTED_HASH>') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); exit(1); }"

# Step 3: Run installer
RUN php composer-setup.php --install-dir=/usr/local/bin --filename=composer

# Step 4: Cleanup
RUN rm composer-setup.php

WORKDIR /app
COPY . /app

RUN composer install

EXPOSE 8000
CMD symfony server:start

