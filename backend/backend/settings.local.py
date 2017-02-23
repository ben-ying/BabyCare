from settings_common import *


SECRET_KEY = '!(poief@tj(fc*o3hifd6a2^$(1qk!m)hv-uug2eh)anp^9u5%'

DEBUG = True

ALLOWED_HOSTS = ['localhost', '127.0.0.1']
DATABASES = {
    'default': {
        # 'ENGINE': 'django.db.backends.sqlite3',
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        # 'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
        'NAME': 'babycare',
        'PASSWORD': '123456',
        'HOST': 'localhost',
        'PORT': '',
    }
}
