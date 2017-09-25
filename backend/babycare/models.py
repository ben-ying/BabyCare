# !/usr/bin/python
# coding:utf-8

from __future__ import unicode_literals

import os

from django.db import models
from django.db.models import TextField
from django.forms import IntegerField

from babycare.utils import upload_file


class BabyUser(models.Model):
    id = IntegerField(label='ID')
    user = models.ForeignKey('auth.User', on_delete=models.CASCADE)
    baby_name = models.CharField(max_length=100, blank=True, null=True)
    phone = models.CharField(max_length=30, blank=True, null=True)
    gender = models.IntegerField(default=2) #0 for boy, 1 for girl, 2 for others
    profile = models.CharField(max_length=200, blank=True, null=True)
    type = models.IntegerField(default=0)
    region = models.CharField(max_length=100, blank=True, null=True)
    locale = models.CharField(max_length=10, blank=True, null=True)
    whats_up = models.CharField(max_length=200, blank=True, null=True)
    zone = models.CharField(max_length=50, blank=True, null=True)
    birth = models.DateField(blank=True, null=True)
    hobbies = models.TextField(max_length=500, blank=True, null=True)
    highlighted = models.TextField(blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)
    is_email_activate = models.BooleanField(default=False)
    is_phone_activate = models.BooleanField(default=False)

    def __str__(self):
        return self.user.email + '(' + self.user.username + ')'

    def save(self, *args, **kwargs):
        super(BabyUser, self).save(*args, **kwargs)


class Event(models.Model):
    id = IntegerField(label='ID')
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    type = models.IntegerField(default=0)
    title = models.CharField(max_length=100, blank=True, null=True)
    content = models.TextField(blank=True, null=True)
    image1 = models.CharField(max_length=200, blank=True, null=True)
    image2 = models.CharField(max_length=200, blank=True, null=True)
    image3 = models.CharField(max_length=200, blank=True, null=True)
    image4 = models.CharField(max_length=200, blank=True, null=True)
    image5 = models.CharField(max_length=200, blank=True, null=True)
    image6 = models.CharField(max_length=200, blank=True, null=True)
    image7 = models.CharField(max_length=200, blank=True, null=True)
    image8 = models.CharField(max_length=200, blank=True, null=True)
    image9 = models.CharField(max_length=200, blank=True, null=True)
    video_url = models.CharField(max_length=200, blank=True, null=True)
    video_width = models.IntegerField(blank=True, null=True)
    video_height = models.IntegerField(blank=True, null=True)
    video_thumbnail = models.CharField(max_length=200, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.baby.user.username


class LoginLog(models.Model):
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    system_type = models.CharField(max_length=10) # 1 for android, 2 for ios
    system_version = models.CharField(max_length=30) # 20(7.0.1)
    phone_model = models.CharField(max_length=30) # Sony E8230
    country = models.CharField(max_length=30) #CN
    state = models.CharField(max_length=30)
    city = models.CharField(max_length=30)
    created = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.baby.username


class Verify(models.Model):
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    email_verify_code = models.CharField(max_length=10, blank=True, null=True)
    phone_verify_code = models.CharField(max_length=10, blank=True, null=True)
    created = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.baby.user.username


class Like(models.Model):
    id = IntegerField(label='ID')
    event = models.ForeignKey(Event, on_delete=models.CASCADE)
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    datetime = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.baby.user.username


class Feedback(models.Model):
    id = IntegerField(label='ID')
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    description = models.TextField()
    image1 = models.CharField(max_length=200, blank=True, null=True)
    image2 = models.CharField(max_length=200, blank=True, null=True)
    image3 = models.CharField(max_length=200, blank=True, null=True)
    image4 = models.CharField(max_length=200, blank=True, null=True)
    image5 = models.CharField(max_length=200, blank=True, null=True)
    image6 = models.CharField(max_length=200, blank=True, null=True)
    image7 = models.CharField(max_length=200, blank=True, null=True)
    image8 = models.CharField(max_length=200, blank=True, null=True)
    image9 = models.CharField(max_length=200, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.baby.user.username


class Comment(models.Model):
    id = IntegerField(label='ID')
    text = models.TextField()
    event = models.ForeignKey(Event, on_delete=models.CASCADE)
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    source_comment = models.ForeignKey('self', blank=True, null=True, on_delete=models.CASCADE)
    datetime = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.text


class AppInfo(models.Model):
    id = IntegerField(label='ID')
    version_name = models.CharField(max_length=50)
    version_code = models.IntegerField()
    version_type = models.IntegerField(default=0)
    update_info = models.TextField()
    app_file = models.FileField(upload_to='apk/%Y-%m-%d %H:%M:%S/')
    datetime = models.DateTimeField(auto_now=True, blank=True, null=True)

    def save(self, force_insert=False, force_update=False, using=None, update_fields=None):
        super(AppInfo, self).save(force_insert, force_update, using, update_fields)
        upload_file(self.app_file.file.name, 'APKs/' + os.path.basename(self.app_file.name))

    def __str__(self):
        return self.version_name + '(' + str(self.version_code) + ')'


class RedEnvelope(models.Model):
    id = IntegerField(label='ID')
    baby = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    money = models.CharField(max_length=10, blank=True, null=True)
    money_type = models.IntegerField(default=0) # 0 for rmb, 1 for dollar
    money_from = models.CharField(max_length=100, blank=True, null=True)
    remark = models.CharField(max_length=100, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)


class Iaer(models.Model):
    CATEGORY_CHOICES = (
        (u'生活用品', u'生活用品'),
        (u'服饰', u'服饰'),
        (u'餐饮', u'餐饮'),
        (u'孩子', u'孩子'),
        (u'收入', u'收入'),
        (u'其他', u'其他'),
    )

    id = IntegerField(label='ID')
    user = models.ForeignKey(BabyUser, on_delete=models.CASCADE)
    money = models.IntegerField()
    category = models.CharField(max_length=30, choices=CATEGORY_CHOICES)
    money_type = models.IntegerField(default=0)  # 0 for rmb, 1 for dollar
    remark = models.CharField(max_length=100, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    type = models.IntegerField(default=0)
    chart_type = models.IntegerField(default=0)
    format = models.CharField(max_length=50, blank=True, null=True)
    datetime = models.DateTimeField(auto_now=True, blank=True, null=True)
    description = TextField(blank=True, null=True)
    timing = models.CharField(max_length=100, blank=True, null=True) # for every week or every month input or consumption
