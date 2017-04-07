from django.contrib import admin

from models import BabyUser, Verify, Like, Feedback
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
    search_fields = ('user', 'email_verify_code')
admin.site.register(Verify, VerifyAdmin)


class LikeAdmin(admin.ModelAdmin):
    list_display = ('event', 'baby', 'datetime')
    search_fields = ('event', 'baby')
admin.site.register(Like, LikeAdmin)


class FeedbackAdmin(admin.ModelAdmin):
    list_display = ('user', 'description', 'image1', 'image2', 'image3', 'created')
    search_fields = ('user', 'description')
admin.site.register(Feedback, FeedbackAdmin)
