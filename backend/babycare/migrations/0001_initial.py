# -*- coding: utf-8 -*-
# Generated by Django 1.10.4 on 2017-09-25 06:29
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
            name='AppInfo',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('version_name', models.CharField(max_length=50)),
                ('version_code', models.IntegerField()),
                ('version_type', models.IntegerField(default=0)),
                ('update_info', models.TextField()),
                ('app_file', models.FileField(upload_to='apk/%Y-%m-%d %H:%M:%S/')),
                ('datetime', models.DateTimeField(auto_now=True, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='BabyUser',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('baby_name', models.CharField(blank=True, max_length=100, null=True)),
                ('phone', models.CharField(blank=True, max_length=30, null=True)),
                ('gender', models.IntegerField(default=2)),
                ('profile', models.CharField(blank=True, max_length=200, null=True)),
                ('type', models.IntegerField(default=0)),
                ('region', models.CharField(blank=True, max_length=100, null=True)),
                ('locale', models.CharField(blank=True, max_length=10, null=True)),
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
            name='Comment',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('text', models.TextField()),
                ('datetime', models.DateTimeField(auto_now=True, null=True)),
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
            ],
        ),
        migrations.CreateModel(
            name='Event',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.IntegerField(default=0)),
                ('title', models.CharField(blank=True, max_length=100, null=True)),
                ('content', models.TextField(blank=True, null=True)),
                ('image1', models.CharField(blank=True, max_length=200, null=True)),
                ('image2', models.CharField(blank=True, max_length=200, null=True)),
                ('image3', models.CharField(blank=True, max_length=200, null=True)),
                ('image4', models.CharField(blank=True, max_length=200, null=True)),
                ('image5', models.CharField(blank=True, max_length=200, null=True)),
                ('image6', models.CharField(blank=True, max_length=200, null=True)),
                ('image7', models.CharField(blank=True, max_length=200, null=True)),
                ('image8', models.CharField(blank=True, max_length=200, null=True)),
                ('image9', models.CharField(blank=True, max_length=200, null=True)),
                ('video_url', models.CharField(blank=True, max_length=200, null=True)),
                ('video_width', models.IntegerField(blank=True, null=True)),
                ('video_height', models.IntegerField(blank=True, null=True)),
                ('video_thumbnail', models.CharField(blank=True, max_length=200, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
            ],
        ),
        migrations.CreateModel(
            name='Feedback',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('description', models.TextField()),
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
        migrations.CreateModel(
            name='Like',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('datetime', models.DateTimeField(auto_now=True, null=True)),
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
                ('event', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.Event')),
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
            name='RedEnvelope',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('money', models.CharField(blank=True, max_length=10, null=True)),
                ('money_type', models.IntegerField(default=0)),
                ('money_from', models.CharField(blank=True, max_length=100, null=True)),
                ('remark', models.CharField(blank=True, max_length=100, null=True)),
                ('created', models.DateTimeField(blank=True, editable=False, null=True)),
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
                ('baby', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.BabyUser')),
            ],
        ),
        migrations.AddField(
            model_name='comment',
            name='event',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='babycare.Event'),
        ),
        migrations.AddField(
            model_name='comment',
            name='source_comment',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='babycare.Comment'),
        ),
    ]
