from rest_framework import serializers

from babycare.constants import DATE_TIME_FORMAT
from babycare.models import BabyUser, Event


class EventSerializer(serializers.ModelSerializer):
    event_id = serializers.IntegerField(read_only=True, source='id')
    user_id = serializers.IntegerField(read_only=True, source='baby.id')

    class Meta:
        model = Event
        fields = ['event_id', 'user_id', 'title', 'content', 'image1', 'modified', 'created']
