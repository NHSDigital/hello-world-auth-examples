-include .env

.EXPORT_ALL_VARIABLES:

run:
	docker run -it -p 8000:8000 nhs-login-php -e CLIENT_SECRET=${CLIENT_SECRET} -e CLIENT_ID=${CLIENT_ID}

