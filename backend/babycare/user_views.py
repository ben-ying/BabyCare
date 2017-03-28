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
from django.utils.crypto import get_random_string
from babycare.serializers.baby_user import BabyUserSerializer
from babycare.serializers.event import EventSerializer
from backend.settings import OSS_ACCESS_KEY_ID
from backend.settings import OSS_ACCESS_KEY_SECRET
from backend.settings import OSS_BUCKET_NAME
from backend.settings import OSS_ENDPOINT
from constants import CODE_DUPLICATE_EMAIL, MSG_SEND_VERIFY_CODE_SUCCESS, MSG_NO_SUCH_EMAIL, MSG_EMPTY_VERIFY_CODE, \
    CODE_EMPTY_VERIFY_CODE, MSG_USER_NOT_EXISTS, CODE_USER_NOT_EXISTS
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
    CODE_INCORRECT_USER_NAME_OR_PASSWORD, MSG_NOT_ACTIVE_USER, CODE_NOT_ACTIVE, MSG_LOGIN_SUCCESS, \
    MSG_GET_USERS_SUCCESS, \
    MSG_EMPTY_BABY_NAME, CODE_EMPTY_BABY_NAME, PROFILE_FOOTER_IMAGE
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
from models import BabyUser, Verify
from models import Event
from utils import json_response, invalid_token_response, upload_image_to_oss, send_email, get_user_by_token
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
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid()

        return json_response(super(UserViewSet, self).list(request, *args, **kwargs).data, CODE_SUCCESS,
                             MSG_GET_USERS_SUCCESS)

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

    def update(self, request, *args, **kwargs):
        token = request.data.get('token')
        baby_name = request.data.get('baby_name')
        phone = request.data.get('phone')
        email = request.data.get('email')
        gender = request.data.get('gender')
        birthday = request.data.get('birthday')
        hobbies = request.data.get('hobbies')
        base64 = request.data.get('base64')
        user = get_user_by_token(token)

        if user:
            if BabyUser.objects.filter(user=user):
                if email:
                    user.email = email
                    user.save()

                baby = BabyUser.objects.get(user=user)
                if baby:
                    if baby_name:
                        baby.baby_name = baby_name
                    if phone:
                        baby.phone = phone
                    if gender:
                        baby.gender = gender
                    if birthday:
                        baby.birth = birthday
                    if hobbies:
                        baby.hobbies = hobbies
                    if base64:
                        image_name = user.username + time.strftime('%Y%m%d%H%M%S') + PROFILE_FOOTER_IMAGE
                        profile = upload_image_to_oss(image_name, base64)
                        baby.profile = profile
                    baby.save()

                # pdb.set_trace()
                response_data = BabyUserSerializer(baby).data
                response_data['token'] = Token.objects.get(user=user).key

                return json_response(response_data, CODE_SUCCESS, MSG_LOGIN_SUCCESS)

        return invalid_token_response()


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


@api_view(['POST'])
def send_verify_code_view(request):
    email = request.data.get('email')

    # pdb.set_trace()
    if not email:
        return simple_json_response(CODE_EMPTY_EMAIL, MSG_EMPTY_EMAIL)
    elif not User.objects.filter(email=email.lower()) and \
            not User.objects.filter(username=email.lower()):
        return simple_json_response(CODE_INVALID_EMAIL, MSG_NO_SUCH_EMAIL)

    if User.objects.filter(email=email.lower()):
        baby = User.objects.get(email=email.lower())
    elif User.objects.filter(username=email.lower()):
        baby = User.objects.get(username=email.lower())

    verify_code = get_random_string(length=4).lower()
    send_email(baby, email, verify_code)

    return simple_json_response(CODE_SUCCESS, MSG_SEND_VERIFY_CODE_SUCCESS)


@api_view(['POST'])
def verify_code_view(request):
    code = request.data.get('code')
    token = request.data.get('token')
    user = get_user_by_token(token)
    # pdb.set_trace()
    if not code:
        return simple_json_response(CODE_EMPTY_VERIFY_CODE, MSG_EMPTY_VERIFY_CODE)

    if user:
        if Verify.objects.filter(user=user, email_verify_code=code):
            return simple_json_response(CODE_SUCCESS, MSG_SEND_VERIFY_CODE_SUCCESS)
        else:
            return simple_json_response(CODE_USER_NOT_EXISTS, MSG_USER_NOT_EXISTS)
    else:
        return invalid_token_response()


class EventViewSet(CustomModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        token = request.data.get('token')
        title = request.data.get('title')
        content = request.data.get('content')
        user = get_user_by_token(token)

        if not title:
            return simple_json_response(CODE_EMPTY_EVENT_TITLE, MSG_EMPTY_EVENT_TITLE)
        elif not content:
            return simple_json_response(CODE_EMPTY_EVENT_MESSAGE, MSG_EMPTY_EVENT_MESSAGE)
        elif serializer.is_valid():
            if user:
                serializer.validated_data['baby_id'] = BabyUser.objects.get(user=user).id
                self.perform_create(serializer)
                return json_response(serializer.data, CODE_SUCCESS, MSG_CREATE_EVENT_SUCCESS)
            else:
                return invalid_token_response()
        else:
            return simple_json_response(CODE_INVALID_REQUEST, MSG_400)
