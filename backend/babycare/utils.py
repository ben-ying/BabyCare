#!/usr/bin/env python
# -*- coding: utf-8 -*- 
from django.http import HttpResponse
from constants import CODE_SUCCESS
from constants import MIN_PASSWORD_LEN

import json
import random
import string

def json_response(response_data, result, code=CODE_SUCCESS, message=''):
    response_data['code'] = code
    response_data['message'] = message
    response_data['result'] = result
    return HttpResponse(json.dumps(response_data), content_type="application/json")


def simple_json_response(code=CODE_SUCCESS, message=''):
    response_data = {}
    response_data['code'] = code
    response_data['message'] = unicode(message)
    print response_data['message']
#     import pdb; pdb.set_trace();
        
    return HttpResponse(json.dumps(response_data), content_type="application/json")


def password_generator(size=MIN_PASSWORD_LEN, chars=string.ascii_lowercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))

