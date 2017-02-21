from __future__ import unicode_literals

from django.db import models
#from django.contrib.postgres.fields import ArrayField
from django.forms import IntegerField
from pygments import highlight
from pygments.formatters.html import HtmlFormatter
from pygments.lexers import get_lexer_by_name


class Baby(models.Model):
    id = IntegerField(label='ID')
    user = models.ForeignKey('auth.User', related_name='snippets', on_delete=models.CASCADE)
    highlighted = models.TextField()
    phone = models.CharField(max_length=30, blank=True, null=True)
    gender = models.IntegerField(default=2) #0 for boy, 1 for girl, 2 for others
    #profile = ArrayField(ArrayField(models.ImageField()))
    region = models.CharField(max_length=100, blank=True, null=True)
    whats_up = models.CharField(max_length=200, blank=True, null=True)
    birth = models.DateTimeField(blank=True, null=True)
    hobbies = models.CharField(max_length=500, blank=True, null=True)
    accesstoken = models.CharField(max_length=100, blank=True, null=True)
    created = models.DateTimeField(editable=False, blank=True, null=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)

    def __str__(self):
        return self.user.email + '(' + self.user.username + ')'

    def save(self, *args, **kwargs):
        lexer = get_lexer_by_name("python")
        # linenos = self.phone and 'table' or False
        # options = self.whats_up and {'title': self.whats_up} or {}
        # formatter = HtmlFormatter()
        # self.highlighted = highlight(self.highlighted, lexer, formatter)
        super(Baby, self).save(*args, **kwargs)

class Event(models.Model):
    baby = models.ForeignKey(Baby, on_delete=models.CASCADE)
    title = models.CharField(max_length=100)
    like = models.BooleanField(default=False)
    #image = ArrayField(ArrayField(models.ImageField()))
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


