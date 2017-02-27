#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import json
import random
import string

from django.http import HttpResponse

from constants import CODE_SUCCESS
from constants import MIN_PASSWORD_LEN


def json_response(result, code=CODE_SUCCESS, message=''):
    response_data = {}
    response_data['code'] = code
    response_data['message'] = unicode(message)
    response_data['result'] = result
    return HttpResponse(json.dumps(response_data), content_type="application/json")


def simple_json_response(code=CODE_SUCCESS, message=''):
    response_data = {}
    response_data['code'] = code
    response_data['message'] = unicode(message)

    return HttpResponse(json.dumps(response_data), content_type="application/json")


def password_generator(size=MIN_PASSWORD_LEN, chars=string.ascii_lowercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))

