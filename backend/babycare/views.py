#!/usr/bin/env python
# -*- coding: utf-8 -*-

from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth import authenticate
from validate_email import validate_email
from datetime import datetime

from utils import simple_json_response
from models import Baby
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

import pdb


def index(request):
    return HttpResponse("Hello, world!")


def create_user(request):
    if request.method == 'GET':
        baby = Baby()
        baby.email = request.GET.get('email')
        baby.password = request.GET.get('password')
        baby.username = request.GET.get('username')
        baby.phone = request.GET.get('phone', '')
        if not baby.username:
            return simple_json_response(CODE_EMPTY_USER, MSG_EMPTY_USER)
        elif not baby.email:
            return simple_json_response(CODE_EMPTY_EMAIL, MSG_EMPTY_EMAIL)
        elif not baby.password:
            return simple_json_response(CODE_EMPTY_PASSWORD, MSG_EMPTY_PASSWORD)
        elif not validate_email(baby.email):
            return simple_json_response(CODE_INVALID_EMAIL, MSG_INVALID_PASSWORD)
        elif len(baby.password) < MIN_PASSWORD_LEN:
            return simple_json_response(CODE_INVALID_PASSWORD, MSG_INVALID_PASSWORD)
        elif Baby.objects.filter(username=baby.username):
            return simple_json_response(CODE_DUPLICATE_USER, MSG_INVALID_PASSWORD)
        elif Baby.objects.filter(email=baby.email):
            return simple_json_response(CODE_DUPLICATE_EMAIL, MSG_DUPLICATE_EMAIL)
        elif Baby.objects.filter(phone=baby.phone):
            return simple_json_response(CODE_DUPLICATE_PHONE, MSG_DUPLICATE_PHONE)
#         elif not validate_email(baby.email, verify=True):
#             return simple_json_response(CODE_NOT_EXISTS_EMAIL, MSG_NOT_EXISTS_EMAIL)

        baby.firstname = request.GET.get('firstname', '')
        baby.lastname = request.GET.get('lastname', '')
        baby.baby_name = request.GET.get('baby_name')
        baby.gender = request.GET.get('gender')
        baby.profile = request.GET.get('profile')
        baby.region = request.GET.get('region', '')
        baby.whats_up = request.GET.get('whats_up', '')
        baby.birth = request.GET.get('birth')
        baby.hobbies = request.GET.get('hobbies', '')
        baby.created = datetime.now()
        baby.save()

        return simple_json_response(CODE_SUCCESS)


def login(request):
    if request.method == 'GET':
        username = request.GET.get('username')
        password = request.GET.get('password')
        user = authenticate(username = username, password = password)
#         pdb.set_trace()
        if Baby.objects.filter(username = username, password = password):
            system_type = request.GET.get('system_type')
            system_version = request.GET.get('system_version')
            phone_model = request.GET.get('phone_model')
            country = request.GET.get('country')
            state = request.GET.get('state')
            city = request.GET.get('city')
            loginlog = LoginLog()
            loginlog.system_type = system_type
            loginlog.system_version = system_version
            loginlog.phone_model = phone_model
            loginlog.country = country
            loginlog.state = state
            loginlog.city = city
            loginlog.baby = Baby.objects.filter(username = username, password = password)[0]
            loginlog.save()
            return simple_json_response(CODE_SUCCESS)

def add_event(request):
    if request.method == 'GET':
        return simple_json_response(CODE_SUCCESS)


    
