from rest_framework import serializers

from babycare.models import BabyUser


class BabyUserSerializer(serializers.ModelSerializer):
    user_id = serializers.IntegerField(read_only=True, source="id")
    username = serializers.CharField(read_only=True, max_length=100, source="user.username")
    email = serializers.CharField(read_only=True, max_length=100, source="user.email")

    class Meta:
        model = BabyUser
        fields = ['user_id', 'username', 'baby_name', 'profile', 'email', 'phone', 'gender', 'type', 'region',
                  'zone', 'locale', 'whats_up', 'birth', 'hobbies', 'highlighted', 'is_email_activate', 'is_phone_activate', 'modified', 'created']
