from rest_framework import serializers

from babycare.models import Baby


class BabySerializer(serializers.ModelSerializer):
    class Meta:
        model = Baby
        fields = ['phone', 'gender', 'region', 'whats_up', 'birth', 'hobbies', 'highlighted']
