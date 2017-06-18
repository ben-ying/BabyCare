#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import json
import os
import random
import string

import oss2
from django.contrib.auth.models import User
from django.http import HttpResponse
from rest_framework import viewsets
from rest_framework.authtoken.models import Token
from rest_framework.pagination import PageNumberPagination

from backend.settings import OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT
from constants import CODE_SUCCESS, CODE_INVALID_TOKEN, MSG_401, TEMP_IMAGE, MSG_402, CODE_EXCEPTION, CODE_DUPLICATE, \
    MSG_403, TYPE_IMAGE, TEMP_VIDEO, DIR_EVENT_IMAGE, DIR_EVENT_VIDEO
from constants import MIN_PASSWORD_LEN


class CustomModelViewSet(viewsets.ModelViewSet):
    code = CODE_SUCCESS


def json_response(result, code=CODE_SUCCESS, message=''):
    response_data = dict()
    response_data['code'] = code
    response_data['message'] = unicode(message)
    response_data['result'] = result
    return HttpResponse(json.dumps(response_data), content_type="application/json")


def simple_json_response(code=CODE_SUCCESS, message=''):
    response_data = dict()
    response_data['code'] = code
    response_data['message'] = unicode(message)
    response_data['result'] = {}

    return HttpResponse(json.dumps(response_data), content_type="application/json")


def invalid_token_response():
    return simple_json_response(CODE_INVALID_TOKEN, MSG_401)


def duplicate_response():
    return simple_json_response(CODE_DUPLICATE, MSG_403)


def password_generator(size=MIN_PASSWORD_LEN, chars=string.ascii_lowercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))


def get_user(email):
    try:
        return User.objects.get(email=email.lower())
    except User.DoesNotExist:
        return None


def upload_file_to_oss(name, base64, file_type=TYPE_IMAGE):
    temp_file = TEMP_IMAGE if file_type == TYPE_IMAGE else TEMP_VIDEO
    with open(temp_file, "wb") as fh:
        fh.write(base64.decode('base64'))
        fh.close()

        for param in (OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT):
            assert '<' not in param, '请设置参数：' + param

        bucket = oss2.Bucket(oss2.Auth(OSS_ACCESS_KEY_ID,
                                       OSS_ACCESS_KEY_SECRET), OSS_ENDPOINT, OSS_BUCKET_NAME)
        bucket.put_object_from_file(name, temp_file)
        os.remove(temp_file)
        return "https://" + OSS_BUCKET_NAME + "." + OSS_ENDPOINT + "/" + name


def upload_file(file_path, filename):
    for param in (OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT):
        assert '<' not in param, '请设置参数：' + param

    bucket = oss2.Bucket(oss2.Auth(OSS_ACCESS_KEY_ID,
                                   OSS_ACCESS_KEY_SECRET), OSS_ENDPOINT, OSS_BUCKET_NAME)
    bucket.put_object_from_file(filename, file_path)


def delete_file_from_oss(url, path):
    for param in (OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT):
        assert '<' not in param, '请设置参数：' + param

    bucket = oss2.Bucket(oss2.Auth(OSS_ACCESS_KEY_ID,
                                   OSS_ACCESS_KEY_SECRET), OSS_ENDPOINT, OSS_BUCKET_NAME)
    if url:
        bucket.delete_object(path + url.split('/')[-1])
    return None


def delete_event_file(event):
    username = event.baby.user.username
    if event.image1:
        delete_file_from_oss(event.image1, username + DIR_EVENT_IMAGE)
    if event.image2:
        delete_file_from_oss(event.image2, username + DIR_EVENT_IMAGE)
    if event.image3:
        delete_file_from_oss(event.image3, username + DIR_EVENT_IMAGE)
    if event.image4:
        delete_file_from_oss(event.image4, username + DIR_EVENT_IMAGE)
    if event.image5:
        delete_file_from_oss(event.image5, username + DIR_EVENT_IMAGE)
    if event.image6:
        delete_file_from_oss(event.image6, username + DIR_EVENT_IMAGE)
    if event.image7:
        delete_file_from_oss(event.image7, username + DIR_EVENT_IMAGE)
    if event.image8:
        delete_file_from_oss(event.image8, username + DIR_EVENT_IMAGE)
    if event.image9:
        delete_file_from_oss(event.image9, username + DIR_EVENT_IMAGE)
    if event.video_thumbnail:
        delete_file_from_oss(event.video_thumbnail, username + DIR_EVENT_VIDEO)
    if event.video_url:
        delete_file_from_oss(event.video_url, username + DIR_EVENT_VIDEO)


def get_user_by_token(token):
    if Token.objects.filter(key=token):
        return Token.objects.get(key=token).user

    return None


def save_error_log(request, exception):
    view_name = request.resolver_match.view_name
    # todo
    return simple_json_response(CODE_EXCEPTION, MSG_402)


class LargeResultsSetPagination(PageNumberPagination):
    page_size = 1000
    page_size_query_param = 'page_size'
    max_page_size = 10000


class StandardResultsSetPagination(PageNumberPagination):
    page_size = 100
    page_size_query_param = 'page_size'
    max_page_size = 1000



