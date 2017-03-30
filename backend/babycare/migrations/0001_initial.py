# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='BabyUser',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('baby_name', models.CharField(blank=True, max_length=100, null=True)),
                ('phone', models.CharField(blank=True, max_length=30, null=True)),
                ('gender', models.IntegerField(default=2)),
                ('profile', models.CharField(blank=True, max_length=200, null=True)),
                ('region', models.CharField(blank=True, max_length=100, null=True)),
                ('whats_up', models.CharField(blank=True, max_length=200, null=True)),
                ('zone', models.CharField(blank=True, max_length=50, null=True)),
                ('birth', models.DateField(blank=True, null=True)),
                ('hobbies', models.TextField(blank=True, max_length=500, null=True)),
                ('highlighted', models.TextField(blank=True, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('is_email_activate', models.BooleanField(default=False)),
                ('is_phone_activate', models.BooleanField(default=False)),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Event',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=100)),
                ('content', models.TextField()),
                ('image1', models.CharField(blank=True, max_length=200, null=True)),
                ('image2', models.CharField(blank=True, max_length=200, null=True)),
                ('image3', models.CharField(blank=True, max_length=200, null=True)),
                ('image4', models.CharField(blank=True, max_length=200, null=True)),
                ('image5', models.CharField(blank=True, max_length=200, null=True)),
                ('image6', models.CharField(blank=True, max_length=200, null=True)),
                ('image7', models.CharField(blank=True, max_length=200, null=True)),
                ('image8', models.CharField(blank=True, max_length=200, null=True)),
                ('image9', models.CharField(blank=True, max_length=200, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
            ],
        ),
        migrations.CreateModel(
            name='LoginLog',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('system_type', models.CharField(max_length=10)),
                ('system_version', models.CharField(max_length=30)),
                ('phone_model', models.CharField(max_length=30)),
                ('country', models.CharField(max_length=30)),
                ('state', models.CharField(max_length=30)),
                ('city', models.CharField(max_length=30)),
                ('created', models.DateTimeField(auto_now=True, null=True)),
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
            ],
        ),
        migrations.CreateModel(
            name='Verify',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('email_verify_code', models.CharField(blank=True, max_length=10, null=True)),
                ('phone_verify_code', models.CharField(blank=True, max_length=10, null=True)),
                ('created', models.DateTimeField(auto_now=True, null=True)),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]
