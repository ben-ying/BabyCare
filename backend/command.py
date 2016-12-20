#Python2.7.6|Django1.10.4

#install MacVim
download MacVim.dmg and set
alias gvim='/Applications/MacVim.app/Contents/MacOS/Vim -g'
#check django version
python -m django --version
#install pip
curl -O https://bootstrap.pypa.io/get-pip.py
python get-pip.py
#install djanog
sudo pip install Django
sudo pip install Django==1.10.4
#create a project
django-admin.py startproject backend
#create an app
python manage.py startapp babycare
#create tables
python manage.py migrate
#crete babycate tables
python manage.py makemigrations babycare
#pip install file
sudo pip install -r requirements.txt
#show migration sql
python manage.py sqlmigrate babycare 0001
#python shell
python manage.py shell
import django
django.setup()
#create admin user
python manage.py createsuperuser
