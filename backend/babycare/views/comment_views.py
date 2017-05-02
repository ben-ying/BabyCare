#!/usr/bin/env python
# -*- coding: utf-8 -*-
import json

from rest_framework import status

from babycare.constants import CODE_SUCCESS, MSG_GET_COMMENTS_SUCCESS, MSG_DELETE_COMMENT_SUCCESS, CODE_EMPTY_COMMENT, \
    MSG_EMPTY_COMMENT_FIELD, MSG_POST_COMMENT_SUCCESS, CODE_NO_CONTENT, MSG_204
from babycare.models import Comment, Event, BabyUser
from babycare.serializers.comment import CommentSerializer
from babycare.utils import invalid_token_response, get_user_by_token, save_error_log, CustomModelViewSet, json_response, \
    simple_json_response


class CommentViewSet(CustomModelViewSet):
    queryset = Comment.objects.all()
    serializer_class = CommentSerializer

    def create(self, request, *args, **kwargs):
        try:
            token = request.data.get('token')
            text = request.data.get('text')
            event_id = request.data.get('event_id')
            user_id = request.data.get('user_id')
            source_comment_id = request.data.get('source_comment_id', -1)
            user = get_user_by_token(token)

            if not text or not event_id or not user_id:
                return simple_json_response(CODE_EMPTY_COMMENT, MSG_EMPTY_COMMENT_FIELD)
            if user:
                comment = Comment()
                comment.text = text
                comment.event = Event.objects.get(id=event_id)
                comment.baby = BabyUser.objects.get(id=user_id)
                if source_comment_id > 0 and Comment.objects.filter(id=source_comment_id):
                    comment.source_comment = Comment.objects.get(id=source_comment_id)
                comment.save()
                response = CommentSerializer(comment).data
                return json_response(response, CODE_SUCCESS, MSG_POST_COMMENT_SUCCESS)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)

    def list(self, request, *args, **kwargs):
        try:
            token = request.query_params.get('token')
            user = get_user_by_token(token)
            if user:
                response = super(CommentViewSet, self).list(request, *args, **kwargs).data
                return json_response(response, CODE_SUCCESS, MSG_GET_COMMENTS_SUCCESS)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)

    def get_queryset(self):
        event_id = self.request.query_params.get('event_id', -1)
        if int(event_id) < 0:
            return super(CommentViewSet, self).get_queryset()
        else:
            return super(CommentViewSet, self).get_queryset().filter(event_id=event_id)

    def destroy(self, request, *args, **kwargs):
        try:
            obj = json.loads(request.body)
            token = obj.get('token')
            user = get_user_by_token(token)
            if user:
                comment = self.get_object()
                if comment:
                    try:
                        response = super(CommentViewSet, self).destroy(request, *args, **kwargs)
                        if response.status_code != status.HTTP_204_NO_CONTENT:
                            comment.id = -1
                    except Exception as e:
                        comment.id = -1
                    comment_json = CommentSerializer(comment).data
                    return json_response(comment_json, CODE_SUCCESS, MSG_DELETE_COMMENT_SUCCESS)
                else:
                    return simple_json_response(CODE_NO_CONTENT, MSG_204)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)
