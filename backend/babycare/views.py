#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import pdb

import oss2
import time
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from django.urls import reverse
from rest_framework import viewsets
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view
from rest_framework.response import Response
from validate_email import validate_email

from babycare.serializers.baby_user import BabyUserSerializer
from babycare.serializers.event import EventSerializer
from backend.settings import OSS_ACCESS_KEY_ID
from backend.settings import OSS_ACCESS_KEY_SECRET
from backend.settings import OSS_BUCKET_NAME
from backend.settings import OSS_ENDPOINT
from constants import CODE_DUPLICATE_EMAIL
from constants import CODE_DUPLICATE_USER
from constants import CODE_EMPTY_EMAIL
from constants import CODE_EMPTY_PASSWORD
from constants import CODE_EMPTY_USER
from constants import CODE_INVALID_EMAIL
from constants import CODE_INVALID_PASSWORD
from constants import CODE_INVALID_REQUEST
from constants import CODE_SUCCESS, MSG_EMPTY_EVENT_TITLE, CODE_EMPTY_EVENT_TITLE, MSG_EMPTY_EVENT_MESSAGE, \
    CODE_EMPTY_EVENT_MESSAGE, \
    MSG_CREATE_EVENT_SUCCESS, MSG_INCORRECT_USER_NAME_OR_PASSWORD, \
    CODE_INCORRECT_USER_NAME_OR_PASSWORD, MSG_NOT_ACTIVE_USER, CODE_NOT_ACTIVE, MSG_LOGIN_SUCCESS, MSG_GET_USERS_SUCCESS, \
    MSG_EMPTY_BABY_NAME, CODE_EMPTY_BABY_NAME, TEMP_IMAGE, PROFILE_FOOTER_IMAGE
from constants import MIN_PASSWORD_LEN
from constants import MSG_400
from constants import MSG_CREATE_USER_SUCCESS
from constants import MSG_DUPLICATE_EMAIL
from constants import MSG_DUPLICATE_USER
from constants import MSG_EMPTY_EMAIL
from constants import MSG_EMPTY_PASSWORD
from constants import MSG_EMPTY_USERNAME
from constants import MSG_INVALID_EMAIL
from constants import MSG_INVALID_PASSWORD
from models import BabyUser
from models import Event
from utils import json_response, invalid_token_response, upload_image_to_oss
from utils import simple_json_response


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'users': reverse('user-list'),
    })


class CustomModelViewSet(viewsets.ModelViewSet):
    code = CODE_SUCCESS
    # permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly,)


