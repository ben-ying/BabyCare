1. sudo su - postgres  (mac: psql -U postgres)
2. psql -l # show all user
3. SHOW config_file; # show config path
4. psql -c "\du" # show all databases
5. ALTER USER <user> CREATEDB; # grant user createdb permission
6. sudo /etc/init.d/postgresql reload # restart postgresql
7. ALTER ROLE ben WITH SUPERUSER; (\h CREATE ROLE;)
ALTER ROLE ben WITH CREATEROLE;
8. dropdb development_db_name
createdb developmnent_db_name
9. pg_dump -U ben babycare > dbexport.pgsql
10. psql -U ben babycare < dbexport.pgsql
11. # login postgres: (http://askubuntu.com/questions/413585/postgres-password-authentication-fails)
You are confusing the password for the unix user "postgres" with the database password for the database user "postgres". These are not the same.
You have locked yourself out, because you enabled md5 authentication for user postgres without setting a password for the database user postgres.
Add a new line to the top of pg_hba.conf:
    local    postgres     postgres     peer
    then restart/reload PostgreSQL and:
    sudo -u postgres psql
    From the resulting prompt:
    ALTER USER postgres PASSWORD 'my_postgres_password';
    then remove the line you added to pg_hba.conf and restart Pg again. You can now use the password you set above to connect to PostgreSQL as the postgres user.
    To learn more, read the "client authentication" chapter of the user manual and the docs on pg_hba.conf.

12. # kill postgres session
SELECT 
    pg_terminate_backend(pid) 
FROM 
    pg_stat_activity 
WHERE 
    -- don't kill my own connection!
        pid <> pg_backend_pid()
    -- don't kill the connections to other databases
    AND datname = 'babycare'
                            ;
