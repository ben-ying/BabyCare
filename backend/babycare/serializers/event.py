from rest_framework import serializers

from babycare.models import BabyUser, Event


class EventSerializer(serializers.ModelSerializer):
    event_id = serializers.IntegerField(read_only=True, source="id")
    baby_id = serializers.IntegerField(read_only=True, source="baby.id")

    class Meta:
        model = Event
        fields = ['event_id', 'baby_id', 'title', 'content', 'like', 'size', 'height', 'weight', 'remark']
