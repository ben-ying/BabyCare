#!/usr/bin/env python
# -*- coding: utf-8 -*-
import time

from rest_framework.authtoken.models import Token

from babycare.serializers.event import EventSerializer
from babycare.views import CustomModelViewSet
from constants import CODE_INVALID_REQUEST, PROFILE_FOOTER_IMAGE
from constants import CODE_SUCCESS, MSG_EMPTY_EVENT_TITLE, CODE_EMPTY_EVENT_TITLE, MSG_EMPTY_EVENT_MESSAGE, \
    CODE_EMPTY_EVENT_MESSAGE, \
    MSG_CREATE_EVENT_SUCCESS
from constants import MSG_400
from models import BabyUser
from models import Event
from utils import json_response, invalid_token_response, upload_image_to_oss
from utils import simple_json_response


class EventViewSet(CustomModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        token = request.data.get('token')
        title = request.data.get('title')
        message = request.data.get('message')
        base64 = request.data.get('base64', '')

        if not title:
            return simple_json_response(CODE_EMPTY_EVENT_TITLE, MSG_EMPTY_EVENT_TITLE)
        elif not message:
            return simple_json_response(CODE_EMPTY_EVENT_MESSAGE, MSG_EMPTY_EVENT_MESSAGE)
        elif serializer.is_valid():
            if Token.objects.filter(key=token):
                user = Token.objects.get(key=token).user
                serializer.validated_data['baby_id'] = BabyUser.objects.get(user=user).id
                self.perform_create(serializer)
                response_data = serializer.data
                if base64:
                    image_name = title + time.strftime('%Y%m%d%H%M%S') + PROFILE_FOOTER_IMAGE
                    response_data['profile'] = upload_image_to_oss(image_name, base64)
                    # event = Event.objects.get(baby=user)
                    # event.profile = response_data['profile']
                    # event.save()
                return json_response(serializer.data, CODE_SUCCESS, MSG_CREATE_EVENT_SUCCESS)
            else:
                return invalid_token_response()
        else:
            return simple_json_response(CODE_INVALID_REQUEST, MSG_400)
