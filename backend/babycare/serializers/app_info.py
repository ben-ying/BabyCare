from rest_framework import serializers

from babycare.models import Event, AppInfo


class AppInfoSerializer(serializers.ModelSerializer):
    app_link = serializers.CharField(read_only=True, source='app_file.url')

    class Meta:
        model = AppInfo
        fields = ['version_name', 'version_code', 'version_type', 'app_link', 'update_info', 'datetime']
