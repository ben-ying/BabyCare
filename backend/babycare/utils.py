#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import json
import os
import random
import string
import pdb
from shutil import copyfile

import oss2
import time

from django.contrib.auth.models import User
from django.http import HttpResponse
from rest_framework import viewsets

from babycare.models import Verify
from backend.settings import OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT, EMAIL_HOST_USER
from constants import CODE_SUCCESS, CODE_INVALID_TOKEN, MSG_401, TEMP_IMAGE, PROFILE_FOOTER_IMAGE, \
    PASSWORD_VERIFY_CODE_EMAIL_SUBJECT, PASSWORD_VERIFY_CODE_EMAIL_CONTENT
from constants import MIN_PASSWORD_LEN
import smtplib
from email.mime.text import MIMEText
from rest_framework.authtoken.models import Token
from django.core.mail import EmailMessage


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


def password_generator(size=MIN_PASSWORD_LEN, chars=string.ascii_lowercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))


def get_user(email):
    try:
        return User.objects.get(email=email.lower())
    except User.DoesNotExist:
        return None


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


def send_email(user, to_email, verify_code, is_email_verify=True):
    if Verify.objects.filter(user=user):
        verify = Verify.objects.get(user=user)
        if is_email_verify:
            verify.email_verify_code = verify_code
    else:
        verify = Verify()
        verify.user = user
        if is_email_verify:
            verify.email_verify_code = verify_code

    email = EmailMessage(PASSWORD_VERIFY_CODE_EMAIL_SUBJECT, PASSWORD_VERIFY_CODE_EMAIL_CONTENT %verify_code, to=[to_email])
    try:
        email.send()
        verify.save()
    except smtplib.SMTPDataError:
        # todo not send email
        pass


def get_user_by_token(token):
    if Token.objects.filter(key=token):
        return Token.objects.get(key=token).user

    return None




