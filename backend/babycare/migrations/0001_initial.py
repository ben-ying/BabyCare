# -*- coding: utf-8 -*-
# Generated by Django 1.10.4 on 2017-02-28 02:14
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
            name='Baby',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('phone', models.CharField(blank=True, max_length=30, null=True)),
                ('gender', models.IntegerField(default=2)),
                ('region', models.CharField(blank=True, max_length=100, null=True)),
                ('whats_up', models.CharField(blank=True, max_length=200, null=True)),
                ('birth', models.DateTimeField(blank=True, null=True)),
                ('hobbies', models.CharField(blank=True, max_length=500, null=True)),
                ('highlighted', models.TextField(blank=True, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='snippets', to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Event',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=100)),
                ('message', models.TextField()),
                ('like', models.BooleanField(default=False)),
                ('size', models.CharField(blank=True, max_length=100, null=True)),
                ('height', models.FloatField(blank=True, null=True)),
                ('weight', models.FloatField(blank=True, null=True)),
                ('remark', models.TextField(blank=True, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.Baby')),
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
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.Baby')),
            ],
        ),
    ]
