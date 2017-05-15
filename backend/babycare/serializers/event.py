from rest_framework import serializers

from babycare.models import Event


class EventSerializer(serializers.ModelSerializer):
    event_id = serializers.IntegerField(read_only=True, source='id')
    user_id = serializers.IntegerField(read_only=True, source='baby.id')
    username = serializers.CharField(read_only=True, source='baby.baby_name')
    user_profile = serializers.CharField(read_only=True, source='baby.profile')

    class Meta:
        model = Event
        fields = ['event_id', 'user_id', 'username', 'type', 'user_profile', 'video_url', 'video_thumbnail',
                  'title', 'content', 'image1', 'modified', 'created']
