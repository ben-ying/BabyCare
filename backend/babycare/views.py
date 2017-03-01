#!/usr/bin/env python
# -*- coding: utf-8 -*-
from django.http import Http404
from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth import authenticate
from django.urls import reverse
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt, csrf_protect
from rest_framework import generics
from rest_framework import mixins
from rest_framework import permissions
from rest_framework import renderers
from rest_framework import status
from rest_framework import viewsets
from rest_framework.decorators import api_view, detail_route
from rest_framework.parsers import JSONParser
from rest_framework.renderers import JSONRenderer
from rest_framework.response import Response
from rest_framework.views import APIView
from validate_email import validate_email
from datetime import datetime

from babycare.permissions import IsOwnerOrReadOnly
from babycare.serializers.baby import BabySerializer
from babycare.serializers.event import EventSerializer
from utils import json_response, invalid_token_response
from utils import simple_json_response
from models import Baby, LoginLog
from models import Event
from constants import CODE_SUCCESS, MSG_EMPTY_EVENT_TITLE, CODE_EMPTY_EVENT_TITLE, MSG_EMPTY_EVENT_MESSAGE, \
    CODE_EMPTY_EVENT_MESSAGE, \
    MSG_401, CODE_INVALID_TOKEN, MSG_CREATE_EVENT_SUCCESS, MSG_INCORRECT_USER_NAME_OR_PASSWORD, \
    CODE_INCORRECT_USER_NAME_OR_PASSWORD, MSG_NOT_ACTIVE_USER, CODE_NOT_ACTIVE, MSG_LOGIN_SUCCESS
from constants import CODE_EMPTY_USER
from constants import CODE_EMPTY_EMAIL
from constants import CODE_EMPTY_PASSWORD
from constants import CODE_INVALID_EMAIL
from constants import CODE_INVALID_PASSWORD
from constants import CODE_DUPLICATE_USER
from constants import CODE_DUPLICATE_EMAIL
from constants import CODE_DUPLICATE_PHONE
from constants import CODE_NOT_EXISTS_EMAIL
from constants import CODE_INVALID_REQUEST
from constants import MSG_400
from constants import MSG_EMPTY_USER
from constants import MSG_EMPTY_EMAIL
from constants import MSG_EMPTY_PASSWORD
from constants import MSG_INVALID_EMAIL
from constants import MSG_INVALID_PASSWORD
from constants import MSG_DUPLICATE_USER
from constants import MSG_DUPLICATE_EMAIL
from constants import MSG_DUPLICATE_PHONE
from constants import MSG_NOT_EXISTS_EMAIL
from constants import MSG_CREATE_USER_SUCCESS
from constants import MIN_PASSWORD_LEN

from django.contrib.auth.models import User
from rest_framework.authtoken.models import Token

import pdb


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'users': reverse('user-list'),
    })


class CustomModelViewSet(viewsets.ModelViewSet):
    code = CODE_SUCCESS
    # permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly,)


class UserViewSet(CustomModelViewSet):
    queryset = Baby.objects.all()
    serializer_class = BabySerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        username = request.data.get('username')
        password = request.data.get('password')
        email = request.data.get('email')
        first_name = request.data.get('first_name', '')
        last_name = request.data.get('last_name', '')

        if not username:
            return simple_json_response(CODE_EMPTY_USER, MSG_EMPTY_USER)
        elif not email:
            return simple_json_response(CODE_EMPTY_EMAIL, MSG_EMPTY_EMAIL)
        elif not password:
            return simple_json_response(CODE_EMPTY_PASSWORD, MSG_EMPTY_PASSWORD)
        elif not validate_email(email):
            return simple_json_response(CODE_INVALID_EMAIL, MSG_INVALID_EMAIL)
        elif len(password) < MIN_PASSWORD_LEN:
            return simple_json_response(CODE_INVALID_PASSWORD, MSG_INVALID_PASSWORD)
        elif User.objects.filter(username=username):
            return simple_json_response(CODE_DUPLICATE_USER, MSG_DUPLICATE_USER)
        elif User.objects.filter(email=email.lower):
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

    # def get_user(email):
    #     try:
    #         return User.objects.get(email=email.lower())
    #     except User.DoesNotExist:
    #         return None

    user = authenticate(username=username, password=password)

    if user:
        baby = Baby.objects.get(user=user)
        if baby:
            if user.is_active:
                return json_response(BabySerializer(baby).data, CODE_SUCCESS, MSG_LOGIN_SUCCESS)
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
                serializer.validated_data['baby_id'] = Baby.objects.get(user=user).id
                self.perform_create(serializer)
                return json_response(serializer.data, CODE_SUCCESS, MSG_CREATE_EVENT_SUCCESS)
            else:
                return invalid_token_response()
        else:
            return simple_json_response(CODE_INVALID_REQUEST, MSG_400)
