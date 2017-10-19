from rest_framework import serializers

from babycare.models import Iaer


class IaerSerializer(serializers.ModelSerializer):
    iaer_id = serializers.IntegerField(read_only=True, source="id")
    user_id = serializers.IntegerField(read_only=True, source='baby.id')

    class Meta:
        model = Iaer
        fields = ['iaer_id', 'user_id', 'money', 'money_type', 'category', 'remark', 'created', 'datetime']
