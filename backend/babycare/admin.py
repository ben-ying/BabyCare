from django.contrib import admin
from models import Baby
from models import Event

class BabyAdmin(admin.ModelAdmin):
    list_display = ('username', 'email', 'baby_name')
    search_fields = ('username', 'email', 'baby_name')
    #fields = ('email', 'name', 'baby_name')

admin.site.register(Baby, BabyAdmin)


class EventAdmin(admin.ModelAdmin):
    list_display = ('title', 'baby',)
    search_fields = ('title', 'baby',)
admin.site.register(Event, EventAdmin)
