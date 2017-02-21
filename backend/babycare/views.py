#!/usr/bin/env python
# -*- coding: utf-8 -*-
from django.http import Http404
from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth import authenticate
from django.urls import reverse
from django.views.decorators.csrf import csrf_exempt
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
from utils import simple_json_response
from models import Baby, LoginLog
from models import Event
from constants import CODE_SUCCESS
from constants import CODE_EMPTY_USER
from constants import CODE_EMPTY_EMAIL
from constants import CODE_EMPTY_PASSWORD
from constants import CODE_INVALID_EMAIL
from constants import CODE_INVALID_PASSWORD
from constants import CODE_DUPLICATE_USER
from constants import CODE_DUPLICATE_EMAIL
from constants import CODE_DUPLICATE_PHONE
from constants import CODE_NOT_EXISTS_EMAIL
from constants import MSG_EMPTY_USER
from constants import MSG_EMPTY_EMAIL
from constants import MSG_EMPTY_PASSWORD
from constants import MSG_INVALID_EMAIL
from constants import MSG_INVALID_PASSWORD
from constants import MSG_DUPLICATE_USER
from constants import MSG_DUPLICATE_EMAIL
from constants import MSG_DUPLICATE_PHONE
from constants import MSG_NOT_EXISTS_EMAIL
from constants import MIN_PASSWORD_LEN

from django.contrib.auth.models import User
from rest_framework.authtoken.models import Token

import pdb


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'users': reverse('user-list'),
    })


class UserViewSet(viewsets.ModelViewSet):
    queryset = Baby.objects.all()
    serializer_class = BabySerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly,)

    # @detail_route(renderer_class=[renderers.StaticHTMLRenderer])
    def highlight(self, request, *args, **kwargs):
        user = self.get_object()
        return Response(user.highlighted)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