class UserViewSet(CustomModelViewSet):
    queryset = BabyUser.objects.all()
    serializer_class = BabyUserSerializer

    def list(self, request, *args, **kwargs):
        # for param in (OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT):
        #     assert '<' not in param, '请设置参数：' + param
        #
        # bucket = oss2.Bucket(oss2.Auth(OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET), OSS_ENDPOINT, OSS_BUCKET_NAME)
        # result = bucket.get_object('test.png', process='image/info')
        # json_content = result.read()
        # decoded_json = json.loads(oss2.to_unicode(json_content))
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid()
        # pdb.set_trace()

        return json_response(super(UserViewSet, self).list(request, *args, **kwargs).data, CODE_SUCCESS, MSG_GET_USERS_SUCCESS)

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        username = request.data.get('username')
        baby_name = request.data.get('baby_name')
        password = request.data.get('password')
        email = request.data.get('email')
        first_name = request.data.get('first_name', '')
        last_name = request.data.get('last_name', '')
        base64 = request.data.get('base64', '')

        if not username:
            return simple_json_response(CODE_EMPTY_USER, MSG_EMPTY_USERNAME)
        elif not baby_name:
            return simple_json_response(CODE_EMPTY_BABY_NAME, MSG_EMPTY_BABY_NAME)
        elif not email:
            return simple_json_response(CODE_EMPTY_EMAIL, MSG_EMPTY_EMAIL)
        elif not password:
            return simple_json_response(CODE_EMPTY_PASSWORD, MSG_EMPTY_PASSWORD)
        elif not validate_email(email):
            return simple_json_response(CODE_INVALID_EMAIL, MSG_INVALID_EMAIL)
        elif len(password) < MIN_PASSWORD_LEN:
            return simple_json_response(CODE_INVALID_PASSWORD, MSG_INVALID_PASSWORD)
        elif User.objects.filter(username=username) or User.objects.filter(username=email.lower()):
            return simple_json_response(CODE_DUPLICATE_USER, MSG_DUPLICATE_USER)
        elif User.objects.filter(email=email.lower()) or User.objects.filter(email=username):
            return simple_json_response(CODE_DUPLICATE_EMAIL, MSG_DUPLICATE_EMAIL)
        elif serializer.is_valid():
            user = User()
            user.email = email.lower()
            user.is_active = True
            user.is_staff = True
            user.set_password(password)
            user.username = username
            user.first_name = first_name
            user.last_name = last_name
            self.request.user = user
            self.perform_create(serializer)
            response_data = serializer.data
            response_data['token'] = Token.objects.create(user=user).key
            if base64:
                image_name = username + time.strftime('%Y%m%d%H%M%S') + PROFILE_FOOTER_IMAGE
                response_data['profile'] = upload_image_to_oss(image_name, base64)
                baby_user = BabyUser.objects.get(user=user)
                baby_user.profile = response_data['profile']
                baby_user.save()
            return json_response(response_data, CODE_SUCCESS, MSG_CREATE_USER_SUCCESS)
        else:
            return simple_json_response(CODE_INVALID_REQUEST, MSG_400)

    def perform_create(self, serializer):
        self.request.user.save()
        serializer.save(user=self.request.user)


@api_view(['POST'])
def login_view(request):
    username = request.data.get('username')
    password = request.data.get('password')

    def get_user(email):
        try:
            return User.objects.get(email=email.lower())
        except User.DoesNotExist:
            return None

    user = authenticate(username=username, password=password)

    if not user and validate_email(username):
        user = get_user(email=username)
        # pdb.set_trace()
        if not user:
            user = authenticate(username=user.username, password=password)

    if user:
        baby = BabyUser.objects.get(user=user)
        if baby:
            if user.is_active:
                response_data = BabyUserSerializer(baby).data
                # pdb.set_trace()
                if Token.objects.filter(user=user):
                    response_data['token'] = Token.objects.get(user=user).key
                    return json_response(response_data, CODE_SUCCESS, MSG_LOGIN_SUCCESS)
                else:
                    return invalid_token_response()
            else:
                return simple_json_response(CODE_NOT_ACTIVE, MSG_NOT_ACTIVE_USER)
        else:
            user.delete()
            return simple_json_response(CODE_INCORRECT_USER_NAME_OR_PASSWORD, MSG_INCORRECT_USER_NAME_OR_PASSWORD)
    else:
        return simple_json_response(CODE_INCORRECT_USER_NAME_OR_PASSWORD, MSG_INCORRECT_USER_NAME_OR_PASSWORD)


class EventViewSet(CustomModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        token = request.data.get('token')
        title = request.data.get('title')
        message = request.data.get('message')

        if not title:
            return simple_json_response(CODE_EMPTY_EVENT_TITLE, MSG_EMPTY_EVENT_TITLE)
        elif not message:
            return simple_json_response(CODE_EMPTY_EVENT_MESSAGE, MSG_EMPTY_EVENT_MESSAGE)
        elif serializer.is_valid():
            if Token.objects.filter(key=token):
                user = Token.objects.get(key=token).user
                serializer.validated_data['baby_id'] = BabyUser.objects.get(user=user).id
                self.perform_create(serializer)
                return json_response(serializer.data, CODE_SUCCESS, MSG_CREATE_EVENT_SUCCESS)
            else:
                return invalid_token_response()
        else:
            return simple_json_response(CODE_INVALID_REQUEST, MSG_400)
