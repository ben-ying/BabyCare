#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import json
import os
import random
import string
from shutil import copyfile

import oss2
import time
from django.http import HttpResponse

from backend.settings import OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT
from constants import CODE_SUCCESS, CODE_INVALID_TOKEN, MSG_401, TEMP_IMAGE, PROFILE_FOOTER_IMAGE
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


def invalid_token_response():
    return simple_json_response(CODE_INVALID_TOKEN, MSG_401)


def password_generator(size=MIN_PASSWORD_LEN, chars=string.ascii_lowercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))


def upload_image_to_oss(name, base64):
    with open(TEMP_IMAGE, "wb") as fh:
        fh.write(base64.decode('base64'))
        fh.close()

        for param in (OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT):
            assert '<' not in param, '请设置参数：' + param

        bucket = oss2.Bucket(oss2.Auth(OSS_ACCESS_KEY_ID,
                                       OSS_ACCESS_KEY_SECRET), OSS_ENDPOINT, OSS_BUCKET_NAME)
        bucket.put_object_from_file(name, TEMP_IMAGE)
        os.remove(TEMP_IMAGE)
        return "https://" + OSS_BUCKET_NAME + "." + OSS_ENDPOINT + "/" + name


