#!/usr/bin/env python
# -*- coding: utf-8 -*-
import smtplib
import time
import pdb

from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from django.urls import reverse
from django.utils import timezone
from django.utils.crypto import get_random_string
from django.utils.dateformat import format
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view
from rest_framework.response import Response
from validate_email import validate_email
from django.core.mail import EmailMessage

from babycare.constants import CODE_DUPLICATE_EMAIL, MSG_SEND_VERIFY_CODE_SUCCESS, MSG_NO_SUCH_EMAIL, \
    MSG_EMPTY_VERIFY_CODE, \
    CODE_EMPTY_VERIFY_CODE, MSG_INCORRECT_VERIFY_CODE, CODE_INCORRECT_VERIFY_CODE, CODE_EXPIRED_VERIFY_CODE, \
    MSG_EXPIRED_VERIFY_CODE, \
    VERIFY_CODE_EXPIRED_TIME, CODE_USER_NOT_EXISTS, MSG_USER_NOT_EXISTS, MSG_GET_USER_DETAIL_SUCCESS, \
    MSG_DUPLICATE_PHONE, CODE_DUPLICATE_PHONE, MSG_GET_APP_INFO_SUCCESS, PASSWORD_VERIFY_CODE_EMAIL_SUBJECT, \
    PASSWORD_VERIFY_CODE_EMAIL_CONTENT, DIR_USER_PROFILE, MSG_UPDATE_USER_INFO_SUCCESS
from babycare.constants import CODE_DUPLICATE_USER
from babycare.constants import CODE_EMPTY_EMAIL
from babycare.constants import CODE_EMPTY_PASSWORD
from babycare.constants import CODE_EMPTY_USER
from babycare.constants import CODE_INVALID_EMAIL
from babycare.constants import CODE_INVALID_PASSWORD
from babycare.constants import CODE_INVALID_REQUEST
from babycare.constants import CODE_SUCCESS, MSG_INCORRECT_USER_NAME_OR_PASSWORD, \
    CODE_INCORRECT_USER_NAME_OR_PASSWORD, MSG_NOT_ACTIVE_USER, CODE_NOT_ACTIVE, MSG_LOGIN_SUCCESS, \
    MSG_GET_USERS_SUCCESS, \
    MSG_EMPTY_BABY_NAME, CODE_EMPTY_BABY_NAME, PROFILE_FOOTER_IMAGE
from babycare.constants import MIN_PASSWORD_LEN
from babycare.constants import MSG_400
from babycare.constants import MSG_CREATE_USER_SUCCESS
from babycare.constants import MSG_DUPLICATE_EMAIL
from babycare.constants import MSG_DUPLICATE_USER
from babycare.constants import MSG_EMPTY_EMAIL
from babycare.constants import MSG_EMPTY_PASSWORD
from babycare.constants import MSG_EMPTY_USERNAME
from babycare.constants import MSG_INVALID_EMAIL
from babycare.constants import MSG_INVALID_PASSWORD
from babycare.models import BabyUser, Verify, Event, AppInfo
from babycare.serializers.app_info import AppInfoSerializer
from babycare.serializers.baby import BabyUserSerializer
from babycare.serializers.event import EventSerializer
from babycare.utils import json_response, invalid_token_response, upload_file_to_oss, get_user_by_token, \
    get_user, \
    CustomModelViewSet, save_error_log
from babycare.utils import simple_json_response


@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'users': reverse('user-list'),
    })

    # permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly,)


