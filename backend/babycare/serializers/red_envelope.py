from rest_framework import serializers

from babycare.models import RedEnvelope


class RedEnvelopeSerializer(serializers.ModelSerializer):
    red_envelope_id = serializers.IntegerField(read_only=True, source="id")
    user_id = serializers.IntegerField(read_only=True, source='baby.id')

    class Meta:
        model = RedEnvelope
        fields = ['red_envelope_id', 'user_id', 'money', 'money_type', 'money_from', 'remark', 'created']
