#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pdb

import time
from django.utils import timezone
from babycare.serializers.event import EventSerializer
from constants import CODE_EMPTY_EVENT, MSG_EMPTY_EVENT, PROFILE_FOOTER_IMAGE, EVENT_FOOTER_IMAGE, \
    MSG_GET_EVENTS_SUCCESS
from constants import CODE_SUCCESS, MSG_CREATE_EVENT_SUCCESS
from models import BabyUser
from models import Event
from utils import json_response, invalid_token_response, get_user_by_token, CustomModelViewSet, upload_image_to_oss
from utils import simple_json_response
from django.utils.dateparse import parse_datetime


class EventViewSet(CustomModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def list(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid()
        return json_response(super(EventViewSet, self).list(request, *args, **kwargs).data, CODE_SUCCESS, MSG_GET_EVENTS_SUCCESS)

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        token = request.data.get('token')
        title = request.data.get('title')
        content = request.data.get('content')
        base64_images = request.data.get('base64_images')
        user = get_user_by_token(token)
        image = None

        if not title and not content and not base64_images:
            return simple_json_response(CODE_EMPTY_EVENT, MSG_EMPTY_EVENT)
        if user:
            event = Event()
            event.baby = BabyUser.objects.get(user=user)
            event.created = timezone.now()
            pdb.set_trace()
            if title:
                event.title = title
            if content:
                event.content = content
            if base64_images:
                # only one image for now
                for image in base64_images:
                    image_name = user.username + time.strftime('%Y%m%d%H%M%S') + EVENT_FOOTER_IMAGE
                    image = upload_image_to_oss(image_name, image)
                    event.image1 = image
            event.save()
            serializer.is_valid()
            response = serializer.data
            response['event_id'] = event.id
            baby_user = BabyUser.objects.get(user=user)
            response['user_id'] = baby_user.id
            response['image1'] = image
            response['modified'] = str(event.modified)
            response['created'] = str(event.created)
            return json_response(response, CODE_SUCCESS, MSG_CREATE_EVENT_SUCCESS)
        else:
            return invalid_token_response()
