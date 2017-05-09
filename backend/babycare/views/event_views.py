#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json
import time

from django.utils import timezone
from rest_framework import status
from rest_framework.decorators import api_view

from babycare.constants import CODE_EMPTY_EVENT, MSG_EMPTY_EVENT, EVENT_FOOTER_IMAGE, \
    MSG_GET_EVENTS_SUCCESS, MSG_DELETE_EVENT_SUCCESS, CODE_NO_CONTENT, MSG_204, TYPE_IMAGE, TYPE_VIDEO, \
    EVENT_FOOTER_VIDEO, EVENT_FOOTER_VIDEO_THUMBNAIL
from babycare.constants import CODE_SUCCESS, MSG_POST_EVENT_SUCCESS
from babycare.models import BabyUser, Like
from babycare.models import Event
from babycare.serializers.event import EventSerializer
from babycare.serializers.like import LikeSerializer
from babycare.utils import json_response, invalid_token_response, get_user_by_token, CustomModelViewSet, upload_file_to_oss, \
    save_error_log
from babycare.utils import simple_json_response


class EventViewSet(CustomModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def list(self, request, *args, **kwargs):
        try:
            token = request.query_params.get('token')
            user = get_user_by_token(token)
            if user:
                response = super(EventViewSet, self).list(request, *args, **kwargs).data
                for eventDict in response['results']:
                    like_list = list()
                    likes = Like.objects.filter(event=eventDict['event_id'])
                    for like in likes:
                        data = LikeSerializer(like).data
                        like_list.append(data)
                    eventDict['likes'] = like_list

                return json_response(response, CODE_SUCCESS, MSG_GET_EVENTS_SUCCESS)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)

    def get_queryset(self):
        user_id = self.request.query_params.get('user_id', -1)
        if int(user_id) < 0:
            return super(EventViewSet, self).get_queryset().order_by("-id")
        else:
            return super(EventViewSet, self).get_queryset().filter(baby_id=user_id).order_by("-id")

    def create(self, request, *args, **kwargs):
        try:
            token = request.data.get('token')
            title = request.data.get('title')
            content = request.data.get('content')
            base64s = request.data.get('base64s')
            type = request.data.get('type', 0)
            user = get_user_by_token(token)

            if not title and not content and not base64s:
                return simple_json_response(CODE_EMPTY_EVENT, MSG_EMPTY_EVENT)
            if user:
                event = Event()
                event.baby = BabyUser.objects.get(user=user)
                event.created = timezone.now()
                if title:
                    event.title = title
                if content:
                    event.content = content
                if base64s:
                    # only one image for now
                    if type == TYPE_IMAGE:
                        for image in base64s:
                            image_name = user.username + time.strftime('%Y%m%d%H%M%S') + EVENT_FOOTER_IMAGE
                            image = upload_file_to_oss(image_name, image, type)
                            event.image1 = image
                    elif type == TYPE_VIDEO:
                        for video in base64s:
                            video_name = user.username + time.strftime('%Y%m%d%H%M%S') + EVENT_FOOTER_VIDEO
                            video = upload_file_to_oss(video_name, video, type)
                            event.video = video
                            image_name = user.username + time.strftime('%Y%m%d%H%M%S') + EVENT_FOOTER_VIDEO_THUMBNAIL
                            image = upload_file_to_oss(image_name, request.data.get('video_thumbnail'), TYPE_IMAGE)
                            event.video_thumbnail = image
                event.save()
                response = EventSerializer(event).data
                return json_response(response, CODE_SUCCESS, MSG_POST_EVENT_SUCCESS)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)

    def retrieve(self, request, *args, **kwargs):
        return super(EventViewSet, self).retrieve(request, *args, **kwargs)

    def destroy(self, request, *args, **kwargs):
        try:
            obj = json.loads(request.body)
            token = obj.get('token')
            user = get_user_by_token(token)
            if user:
                event = self.get_object()
                if event:
                    try:
                        response = super(EventViewSet, self).destroy(request, *args, **kwargs)
                        if response.status_code != status.HTTP_204_NO_CONTENT:
                            event.id = -1
                    except Exception as e:
                        event.id = -1
                    event_json = EventSerializer(event).data
                    return json_response(event_json, CODE_SUCCESS, MSG_DELETE_EVENT_SUCCESS)
                else:
                    return simple_json_response(CODE_NO_CONTENT, MSG_204)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)


@api_view(['POST'])
def like_view(request):
    try:
        token = request.data.get('token')
        event_id = request.data.get("event_id")
        like_user_id = request.data.get("like_user_id")
        user = get_user_by_token(token)
        if user:
            baby = BabyUser.objects.get(id=like_user_id)
            event = Event.objects.get(id=event_id)
            if Like.objects.filter(event=event, baby=baby):
                Like.objects.filter(event=event, baby=baby).delete()
            like = Like()
            like.baby = baby
            like.event = event
            like.save()
            response = LikeSerializer(like).data
            return json_response(response, CODE_SUCCESS, MSG_POST_EVENT_SUCCESS)
        else:
            return invalid_token_response()
    except Exception as e:
        return save_error_log(request, e)
