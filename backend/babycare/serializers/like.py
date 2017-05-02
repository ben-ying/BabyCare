from rest_framework import serializers

from babycare.models import Like


class LikeSerializer(serializers.ModelSerializer):
    like_id = serializers.IntegerField(read_only=True, source='id')
    event_id = serializers.IntegerField(read_only=True, source='event.id')
    like_user_id = serializers.IntegerField(read_only=True, source='baby.id')

    class Meta:
        model = Like
        fields = ['like_id', 'event_id', 'like_user_id', 'datetime']
