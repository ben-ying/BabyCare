# -*- coding: utf-8 -*-
# Generated by Django 1.10.4 on 2017-10-14 00:18
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('babycare', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Iaer',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('money', models.IntegerField()),
                ('category', models.CharField(choices=[('\u751f\u6d3b\u7528\u54c1', '\u751f\u6d3b\u7528\u54c1'), ('\u670d\u9970', '\u670d\u9970'), ('\u9910\u996e', '\u9910\u996e'), ('\u5b69\u5b50', '\u5b69\u5b50'), ('\u6536\u5165', '\u6536\u5165'), ('\u5176\u4ed6', '\u5176\u4ed6')], max_length=30)),
                ('money_type', models.IntegerField(default=0)),
                ('remark', models.CharField(blank=True, max_length=100, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
                ('type', models.IntegerField(default=0)),
                ('chart_type', models.IntegerField(default=0)),
                ('format', models.CharField(blank=True, max_length=50, null=True)),
                ('datetime', models.DateTimeField(auto_now=True, null=True)),
                ('description', models.TextField(blank=True, null=True)),
                ('timing', models.CharField(blank=True, max_length=100, null=True)),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
            ],
        ),
    ]