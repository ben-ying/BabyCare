#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os

from django.utils.translation import ugettext_lazy as _

MIN_PASSWORD_LEN = 6
TEMP_IMAGE = os.path.join(os.path.dirname(__file__), 'temp.jpg')
PROFILE_FOOTER_IMAGE = '_profile.jpg'
EVENT_FOOTER_IMAGE = '_event.jpg'

# register & user
CODE_SUCCESS = 200
CODE_EMPTY_USER = 201
CODE_EMPTY_BABY_NAME = 202
CODE_EMPTY_EMAIL = 203
CODE_EMPTY_PASSWORD = 204
CODE_INVALID_EMAIL = 205
CODE_INVALID_PASSWORD = 206
CODE_DUPLICATE_USER = 207
CODE_DUPLICATE_EMAIL = 208
CODE_DUPLICATE_PHONE = 209
CODE_NOT_EXISTS_EMAIL = 210
CODE_NOT_ACTIVE = 211
CODE_INCORRECT_USER_NAME_OR_PASSWORD = 212

# event
CODE_EMPTY_EVENT_TITLE = 300
CODE_EMPTY_EVENT_MESSAGE = 301

CODE_INVALID_REQUEST = 400
CODE_INVALID_TOKEN = 401
MSG_400 = _(u'请求数据格式不正确')
MSG_401 = _(u'AccessToken异常')

# register message
MSG_EMPTY_USERNAME = _(u'用户名不能为空')
MSG_EMPTY_BABY_NAME = _(u'宝宝名不能为空')
MSG_EMPTY_EMAIL = _(u'邮箱不能为空')
MSG_EMPTY_PASSWORD = _(u'密码不能为空')
MSG_INVALID_EMAIL = _(u'邮箱格式不正确')
MSG_INVALID_PASSWORD = _(u'密码不能少于6位')
MSG_DUPLICATE_USER = _(u'该用户已存在')
MSG_DUPLICATE_EMAIL = _(u'该邮箱已存在')
MSG_DUPLICATE_PHONE = _(u'该手机号码已存在')
MSG_NOT_EXISTS_EMAIL = _(u'该邮箱不存在')
MSG_CREATE_USER_SUCCESS = _(u'用户创建成功')
MSG_GET_USERS_SUCCESS = _(u'获取用户成功')
MSG_LOGIN_SUCCESS = _(u'登入成功')
MSG_NOT_ACTIVE_USER = _(u'该用户暂时不可用')
MSG_INCORRECT_USER_NAME_OR_PASSWORD = _(u'用户名或账号错误')

# event message
MSG_EMPTY_EVENT_TITLE = _(u'标题不能为空')
MSG_EMPTY_EVENT_MESSAGE = _(u'内容不能为空')
MSG_CREATE_EVENT_SUCCESS = _(u'添加成功')

