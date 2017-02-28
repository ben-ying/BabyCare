#!/usr/bin/env bash
rm -rf babycare/migrations/00*
#rm -rf db.sqlite3
dropdb babycare
createdb babycare
python manage.py migrate
python manage.py makemigrations babycare
python manage.py sqlmigrate babycare 0001
python manage.py migrate
python manage.py createsuperuser --username=ben --email=benying1988@gmail.com
