from rest_framework import serializers

from babycare.models import Baby


class BabySerializer(serializers.ModelSerializer):
    baby_id = serializers.IntegerField(read_only=True, source="id")
    user_id = serializers.IntegerField(read_only=True, source="user.id")
    username = serializers.CharField(read_only=True, max_length=100, source="user.username")
    email = serializers.CharField(read_only=True, max_length=100, source="user.email")

    class Meta:
        model = Baby
        fields = ['baby_id', 'user_id', 'username', 'baby_name', 'profile', 'email', 'phone', 'gender', 'region',
                  'zone', 'whats_up', 'birth', 'hobbies', 'highlighted', 'is_email_activate', 'is_phone_activate']
