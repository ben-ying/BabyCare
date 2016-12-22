rm -rf babycare/migrations/0001_initial.py
rm -rf db.sqlite3
python manage.py migrate
python manage.py makemigrations babycare
python manage.py sqlmigrate babycare 0001
python manage.py migrate
python manage.py createsuperuser --username=ben --email=benying1988@gmail.com
