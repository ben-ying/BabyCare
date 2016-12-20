from __future__ import unicode_literals

from django.db import models


class Baby(models.Model):
    email = models.CharField(max_length=50, unique=True)
    phone = models.CharField(max_length=30, unique=True)
    name = models.CharField(max_length=50)
    baby_name = models.CharField(max_length=50)
    gender = models.IntegerField(blank=True, null=True) #0 for boy, 1 for girl, 2 for others
    profile = models.ImageField(blank=True, null=True)
    region = models.CharField(max_length=100, blank=True, null=True)
    whats_up = models.CharField(max_length=200, blank=True, null=True)
    birth = models.DateTimeField(blank=True, null=True)
    hobbies = models.CharField(max_length=500, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.email + '(' + self.phone + ')'

class Event(models.Model):
    baby = models.ForeignKey(Baby, on_delete=models.CASCADE)
    like = models.BooleanField(default=False)
    image = models.ImageField(blank=True, null=True)
    size = models.CharField(max_length=100, blank=True, null=True)
    height = models.FloatField(blank=True, null=True)
    weight = models.FloatField(blank=True, null=True)
    remark = models.TextField(blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return str(self.created)


