from rest_framework import serializers
from babycare.models import Baby


class BabySerializer(serializers.ModelSerializer):
    class Meta:
        model = Baby
        fields = '__all__'