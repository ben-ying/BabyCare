from rest_framework import serializers

from babycare.models import Comment


class CommentSerializer(serializers.ModelSerializer):
    comment_id = serializers.IntegerField(read_only=True, source='id')
    event_id = serializers.IntegerField(read_only=True, source='event.id')
    comment_user_id = serializers.IntegerField(read_only=True, source='baby.id')
    indirect_user_id = serializers.IntegerField(read_only=True, source='source_comment.baby.id')
    indirect_user_name = serializers.CharField(read_only=True, source='source_comment.baby.baby_name')
    username = serializers.CharField(read_only=True, source='baby.baby_name')
    user_profile = serializers.CharField(read_only=True, source='baby.profile')

    class Meta:
        model = Comment
        fields = ['comment_id', 'event_id', 'text', 'datetime', 'username', 'user_profile',
                  'indirect_user_id', 'indirect_user_name', 'comment_user_id']
