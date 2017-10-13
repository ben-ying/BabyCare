from rest_framework import serializers

from babycare.models import Iae


class IaerSerializer(serializers.ModelSerializer):
    iaer_id = serializers.IntegerField(read_only=True, source="id")
    user_id = serializers.IntegerField(read_only=True, source='baby.id')

    class Meta:
        model = Iae
        fields = ['iaer_id', 'user_id', 'money', 'money_type', 'category', 'remark', 'datetime']
