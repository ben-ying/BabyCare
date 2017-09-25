from django.contrib import admin

from models import BabyUser, Verify, Like, Feedback, Comment, AppInfo, RedEnvelope, Iaer
from models import Event


class BabyAdmin(admin.ModelAdmin):
    list_display = ('user', 'phone', 'baby_name', 'zone', 'locale', 'profile', 'created', 'modified')
    search_fields = ('user', 'phone', 'baby_name')
    #fields = ('email', 'name', 'baby_name')

admin.site.register(BabyUser, BabyAdmin)


class EventAdmin(admin.ModelAdmin):
    list_display = ('id', 'baby','title', 'content', 'image1', 'created', 'modified')
    search_fields = ('baby','title', 'content', 'image1')
admin.site.register(Event, EventAdmin)


class VerifyAdmin(admin.ModelAdmin):
    list_display = ('baby', 'email_verify_code', 'created')
    search_fields = ('baby', 'email_verify_code')
admin.site.register(Verify, VerifyAdmin)


class LikeAdmin(admin.ModelAdmin):
    list_display = ('event', 'baby', 'datetime')
    search_fields = ('event', 'baby')
admin.site.register(Like, LikeAdmin)


class FeedbackAdmin(admin.ModelAdmin):
    list_display = ('baby', 'description', 'image1', 'image2', 'image3', 'created')
    search_fields = ('baby', 'description')
admin.site.register(Feedback, FeedbackAdmin)


class CommentAdmin(admin.ModelAdmin):
    list_display = ('id', 'text', 'event', 'baby', 'source_comment', 'datetime')
    search_fields = ('text', 'event', 'baby')
admin.site.register(Comment, CommentAdmin)


class AppInfoAdmin(admin.ModelAdmin):
    list_display = ('version_name', 'version_code', 'app_file', 'update_info', 'datetime')
    search_fields = ('version_name', 'version_code', 'app_file', 'update_info', 'datetime')
admin.site.register(AppInfo, AppInfoAdmin)


class RedEnvelopeAdmin(admin.ModelAdmin):
    list_display = ('baby', 'money', 'money_type', 'money_from', 'remark', 'created')
    search_fields = ('baby', 'money', 'money_type', 'money_from', 'remark', 'created')
admin.site.register(RedEnvelope, RedEnvelopeAdmin)


class IaerAdmin(admin.ModelAdmin):
    list_display = ('user', 'money', 'category', 'datetime', 'remark', 'created')
    search_fields = ('user', 'money', 'category', 'datetime', 'remark', 'created')
    fields = ('user', 'money', 'category', 'remark')
admin.site.register(Iaer, IaerAdmin)