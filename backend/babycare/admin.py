from django.contrib import admin

from models import BabyUser, Verify
from models import Event


class BabyAdmin(admin.ModelAdmin):
    list_display = ('user', 'phone', 'baby_name', 'created', 'modified')
    search_fields = ('user', 'phone', 'baby_name')
    #fields = ('email', 'name', 'baby_name')

admin.site.register(BabyUser, BabyAdmin)


class EventAdmin(admin.ModelAdmin):
    list_display = ('baby','title', 'content', 'image1', 'created', 'modified')
    search_fields = ('baby','title', 'content', 'image1')
admin.site.register(Event, EventAdmin)


class VerifyAdmin(admin.ModelAdmin):
    list_display = ('user', 'email_verify_code', 'created')
    search_fields = ('user', 'email_verify_code', 'created')
admin.site.register(Verify, VerifyAdmin)
