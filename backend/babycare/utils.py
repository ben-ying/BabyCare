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

from babycare.models import Verify
from backend.settings import OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, OSS_BUCKET_NAME, OSS_ENDPOINT, EMAIL_HOST_USER
from constants import CODE_SUCCESS, CODE_INVALID_TOKEN, MSG_401, TEMP_IMAGE, PROFILE_FOOTER_IMAGE, \
    MSG_SEND_VERIFY_CODE_MESSAGES
from constants import MIN_PASSWORD_LEN
import smtplib
from email.mime.text import MIMEText
from rest_framework.authtoken.models import Token


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


def send_email(user, to_email, verify_code, is_email_verify=True):
    msg = dict()
    msg['Subject'] = MSG_SEND_VERIFY_CODE_MESSAGES % verify_code
    msg['From'] = EMAIL_HOST_USER
    msg['To'] = to_email

    if Verify.objects.filter(user=user):
        verify = Verify.objects.get(user=user)
        if is_email_verify:
            verify.email_verify_code = verify_code
    else:
        verify = Verify()
        verify.user = user
        if is_email_verify:
            verify.email_verify_code = verify_code

    # import pdb
    # pdb.set_trace()
    s = smtplib.SMTP('localhost')
    # s = smtplib.SMTP('192.168.1.130:8000')
    # pdb.set_trace()
    s.sendmail(EMAIL_HOST_USER, [to_email], msg)
    s.quit()


def get_user_by_token(token):
    if Token.objects.filter(key=token):
        return Token.objects.get(key=token).user

    return None