class UserViewSet(CustomModelViewSet):
    queryset = BabyUser.objects.all()
    serializer_class = BabyUserSerializer

    def list(self, request, *args, **kwargs):
        try:
            serializer = self.get_serializer(data=request.data)
            serializer.is_valid()
            token = request.data.get('token')
            user = get_user_by_token(token)

            if user:
                return json_response(super(UserViewSet, self).list(request, *args, **kwargs).data,
                                     CODE_SUCCESS, MSG_GET_USERS_SUCCESS)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)

    def create(self, request, *args, **kwargs):
        try:
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
            elif User.objects.filter(username__iexact=username) or User.objects.filter(username__iexact=email):
                return simple_json_response(CODE_DUPLICATE_USER, MSG_DUPLICATE_USER)
            elif User.objects.filter(email__iexact=email) or User.objects.filter(email__iexact=username):
                return simple_json_response(CODE_DUPLICATE_EMAIL, MSG_DUPLICATE_EMAIL)
            elif serializer.is_valid():
                user = User()
                user.email = email.lower()
                user.is_active = True
                user.is_staff = True
                user.set_password(password)
                user.username = username.lower()
                user.first_name = first_name
                user.last_name = last_name
                self.request.user = user
                self.perform_create(serializer)
                response_data = serializer.data
                response_data['token'] = Token.objects.create(user=user).key
                if base64:
                    image_name = username + time.strftime('%Y%m%d%H%M%S') + PROFILE_FOOTER_IMAGE
                    response_data['profile'] = upload_file_to_oss(user.username + DIR_USER_PROFILE + image_name, base64)
                    baby_user = BabyUser.objects.get(user=user)
                    baby_user.profile = response_data['profile']
                    baby_user.locale = response_data['locale']
                    baby_user.created = timezone.now()
                    baby_user.save()
                return json_response(response_data, CODE_SUCCESS, MSG_CREATE_USER_SUCCESS)
            else:
                return simple_json_response(CODE_INVALID_REQUEST, MSG_400)
        except Exception as e:
            return save_error_log(request, e)

    def perform_create(self, serializer):
        self.request.user.save()
        serializer.save(user=self.request.user)

    def retrieve(self, request, *args, **kwargs):
        token = request.query_params.get('token')
        try:
            user = get_user_by_token(token)
            if user:
                response = super(UserViewSet, self).retrieve(request, *args, **kwargs).data
                baby = self.get_object()
                events = Event.objects.filter(baby=baby)
                event_list = list()

                for event in events:
                    event_json = EventSerializer(event).data
                    event_list.append(event_json)
                response['events'] = event_list
                return json_response(response, CODE_SUCCESS, MSG_GET_USER_DETAIL_SUCCESS)
            else:
                return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)

    def update(self, request, *args, **kwargs):
        token = request.data.get('token')
        baby_name = request.data.get('baby_name')
        phone = request.data.get('phone')
        email = request.data.get('email')
        gender = request.data.get('gender')
        birthday = request.data.get('birthday')
        hobbies = request.data.get('hobbies')
        base64 = request.data.get('base64')

        try:
            user = get_user_by_token(token)
            if user:
                if BabyUser.objects.filter(user=user):
                    baby = BabyUser.objects.get(user=user)

                    if email:
                        if User.objects.filter(username__iexact=email) or User.objects.filter(email__iexact=email):
                            return simple_json_response(CODE_DUPLICATE_USER, MSG_DUPLICATE_EMAIL)
                        else:
                            user.email = email
                            user.save()
                    if baby_name:
                        baby.baby_name = baby_name
                    if phone:
                        if BabyUser.objects.filter(phone=phone):
                            return simple_json_response(CODE_DUPLICATE_PHONE, MSG_DUPLICATE_PHONE)
                        else:
                            baby.phone = phone
                    if gender:
                        baby.gender = gender
                    if birthday:
                        baby.birth = birthday
                    if hobbies:
                        baby.hobbies = hobbies
                    if base64:
                        image_name = user.username + time.strftime('%Y%m%d%H%M%S') + PROFILE_FOOTER_IMAGE
                        profile = upload_file_to_oss(user.username + DIR_USER_PROFILE + image_name, base64)
                        baby.profile = profile
                    baby.save()

                    response_data = BabyUserSerializer(baby).data
                    response_data['token'] = Token.objects.get(user=user).key

                    return json_response(response_data, CODE_SUCCESS, MSG_UPDATE_USER_INFO_SUCCESS)

            return invalid_token_response()
        except Exception as e:
            return save_error_log(request, e)


@api_view(['POST'])
def login_view(request):
    username = request.data.get('username', '').lower()
    password = request.data.get('password')
    token = request.data.get('token')

    try:
        if token:
            user = Token.objects.get(key=token).user
        else:
            user = authenticate(username=username, password=password)
            if not user and validate_email(username):
                user = get_user(email=username)
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
    except Exception as e:
        return save_error_log(request, e)

