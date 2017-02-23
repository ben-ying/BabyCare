sudo su - postgres  (mac: psql -U postgres)
psql -l # show all user
SHOW config_file; # show config path

psql -c "\du” # show all databases
ALTER USER <user> CREATEDB; # grant user createdb permission
sudo /etc/init.d/postgresql reload

ALTER ROLE ben WITH SUPERUSER; (\h CREATE ROLE;)
ALTER ROLE ben WITH CREATEROLE;

dropdb development_db_name
createdb developmnent_db_name
pg_dump -U ben babycare > dbexport.pgsql
psql -U ben babycare < dbexport.pgsql


