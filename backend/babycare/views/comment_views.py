#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time

from django.utils import timezone

from babycare.constants import CODE_SUCCESS
from babycare.utils import invalid_token_response, get_user_by_token, save_error_log, upload_image_to_oss, \
    CustomModelViewSet
from babycare.utils import simple_json_response


class CommentViewSet(CustomModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def list(self, request, *args, **kwargs):

        try:
            token = request.query_params.get('token')
            user = get_user_by_token(token)
            if user:
                response = super(EventViewSet, self).list(request, *args, **kwargs).data
                for eventDict in response['results']:
                    # pdb.set_trace()
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
