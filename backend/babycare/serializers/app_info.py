from rest_framework import serializers

from babycare.models import Event, AppInfo


class AppInfoSerializer(serializers.ModelSerializer):
    app_link = serializers.IntegerField(read_only=True, source='app_file.file.url')
    app_name = serializers.IntegerField(read_only=True, source='app_file.file.name')

    class Meta:
        model = AppInfo
        fields = ['version_name', 'version_code', 'version_type', 'type', 'app_link', 'app_name', 'datetime']
