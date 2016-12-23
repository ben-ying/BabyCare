from __future__ import unicode_literals

from django.db import models


class Baby(models.Model):
    email = models.CharField(max_length=50, unique=True)
    username = models.CharField(max_length=50, unique=True) 
    password = models.CharField(max_length=100)
    phone = models.CharField(max_length=30, blank=True, null=True)
    baby_name = models.CharField(max_length=50, blank=True, null=True)
    firstname = models.CharField(max_length=50, blank=True, null=True)
    lastname = models.CharField(max_length=50, blank=True, null=True)
    gender = models.IntegerField(default=2) #0 for boy, 1 for girl, 2 for others
    profile = models.ImageField(blank=True, null=True)
    region = models.CharField(max_length=100, blank=True, null=True)
    whats_up = models.CharField(max_length=200, blank=True, null=True)
    birth = models.DateTimeField(blank=True, null=True)
    hobbies = models.CharField(max_length=500, blank=True, null=True)
    accesstoken = models.CharField(max_length=100, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.email + '(' + self.username + ')'

class Event(models.Model):
    baby = models.ForeignKey(Baby, on_delete=models.CASCADE)
    title = models.CharField(max_length=100)
    like = models.BooleanField(default=False)
    image = models.ImageField(blank=True, null=True)
    size = models.CharField(max_length=100, blank=True, null=True)
    height = models.FloatField(blank=True, null=True)
    weight = models.FloatField(blank=True, null=True)
    remark = models.TextField(blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.title

class LoginLog(models.Model):
    baby = models.ForeignKey(Baby, on_delete=models.CASCADE)
    system_type = models.CharField(max_length=10) # 1 for android, 2 for ios
    system_version = models.CharField(max_length=30) # 20(7.0.1)
    phone_model = models.CharField(max_length=30) # Sony E8230
    country = models.CharField(max_length=30) #CN
    state = models.CharField(max_length=30)
    city = models.CharField(max_length=30)
    created = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.baby.username


