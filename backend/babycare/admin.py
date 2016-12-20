from django.contrib import admin
from models import Baby, Event

class BabyAdmin(admin.ModelAdmin):
    list_display = ('email', 'name', 'baby_name')
    search_fields = ('email', 'name', 'baby_name')
    #fields = ('email', 'name', 'baby_name')

admin.site.register(Baby, BabyAdmin)


class EventAdmin(admin.ModelAdmin):
    list_display = ('baby',)
    search_fields = ('baby',)
admin.site.register(Event, EventAdmin)