@api_view(['POST'])
def send_verify_code_view(request):
    email = request.data.get('email')
    try:
        if not email:
            return simple_json_response(CODE_EMPTY_EMAIL, MSG_EMPTY_EMAIL)
        elif not User.objects.filter(email=email.lower()) and \
                not User.objects.filter(username=email.lower()):
            return simple_json_response(CODE_INVALID_EMAIL, MSG_NO_SUCH_EMAIL)

        baby = None
        if User.objects.filter(email=email.lower()):
            user = User.objects.get(email=email.lower())
            baby = BabyUser.objects.get(user=user)
        elif User.objects.filter(username=email.lower()):
            user = User.objects.get(username=email.lower())
            baby = BabyUser.objects.get(user=user)

        verify_code = get_random_string(length=6, allowed_chars='0123456789').lower()
        send_email(baby, email, verify_code)

        return simple_json_response(CODE_SUCCESS, MSG_SEND_VERIFY_CODE_SUCCESS)
    except Exception as e:
        return save_error_log(request, e)


def send_email(baby, to_email, verify_code, is_email_verify=True):
    if Verify.objects.filter(baby=baby):
        verify = Verify.objects.get(baby=baby)
        if is_email_verify:
            verify.email_verify_code = verify_code
    else:
        verify = Verify()
        verify.baby = baby
        if is_email_verify:
            verify.email_verify_code = verify_code

    email = EmailMessage(PASSWORD_VERIFY_CODE_EMAIL_SUBJECT,
                         PASSWORD_VERIFY_CODE_EMAIL_CONTENT % verify_code, to=[to_email])
    try:
        email.send()
        verify.save()
    except smtplib.SMTPDataError:
        # todo not send email
        pass


@api_view(['POST'])
def reset_password_with_verify_code_view(request):
    code = request.data.get('verify_code')
    email = request.data.get('email')
    password = request.data.get('password')

    try:
        if not code:
            return simple_json_response(CODE_EMPTY_VERIFY_CODE, MSG_EMPTY_VERIFY_CODE)
        elif not email:
            return simple_json_response(CODE_EMPTY_EMAIL, MSG_EMPTY_EMAIL)
        elif not password:
            return simple_json_response(CODE_EMPTY_PASSWORD, MSG_EMPTY_PASSWORD)
        elif not validate_email(email):
            return simple_json_response(CODE_INVALID_EMAIL, MSG_INVALID_EMAIL)
        elif len(password) < MIN_PASSWORD_LEN:
            return simple_json_response(CODE_INVALID_PASSWORD, MSG_INVALID_PASSWORD)

        user = get_user(email=email)
        if user:
            baby = BabyUser.objects.get(user=user)
            if Verify.objects.filter(baby=baby, email_verify_code=code.lower()):
                verify = Verify.objects.get(baby=baby, email_verify_code=code.lower())
                if (time.time() - float(format(verify.created, 'U'))) > VERIFY_CODE_EXPIRED_TIME:
                    return simple_json_response(CODE_EXPIRED_VERIFY_CODE, MSG_EXPIRED_VERIFY_CODE)
                else:
                    user.set_password(password)
                    user.save()
                    return simple_json_response(CODE_SUCCESS, MSG_SEND_VERIFY_CODE_SUCCESS)
            else:
                return simple_json_response(CODE_INCORRECT_VERIFY_CODE, MSG_INCORRECT_VERIFY_CODE)
        else:
            return simple_json_response(CODE_USER_NOT_EXISTS, MSG_USER_NOT_EXISTS)
    except Exception as e:
        return save_error_log(request, e)


@api_view(['POST'])
def get_app_info(request):
    try:
        token = request.data.get('token')
        user = get_user_by_token(token)
        if user:
            response_data = dict()
            if AppInfo.objects.all():
                app_info = AppInfo.objects.all().order_by('-id')[0]
                response_data = AppInfoSerializer(app_info).data
                response_data['app_name'] = app_info.app_file.file.name.split("/")[-1].replace('.apk', '')
            return json_response(response_data, CODE_SUCCESS, MSG_GET_APP_INFO_SUCCESS)
        else:
            return invalid_token_response()
    except Exception as e:
        return save_error_log(request, e)
